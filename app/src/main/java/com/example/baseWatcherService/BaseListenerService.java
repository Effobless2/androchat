package com.example.baseWatcherService;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class BaseListenerService extends Service {
    private int inc = 0;

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

    public class ServiceBinder extends Binder{
        public int getInc(){
            return inc;
        }
    }
}
