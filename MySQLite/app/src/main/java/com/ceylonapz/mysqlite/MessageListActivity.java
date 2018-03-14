package com.ceylonapz.mysqlite;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ceylonapz.mysqlite.adapter.MessageListAdapter;
import com.ceylonapz.mysqlite.db.DatabaseHandler;
import com.ceylonapz.mysqlite.model.Message;

import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    private ProgressBar pBar;
    private TextView noDataTv;
    private RecyclerView messageRecycView;
    private List<Message> messageList;
    private MessageListAdapter mAdapter = null;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);


        inits();
        setupView();
    }

    private void inits() {
        pBar = (ProgressBar) findViewById(R.id.progressBar);
        messageRecycView = (RecyclerView) findViewById(R.id.notificationRecyclerView);
        noDataTv = (TextView) findViewById(R.id.nodataTv);
    }

    private void setupView() {
        messageRecycView.setHasFixedSize(true);
        messageRecycView.setClipToPadding(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        messageRecycView.setLayoutManager(layoutManager);

        new LoadMessagesTask().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mAdapter != null) {
            messageList = db.getAllMessages();
            refreshDatabase();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void addMessage(View view) {
        Intent nextIntent = new Intent(this, MainActivity.class);
        startActivity(nextIntent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    // load message
    private class LoadMessagesTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                db = new DatabaseHandler(MessageListActivity.this);
                messageList = db.getAllMessages();

                return "Success";

            } catch (Exception e) {
                return "Error";
            }
        }

        @Override
        protected void onPostExecute(String mapResults) {

            pBar.setVisibility(View.GONE);

            if (mapResults.equals("Success")) {
                if (messageList.size() > 0) {
                    noDataTv.setVisibility(View.GONE);
                    messageRecycView.setVisibility(View.VISIBLE);

                    refreshDatabase();

                } else {
                    messageRecycView.setVisibility(View.GONE);
                    noDataTv.setVisibility(View.VISIBLE);

                    //SET TITLE AND SUB TITLE
                    noDataTv.setText("No messages");
                }
            } else {
                messageRecycView.setVisibility(View.GONE);
                noDataTv.setVisibility(View.VISIBLE);
                noDataTv.setText(mapResults);
            }
        }
    }

    private void refreshDatabase() {
        mAdapter = new MessageListAdapter(MessageListActivity.this, messageList, noDataTv);
        messageRecycView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
