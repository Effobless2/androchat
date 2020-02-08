package com.example.androchat.conversations;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.example.androchat.R;
import com.example.androchat.adapters.MessageAdapter;
import com.example.firelib.managers.MessageManagement;
import com.example.localDB.repositories.messageRepositories.MessageDataAccessRepository;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.notifications.NotificationsService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessagesActivity extends AppCompatActivity {
    Conversation conversation;
    public static LifecycleOwner lifecycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        conversation = (Conversation) getIntent().getSerializableExtra(Conversation.SERIAL_KEY);
        setTitle(conversation.getName());
        lifecycle = this;

        new MessageDataAccessRepository(this).getMessagesByConversation(conversation.getId()).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                MessageAdapter adapter = new MessageAdapter(
                        lifecycle,
                        getApplicationContext(),
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
        final String messageTxt = ((EditText)findViewById(R.id.messageText)).getText().toString();
        final Message message = new Message();
        message.setContent(messageTxt);
        message.setDate(new Date());
        message.setId_user(FirebaseAuth.getInstance().getCurrentUser().getUid());
        message.setId_conv(conversation.getId());
        MessageManagement.create(message).continueWith(new Continuation<String, Object>() {
            @Override
            public Object then(@NonNull Task<String> task) throws Exception {
                NotificationsService.sendMessage(conversation.getId(), conversation.getId(), task.getResult(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                return null;
            }
        });

    }


}
