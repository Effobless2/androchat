package com.example.androchat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.androchat.authentication.SignInActivity;
import com.example.androchat.conversation.ConversationFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity implements MainFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        Toast.makeText(getApplicationContext(), "Signed Out", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                        startActivity(intent);
                    }
                });
    }

    public void contactOnClic(View view) {
        Log.v("CLIC : ","button contact");
    }

    public void conversationOnClic(View view) {
        Log.v("CLIC : ","button conversation");
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragmentMain, new ConversationFragment());
        ft.commit();
    }

    public void searchFirendOnClic(View view) {
        Log.v("CLIC : ","button recherche ami");
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}

