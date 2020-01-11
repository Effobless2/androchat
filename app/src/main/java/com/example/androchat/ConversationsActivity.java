package com.example.androchat;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

public class ConversationsActivity extends ListActivity {

  public static final String LIFE_CYCLE_CHECKER = "LIFE_CYCLE_CHECKER_2";
  private final static String[] days = {
      "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.conversations_activity);
    ArrayAdapter<String> daysAdapter = new ArrayAdapter<>(
        this,
        android.R.layout.simple_list_item_1,
        days
    );
    setListAdapter(daysAdapter);
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

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    Log.d(LIFE_CYCLE_CHECKER, "onSaveInstanceState");
  }

}
