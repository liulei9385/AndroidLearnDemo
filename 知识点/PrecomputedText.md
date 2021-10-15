# PrecomputedText

[PrecomputedText](https://developer.android.com/reference/android/text/PrecomputedText), announced at I/O this year, is a [new API in Android P](https://android-developers.googleblog.com/2018/07/whats-new-for-text-in-android-p.html), with a [compat version in Jetpack](https://developer.android.com/reference/androidx/core/text/PrecomputedTextCompat) that reduces UI thread costs of text. It lets you pre-compute the vast majority of measure/layout work on a background thread.

From the UI thread, determine layout parameters from a TextView:

```kotlin
val params : PrecomputedTextCompat.Params =
        TextViewCompat.getTextMetricsParams(myTextView)
```

Do the computation of an expensive paragraph of text on a background thread:

```kotlin
val precomputedText : Spannable =
        PrecomputedTextCompat.create(expensiveText, params)
```

Use the produced PrecomputedText in a TextView:

```kotlin
TextViewCompat.setPrecomputedText(myTextView, precomputedText)
```

This moves more than 90% of the work off of the UI thread — a huge performance win! `PrecomputedTextCompat`, the AndroidX implementation, works on L (API 21) and above, so the performance gains apply to about 85% of active devices ([as of May this year](https://developer.android.com/about/dashboards/)).

This improvement is great, but **the async pattern above isn’t great for displaying text**. For example in RecyclerView, the text in an item should be visible immediately, not once it has scrolled halfway up the screen. To accomplish this, we need to know text parameters early, and do background thread work before the TextView is displayed.

How can we start this PrecomputedText work early, so we don’t need to defer text presentation?

# First approach — pre-compute layout

Using a background thread to preprocess text works great when you’re already loading text data asynchronously. For example, it’s common to load data from the network, and deserialize it, possibly styling it with spans before sending to the UI thread.

It’s very tempting to also use PrecomputedText here, just after deserialization. We’d like to pre-process the text fully before sending to the UI to minimize UI thread work:

```
/* Worker Thread */// resolve spans on worker thread to reduce load on UI thread
val expensiveSpanned = resolveIntoSpans(networkData.item.textData)
    
// pre-compute measurement work to reduce load on UI thread
val textParams: PrecomputedTextCompat.Params = // we’ll get to this
val precomputedText: PrecomputedTextCompat = 
    PrecomputedTextCompat.create(expensiveSpanned, params)
```

Then you can present the PrecomputedText sent from your loading thread in your UI:

```
/* UI Thread */myTextView.setTextMetricParams(precomputedText.getParams())
myTextView.setPrecomputedText(precomputedText)
```

However, we skipped over how to get the `PrecomputedTextCompat.Params`, and that’s the tricky part.

## The sizing problem

PrecomputedText needs to know glyph sizes to do measurement. This means it needs to know size, not in **sp** (as its typically specified in XML), but in **pixels**, adjusted for text density. Transforming **sp** to **pixels** uses `DisplayMetrics.scaledDensity`, which isn’t easy to simply pass to a background thread.

The platform changes `scaledDensity` at runtime to support user-defined text scaling. If you only query the DisplayMetrics once, and pass them to a background thread, your user could come back from changing system-wide font size to an app that has only half of its TextViews sized correctly — not a great experience.

It’s possible to handle this correctly, but you need to be very careful — any time the activity is recreated, be sure to query `scaledDensity`, and if it changed, drop and rebuild any cached PrecomputedText.

# Second approach — prefetching with Future<>

It would be nice to get the benefits of PrecomputedText without changing much code, or having to worry about bouncing text sizing information across threads. Let’s take a look at some typical RecyclerView binding code, which sets up a TextView:

```kotlin
override fun onBindViewHolder(vh: ViewHolder, position: Int) {
    val itemData = getData(position)    vh.textView.textSize = if (item.isImportant) 14 else 10
    vh.textView.text = itemData.text
}
```

Ideally, this sort of code would be easy to adapt to use PrecomputedText.

What makes precomputing text difficult is that Android TextViews perform layout almost immediately after `setText()` is called, and text styling wasn’t complete until just before that (note the dynamic `textSize` property). This means we don’t have a gap of time to put background text layout work in.

However there’s one important feature that breaks this expectation — [RecyclerView Prefetch](https://medium.com/google-developers/recyclerview-prefetch-c2f269075710). Because of prefetch, RecyclerView actually calls `onBindViewHolder()` several frames earlier than item layout. It does this to avoid doing expensive work inflate/bind work at the last moment, just when the content is about to display.

Fortunately for PrecomputedText, this has the nice side effect of leaving dozens of milliseconds between bind and layout, a ideal gap for precomputing text on a background thread!

In the most recent Jetpack beta, we’ve added additional features to support exactly this use case. It’s now possible to produce a `Future<PrecomputedTextCompat>`, and [inform AppCompatTextView to block on that future](https://developer.android.com/reference/androidx/appcompat/widget/AppCompatTextView#setTextFuture(java.util.concurrent.Future)) next during its next measure() pass.

```kotlin
override fun onBindViewHolder(vh: ViewHolder, position: Int) {
    val itemData = getData(position)    // first, modify item-dependent properties
    vh.textView.textSize = if (item.isImportant) 14 else 10    // Pass text computation future to AppCompatTextView,
    // which awaits result before measuring.
    textView.setTextFuture(PrecomputedTextCompat.getTextFuture(
            item.text,
            TextViewCompat.getTextMetricsParams(textView),
            /*optional custom executor*/ null))
}
```

By creating the Future, the app kicks off the background thread PrecomputedText work. Instead of waiting on that work inside of our bind code, we tell TextView to wait, and only block on the result just before it is measured. As RecyclerView scrolls, items are inflated/bound early, and PrecomputedText takes advantage of that to hide the cost of text.

With just a few lines of code, we’ve added text prefetch, reducing TextView measure time by 95%!

# Caveats

Note that you’re querying `TextViewCompat.getTextMetricsParams()` and passing it to a background thread. Because of this, it’s important to not change TextView properties after `setTextFuture()`, (unless you’re rebinding, and call `setTextFuture()` immediately after). Modifying properties can result in the PrecomputedText being incompatible with the TextView, in which case an exception will be thrown during measurement.

PrecomputedTextCompat relies on Android’s word layout cache, which didn’t exist until the Lollipop release (API 21). For this reason, PrecomputedTextCompat does no precomputation on platform versions older than Lollipop.

This approach won’t help if you have [disabled prefetch](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.LayoutManager#setitemprefetchenabled), or if you’re using a custom LayoutManager which doesn’t explicitly support prefetch. If you use a custom LayoutManager, be sure it implements `collectAdjacentPrefetchPositions()` so RecyclerView knows which items to prefetch. Note also that prefetch only applies to views scrolling on-screen, but fortunately this is when performance matters most.

# Data Binding

If you’re a user of the Android Data Binding, you can get the same benefits of the PrecomputedText future with a custom [BindingAdapter](https://developer.android.com/reference/android/databinding/BindingAdapter). First, we’ll use Data Binding as normal in XML, but use an ‘asyncText’ property:

```
<layout
    xmlns:tools="http://schemas.android.com/tools"
    xmlns:android="http://schemas.android.com/apk/res/android">
    <data>
        <variable name="item" type="com.example.ItemData"/>
    </data>
    <TextView
        android:id="@+id/item_text"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="@{item.isImportant ? 14 : 10}"
        app:asyncText="@{item.text}"/>
</layout>
```

Now in our BindingAdapter, we can define that `asyncText` property, and implement it to use PrecomputedText and `setTextFuture` under the hood:

```kotlin
@BindingAdapter(
        "app:asyncText",
        "android:textSize",
        requireAll = false)
fun asyncText(view: TextView, text: CharSequence, textSize: Int?) {
    // first, set all measurement affecting properties of the text
    // (size, locale, typeface, direction, etc)
    if (textSize != null) {
        // interpret the text size as SP
        view.textSize = textSize.toFloat()
    }
    val params = TextViewCompat.getTextMetricsParams(view)
   (view as AppCompatTextView).setTextFuture(
           PrecomputedTextCompat.getTextFuture(text, params, null)
}
```

Note that we’re careful to consume all text-layout relevant properties in our BindingAdapter, to avoid other BindingAdapters handling them. We need to guarantee our call to getTextMetricsParams runs after the TextView has all other properties bound, and Data Binding doesn’t provide guarantees about which adapter is called when. To be safe, we take any TextView properties that may be controlled by Data Binding, and apply all of them at the beginning of our adapter.

```
override fun onBindViewHolder(holder: Holder, position: Int) {
    holder.binding.item = getItem(position)
    holder.binding.executePendingBindings()
}
```

Finally, we will call `executePendingBindings()` so that the list item is updated without waiting for the next layout phase. This is required when using Data Binding inside RecyclerView, even if you are not using PrecomputedText.

# Conclusion

PrecomputedText solves text layout performance in RecyclerView, one of the most major performance issues in scrolling performance. With only a few lines of code, and the latest Jetpack, you can reduce text measurement cost by 95%!

For now, we recommend trying PrecomputedText with list item TextViews that commonly display 200 characters or more. We’d love to hear about your experiences with it, and where it helps most in your app. This is part of a beta release of Jetpack, so please, try it out, and [let us know what you think](https://issuetracker.google.com/issues/new?component=192731&template=842428)!

# Notes

Performance measurements done on Pixel 2, Android P, locked to 1GHz for measurement stability. App running with debuggable = false, so ART runs with realistic (release) performance.

Running same app on a Nexus 5, Android M, 1.2GHz, the before/after times for the same 80 word inputs are 20.3ms, and 1.2ms, which is a very similar ~94% cost reduction.

The APIs above are available starting in AndroidX `1.0.0-beta01`, (or if you haven’t moved to androidx.* packages, `28.0.0-beta01`).



这很重要的