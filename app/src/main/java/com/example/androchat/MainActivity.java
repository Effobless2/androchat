package com.example.androchat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    public static final String LIFE_CYCLE_CHECKER = "LIFE_CYCLE_CHECKER";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchConversationsActivity(v);
            }
        });
        Log.d(LIFE_CYCLE_CHECKER, "onCreate");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LIFE_CYCLE_CHECKER, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LIFE_CYCLE_CHECKER, "onResume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(LIFE_CYCLE_CHECKER, "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LIFE_CYCLE_CHECKER, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LIFE_CYCLE_CHECKER, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LIFE_CYCLE_CHECKER, "onDestroy");
    }

    public void launchConversationsActivity(View view) {
        Intent intent = new Intent(this, ConversationsActivity.class);
        startActivity(intent);
    }
}
