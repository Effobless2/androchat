package com.example.baseWatcherService;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.firelib.DAL.RelUserConvDAL;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.repositories.relUsersConversationsRepositories.RelUsersConversationsRepository;
import com.example.localDB.repositories.relUsersRepositories.RelUsersRepository;
import com.example.model.RelContacts;

import static com.example.firelib.DAL.RelContactsDAL.getAllContactsOfCurrentGoogleUser;

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
            new RelUsersRepository(context, getAllContactsOfCurrentGoogleUser(googleId));
        }
    }

    public void cleanDatabase(){
        if(context != null){
            Runnable thread = new Runnable() {
                @Override
                public void run() {
                    DBConnect.getInstance(context).userDataUpdatesDAO().removeAll();
                    DBConnect.getInstance(context).conversationDataUpdatesDAO().removeAll();
                    DBConnect.getInstance(context).messageDataUpdatesDAO().removeAll();
                    Log.i("SERVICE_LOG", "removed");
                }
            };
            new Thread(thread).start();

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

        public void removeAll() {
            cleanDatabase();
        }
    }
}
