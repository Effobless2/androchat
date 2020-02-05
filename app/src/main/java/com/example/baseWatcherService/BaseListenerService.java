package com.example.baseWatcherService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.firelib.DAL.RelUserConvDAL;
import com.example.localDB.repositories.relUsersConversationsRepositories.RelUsersConversationsRepository;

public class BaseListenerService extends Service {
    private String googleId;
    private Context context;

    public BaseListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SERVICE_LOG", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new ServiceBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }

    public void onStartListening(){
        if(context == null)
            Log.i("SERVICE_LOG", "context is null");
        else{
            Log.i("SERVICE_LOG", "started");
            new RelUsersConversationsRepository(context, RelUserConvDAL.getAllRelationForUserByGoogleId(googleId));
        }
    }

    public class ServiceBinder extends Binder{
        public String getGoogleId(){
            return googleId;
        }

        public void setGoogleId(String newGoogleId){
            googleId = newGoogleId;
        }

        public void startListening(){
            Log.i("SERVICE_LOG", "start");
            onStartListening();
        }
    }
}
