package com.example.androchat.conversations;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.androchat.R;
import com.example.firelib.managers.MessageManagement;
import com.example.localDB.repositories.messageRepositories.MessageDataAccessRepository;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.notifications.NotificationsService;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessagesActivity extends AppCompatActivity {
    Conversation conversation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        conversation = (Conversation) getIntent().getSerializableExtra(Conversation.SERIAL_KEY);
        setTitle(conversation.getName());

        new MessageDataAccessRepository(this).getMessagesByConversation(conversation.getId()).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                ArrayAdapter<Message> adapter = new ArrayAdapter<>(
                        getApplicationContext(),
                        android.R.layout.simple_list_item_1,
                        messages

                );
                ((ListView) findViewById(R.id.messagesList)).setAdapter(adapter);
            }
        });

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }

    private void sendMessage() {
        String messageTxt = ((EditText)findViewById(R.id.messageText)).getText().toString();
        Message message = new Message();
        message.setContent(messageTxt);
        message.setDate(new Date());
        message.setId_user(FirebaseAuth.getInstance().getCurrentUser().getUid());
        message.setId_conv(conversation.getId());
        MessageManagement.create(message);
        NotificationsService.sendMessage(conversation.getName(), messageTxt, conversation.getId(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }


}