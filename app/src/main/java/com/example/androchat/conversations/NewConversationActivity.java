package com.example.androchat.conversations;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androchat.R;
import com.example.androchat.adapters.ContactRecyclerAdapter;
import com.example.androchat.adapters.ContactsAdapter;
import com.example.firelib.managers.ConversationManagement;
import com.example.localDB.repositories.userRepositories.UserDataAccessRepository;
import com.example.model.Conversation;
import com.example.model.RelUserConv;
import com.example.model.User;
import com.example.notifications.NotificationsService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class NewConversationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ContactRecyclerAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);
        recyclerView = (RecyclerView) findViewById(R.id.contacts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        findViewById(R.id.createConvBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createConv();

            }
        });
    }

    private void createConv() {
        final Conversation conversation = new Conversation();
        conversation.setName(((EditText)findViewById(R.id.convName)).getText().toString());
        conversation.setLast_message_date(new Date());
        ConversationManagement.create(conversation).continueWith(new Continuation<String, Object>() {
            @Override
            public Object then(@NonNull Task<String> task) throws Exception {
                String convId = task.getResult();
                conversation.setId(convId);
                String current = FirebaseAuth.getInstance().getCurrentUser().getUid();
                ConversationManagement.addUserInConv(convId, current);
                NotificationsService.subscribe(convId);
                for (int i = 0; i < mAdapter.getChecked().length; i++) {
                    boolean checked = mAdapter.getChecked()[i];
                    if (checked) {
                        String googleId =  mAdapter.getUsers().get(i).getGoogleId();
                        ConversationManagement.addUserInConv(convId,googleId);
                        NotificationsService.sendMessage("conversation", convId, googleId, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                            }
                        });
                    }
                }
                Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
                intent.putExtra(Conversation.SERIAL_KEY, conversation);
                startActivity(intent);
                return null;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        new UserDataAccessRepository(this).getAll().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                mAdapter = new ContactRecyclerAdapter(users);
                recyclerView.setAdapter(mAdapter);
            }
        });

    }
}