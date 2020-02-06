package com.example.androchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.androchat.authentication.SignInActivity;
import com.example.baseWatcherService.BaseListenerService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

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
        Log.v("CLIC : ","button contact");
    }

    public void conversationOnClic(View view) {
        Log.v("CLIC : ","button conversation");
    }

    public void searchFirendOnClic(View view) {
        Log.v("CLIC : ","button recherche ami");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

