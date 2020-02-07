package com.example.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.androchat.MainActivity;
import com.example.androchat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class NotificationsService extends FirebaseMessagingService {

    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "Androchat";

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String serverKey =
            "key=" + "AAAAakZrs2s:APA91bFVjBN2JmjoCWj-LAzu2S5CO7-VHMasuR1PMvGYfW0Uj7zpWEoM5y28YqWn0s4oGPfR7D8lhhKAdbxBkZ9XWdNbb3nx0X19Y_KzTHhyJNvS5mId0PcCSjfa8iKIgW2xOlnfZXPN";
    private static final String contentType = "application/json";


    /*
    * Sending notification
    * @Params :
    *   title = discussion name
    *   message = message
    *  topic = ConversationId
    *  handler = action after sending
    */
    public static void sendMessage(String title, String message, String topic, AsyncHttpResponseHandler handler){
        try {   //Enter your notification message
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", serverKey);
            client.addHeader("Content-Type", contentType);
            JSONObject notifcationBody = new JSONObject();
            notifcationBody.put("body", message);
            notifcationBody.put("title", title);
            RequestParams params = new RequestParams();
            params.setUseJsonStreamer(true);
            params.put("data", notifcationBody);
            params.put("to", "/topics/" + topic);
            client.post(FCM_API, params, handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Message Reception
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.i("MESSAGE_RECEIVED", remoteMessage.getData().toString());
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("body");
            this.sendVisualNotification(title, message);
        }
    }

    /*
    * Subscribing to a Conversation
    * @Params :
    *   topic = ConversationId
    */
    public static void subscribe(String topic){
        FirebaseMessaging.getInstance().subscribeToTopic(topic)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Success";
                        if (!task.isSuccessful()) {
                            msg = "Error";
                        }
                        Log.d("SUBSCRIPTION", msg);
                    }
                });
    }


    //Notification Visual
    private void sendVisualNotification(String title, String messageBody) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message provenant de Firebase";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(title + " : " + messageBody)
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 7 - Show notification
        notificationManager.notify(new Random().nextInt(9999 - 1000) + 1000 + "", NOTIFICATION_ID, notificationBuilder.build());
    }
}
