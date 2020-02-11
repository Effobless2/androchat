package com.example.androchat.conversations;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androchat.MainActivity;
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

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.security.Key;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MessagesActivity extends AppCompatActivity {
    Conversation conversation;
    MessageAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.toolbarColor));
        actionBar.setBackgroundDrawable(colorDrawable);
        conversation = (Conversation) getIntent().getSerializableExtra(Conversation.SERIAL_KEY);
        setTitle(conversation.getName());

        recyclerView = findViewById(R.id.messagesList);
        adapter = new MessageAdapter(getApplicationContext(), this);
        LinearLayoutManager l = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(l);
        recyclerView.setAdapter(adapter);

        new MessageDataAccessRepository(this).getMessagesByConversation(conversation.getId()).observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setMessages(messages);
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
            }
        });

        findViewById(R.id.sendBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        findViewById(R.id.messageText).setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER)
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                return true;
            }
        });

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen){
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }


    private void sendMessage() {
        EditText editMsg = findViewById(R.id.messageText);
        final String messageTxt = editMsg.getText().toString();
        editMsg.setText("");

        final Message message = new Message();
        message.setContent(messageTxt);
        message.setDate(new Date());
        message.setId_user(FirebaseAuth.getInstance().getCurrentUser().getUid());
        message.setId_conv(conversation.getId());
        MessageManagement.create(message).continueWith(new Continuation<String, Object>() {
            @Override
            public Object then(@NonNull Task<String> task) throws Exception {
                NotificationsService.sendMessage(conversation.getId(), task.getResult(), new AsyncHttpResponseHandler() {
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
