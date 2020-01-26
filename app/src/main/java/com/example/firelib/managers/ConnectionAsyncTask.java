package com.example.firelib.managers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.firelib.DAL.UserDAL;
import com.example.model.User;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.annotation.Documented;

class ConnectionAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final String LOG_CONNECTION_ASYNC = "LOG_CONNECTION_ASYNC";

    private static final long INITIAL_TIMEOUT = 30_000;
    private final User user;

    TaskCompletionSource<String> result;

    public Task<String> getTask(){
        return result.getTask();
    }

    public ConnectionAsyncTask(User user){
        result = new TaskCompletionSource<>();
        this.user = user;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Log.i(LOG_CONNECTION_ASYNC, "first while begin");
        Task<QuerySnapshot> googleIdVerif = UserDAL.getUserByGoogleId(user.getGoogleId()).get();
        try{
            long timeout = INITIAL_TIMEOUT;
            long currentH = 0;
            currentH = System.currentTimeMillis();
            Log.i(LOG_CONNECTION_ASYNC, "first while begin");
            while(!googleIdVerif.isComplete() && timeout >= 0){
                timeout -= (System.currentTimeMillis() - currentH);
                currentH = System.currentTimeMillis();
            }
            Log.i(LOG_CONNECTION_ASYNC, "first while end");

            if(!googleIdVerif.isComplete()){
                Log.i(LOG_CONNECTION_ASYNC, "first while not complete");
                return null;
            }
            QuerySnapshot snapshots = googleIdVerif.getResult();
            if(snapshots.getDocuments().size() != 0){
                Log.i(LOG_CONNECTION_ASYNC, "first while not null");
                result.setResult(snapshots.getDocuments().get(0).getId());
                return null;
            }
            else{
                Task<DocumentReference> googleCreate = UserDAL.register(user);
                timeout = INITIAL_TIMEOUT;
                currentH = 0;
                currentH = System.currentTimeMillis();
                while(!googleCreate.isComplete() && timeout >= 0){
                    timeout -= (System.currentTimeMillis() - currentH);
                    currentH = System.currentTimeMillis();
                }

                if(!googleCreate.isComplete()) {
                    return null;
                }
                else{
                    result.setResult(googleCreate.getResult().getId());
                }
            }
        } catch (Exception e){
            return null;
        }
        return null;
    }
}

