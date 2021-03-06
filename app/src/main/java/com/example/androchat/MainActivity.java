package com.example.androchat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androchat.authentication.SignInActivity;
import com.example.androchat.conversations.ConversationsList;
import com.example.androchat.friendRequest.SearchUserFragment;
import com.example.androchat.localFriends.ContactsFragment;
import com.example.baseWatcherService.BaseListenerService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements
        SearchUserFragment.OnFragmentInteractionListener,
        ConversationsList.OnFragmentInteractionListener,
        ContactsFragment.OnFragmentInteractionListener {

    FragmentManager fm;
    private boolean isBound = false;
    private BaseListenerService.ServiceBinder binder;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isBound = true;
            binder = (BaseListenerService.ServiceBinder) service;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            binder.setGoogleId(user.getUid());
            binder.startListening();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            binder = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = this.getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.toolbarColor));
        actionBar.setBackgroundDrawable(colorDrawable);

        this.setTitle(this.getResources().getString(R.string.hello) + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        fm = getSupportFragmentManager();
        conversationOnClic(null);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (null == binder) {
            Intent intent = new Intent(this, BaseListenerService.class);
            try{
                startService(intent);
            } catch (Exception e){

            }
        }
        bind();
    }

    private void bind(){
        if(!isBound){
            Intent intent = new Intent(getApplicationContext(), BaseListenerService.class);
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    private void unbind(){
        if(isBound){
            isBound = false;
            binder = null;
            unbindService(serviceConnection);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbind();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void logoutOnClic(View view) {
        Log.v("CLIC : ","button deconnexion");
        AuthUI.getInstance()
                .signOut(getApplicationContext())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                });
        if (null != binder){
            binder.removeAll();
            unbind();
        }
    }

    public void contactOnClic(View view) {
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMain, new ContactsFragment());
        ft.commit();
    }

    public void conversationOnClic(View view) {
        Log.v("CLIC : ","button conversation");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMain, new ConversationsList());
        ft.commit();
    }

    public void searchFriendOnClic(View view) {
        Log.v("CLIC : ","button recherche ami");
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMain, new SearchUserFragment());
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

