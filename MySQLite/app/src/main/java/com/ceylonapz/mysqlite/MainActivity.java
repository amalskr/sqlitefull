package com.ceylonapz.mysqlite;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ceylonapz.mysqlite.db.DatabaseHandler;
import com.ceylonapz.mysqlite.model.Message;

public class MainActivity extends AppCompatActivity {

    private EditText etTitle;
    private EditText etMessage;
    private EditText etDateTime;
    private DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        db = new DatabaseHandler(this);

        etTitle = (EditText) findViewById(R.id.editTextTitle);
        etMessage = (EditText) findViewById(R.id.editTextMessage);
        etDateTime = (EditText) findViewById(R.id.editTextDateTime);
    }

    public void addNow(View view) {
        String title = etTitle.getText().toString();
        String message = etMessage.getText().toString();
        String dateTime = etDateTime.getText().toString();

        Message newMessage = new Message(title, message, dateTime, 0);
        int lastId = db.addMessage(newMessage);

        if (lastId != -1) {
            Toast.makeText(this, "Message Add", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Message not add", Toast.LENGTH_SHORT).show();
        }
        finish();
    }
}
