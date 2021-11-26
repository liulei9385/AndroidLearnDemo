# Kotlin 语法糖



**kotlin 1.5.31 中 check方法解析** 

```kotlin
@kotlin.internal.InlineOnly
public inline fun check(value: Boolean, lazyMessage: () -> Any): Unit {
    contract {
        returns() implies value
    }
    if (!value) {
        val message = lazyMessage()
        throw IllegalStateException(message.toString())
    }
}
```



ContractBuilder 只能在方法的第一行编写，并且至少有一个Effect. 上图的returns()返回就是一种SimpleEffect。

Contract 的作用告诉编译器一些额外的信息保证后面的代码可以正确的通过编译，好处时减少了一些重复的代码。



```kotlin
ReceiveChannel<T>.receiveAsFlow()
```

返回一个接受来自ReceiveChannel的数据热流[hot flow]，取消它不会影响到ReceiveChannel，它的生命周期会随着ReceiveChannel的close()而终止。

**suspend lambda表达式的妙用**

```kotlin
suspend{
    // 这里的代码会阻塞coroutinue,等这里的代码执行完后才会执行coroutinue里下面的代码(other code).
}
// other code
```



```kotlin
lifecycleScope.launchWhenResumed{
	// channel 用于收发数据
	val selectFileChannel = Channel<String>()
	launch {
		// 接收数据
		selectFileChannel.receiveAsFlow() 
			.collect {  
				submitUserSelectFileList(this, it)
			}
	}
	lifecycle.addObserver(OnDestroyLifeObserver {
	                //关闭channel
	                selectFileChannel.close()
	                uploadHelper?.onDestroy()}
                ) 
}
```



创建Worker线程，worker线程运行task任务,task里包装了我们代码传进去将要运行的任务，会检查当前cpu里由多少个corePoolSize，如果未超出的话就建立新的Worker，保证当前schedule里不超过corePoolSize数量的线程。首先LocalQueue存在数据就从LocalQueue去取任务，如果获取了cpu依次从globalCpuQueue，globalBlockingQueue取任务，反之从localQueue，globalBlockingQueue取任务。<u>还有调度机制确保了阻塞任务和非阻塞任务的公平分配。</u>

**Marsaglia xorshift RNG 随机数算法。**



**关于函数**



1. 可以创建全局的独立函数来重用代码。

2. 不必把独立的函数伪装成类的静态方法。

3. 要求指定函数的参数类型，单表达式可以推断函数的返回类型。

4. <u>默认参数</u>，<u>命名参数</u>，<u>定义可变数量的参数</u>，<u>spread（伸展）运算符</u>和<u>使用解构</u>。

   > spread的作用将数据转变为值列表传递到可变数量的参数中。
   >
   > 解构是基于<u>属性的位置</u>，而不是属性的名称。

5. 所有的函数都是表达式。

6. 必须为函数或者方法的参数指定类型，在参数名后提供参数的类型。





**外部迭代和参数匹配**



命令式编程风格的外部迭代器和函数式编程风格的内部迭代器。

**范围与迭代**

范围类，例如：IntRange,CharRange.ClosedRange<String>

**正向迭代**

**反向迭代**

使用方便的<u>参数匹配语</u>法将删除大量的样板代码。

限制变量的作用域是一个很好的设计。



**集合相关**

kotlin提供了一些针对java集合的视图接口，kotlin分别为两个和三个值的集合提供了Pair和Tripple。Pair，数组，列表，集合和映射，包括它们的可变和不可变版本。

在kotlin中使用集合

- Pair-两个值的元组。
- Tripple-三个值的元组。
- Array-经过索引的、固定大小的对象和基元集合。
- List-有序的对象集合。
- Set-无序的对象集合。
- Map-键和值的关联字典或映射。

Kotlin对集合的该进是： 扩展函数和视图。



**视图**

Kotlin为列表，集合和映射提供两种不同的视图： **只读或不可变视图，以及读写或可变视图。**

不要假设使用只读视图可以提供线程安全性。只读引用是对可变集合的引用，即使你不能修改改集合，也不能保证所引用的集合在另一个线程中没有被修改。



**对象和基元数组**

Array<T>,使用索引运算符，然后调用Array<T>的get方法，放在左侧用的时候，调用的是Array<T>的set()方法。

<u>arrayOf()</u>创建的是一个封箱的整数类型数组，在处理元数据时，会有开销。可以使用<u>intArrayOf()</u>这样的专用函数

没有封箱开销。

数组是可变的，如果希望有灵活大小的有序集合，可以考虑使用**列表**，列表有可变和不可变的两种形式。



**可空类型到字节码的映射**

可空类型可以清除为它的非空对应项，例如，String?变成了String，以及字节码的额外元指令。Kotlin编译器使用这些元指令来执行编译时null检查，而没有运行时性能开销。



**任何能够减少混乱并使代码中的逻辑更易于遵循的努力都是值得的。**



**类型检查和转换**



<u>as 和 as?</u>

**协变和逆变**



泛型：参数类型的变化和约束

渴望重用代码不应该以牺牲类型安全为代价。



**使用Where的参数类型引用**

在方法声明的最后，放置一个where子句并列出所有的约束，用逗号分隔。这种方式目前第一次了解哦

```kotlin
fun <T> userAndClose(input: T)
        where T : AutoCloseable,
              T : Appendable {
    input.append("there")
    input.close()
}
```



**星投影**

kotlin的“声明点型变”并不是与java支持泛型的唯一区别。当你对类型不太了解但又希望类型安全时，请使用星投影。星投影<u>只允许读出，不允许写入</u>。



**具体化的参数类型**

Kotlin使用具体化的类型参数去除了这个麻烦。（java泛型擦除）



数据类 dataClass

sealedClass.[密封类]



数据类、密封类、伴生对象、单例

内联函数在编译时扩展，从而消除了函数调用的开销。



**Kotlin中何时选择单例或者顶级函数？**

