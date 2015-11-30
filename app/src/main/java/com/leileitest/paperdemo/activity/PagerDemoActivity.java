package com.leileitest.paperdemo.activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.adapter.IBaseRecyclerAdapter;
import com.leileitest.paperdemo.adapter.ListDividerItemDecoration;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.paperdb.Book;
import io.paperdb.Paper;

public class PagerDemoActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private SimpleItemAdapter itemAdapter;
    private EditText editText;

    public static final String DB_KEY = "edit_input_list";

    @Override
    public void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        editText = (EditText) findViewById(R.id.edit);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pager_demo;
    }

    @Override
    public void obtainData() {
        super.obtainData();
        initData();
    }

    private void initData() {
        Book book = Paper.book();
        boolean existed = book.exist(DB_KEY);
        if (existed) {
            List<String> saveString = book.read(DB_KEY);
            if (!itemAdapter.getDataList().containsAll(saveString)) {
                itemAdapter.addItemAll(saveString);
                itemAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void configUi() {
        super.configUi();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        ColorDrawable dividerDrawable = new ColorDrawable(0x90cdcdcd);
        mRecyclerView.addItemDecoration(new ListDividerItemDecoration(dividerDrawable));

        itemAdapter = new SimpleItemAdapter(this, null);
        mRecyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        mRecyclerView.setAdapter(itemAdapter);
        itemAdapter.setOnItemClickListener(new IBaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View convertView, int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(convertView.getContext());
                String message = itemAdapter.getItemAtPosition(position);
                AlertDialog alertDialog = builder.setMessage(message)
                        .setCancelable(true)
                        .create();
                alertDialog.setCanceledOnTouchOutside(true);
                alertDialog.show();
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String text = editText.getText().toString();
                    if (!text.isEmpty()) {
                        itemAdapter.addItem(text);
                        itemAdapter.notifyDataSetChanged();
                        editText.setText("");
                    }
                    return true;
                }
                return false;
            }
        });

        File file = this.getFilesDir();
        if (file != null) {
            Toast.makeText(getApplicationContext(), file.getAbsolutePath(), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private ExecutorService executorService;

    /**
     * Disable new tasks from being submitted
     *
     * @param pool
     */
    void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool == null || pool.isShutdown())
            return;

        pool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private void saveWordToDb() {
        if (executorService == null)
            executorService = Executors.newFixedThreadPool(3);
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                List<String> listString = itemAdapter.getDataList();
                if (!listString.isEmpty()) {
                    Book book = Paper.book();
                    boolean existed = book.exist(DB_KEY);
                    if (!existed)
                        book.write(DB_KEY, listString);
                    else {
                        List<String> dbListString = book.read(DB_KEY);
                        boolean shouldSaved = false;
                        for (String str : listString) {
                            if (!dbListString.contains(str)) {
                                dbListString.add(str);
                                shouldSaved = true;
                            }
                        }
                        if (shouldSaved) {
                            book.write(DB_KEY, dbListString);
                        }
                    }
                }
            }
        });
    }

    public class SimpleItemAdapter extends IBaseRecyclerAdapter<SimpleItemAdapter.ViewHolder> {

        public SimpleItemAdapter(Context context, List<String> ts) {
            super(context, ts);
        }

        @Override
        protected ViewHolder onCreateViewHolder(ViewGroup parent,
                                                LayoutInflater layoutInflater,
                                                int viewType) {
            View itemView = layoutInflater.inflate(R.layout.pager_adapter_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            super.onBindViewHolder(holder, position);
            String string = getItemAtPosition(position);
            holder.mTextView.setText(string);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView mTextView;

            public ViewHolder(View itemView) {
                super(itemView);
                mTextView = (TextView) itemView.findViewById(R.id.text);
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        saveWordToDb();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shutdownAndAwaitTermination(executorService);
    }
}