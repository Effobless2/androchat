package com.example.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.androchat.MainActivity;
import com.example.androchat.R;
import com.example.androchat.conversations.MessagesActivity;
import com.example.androchat.widget.AndrochatWidget;
import com.example.firelib.managers.ConversationManagement;
import com.example.firelib.managers.MessageManagement;
import com.example.firelib.managers.UserManagement;
import com.example.model.Conversation;
import com.example.model.Message;
import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
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

import java.util.List;
import java.util.Random;

import cz.msebera.android.httpclient.Header;

public class NotificationsService extends FirebaseMessagingService {

    public static final String FRIEND_REQUEST = "FRIEND_REQUEST";
    public static final String CONVERSATION = "CONVERSATION";


    private final int NOTIFICATION_ID = 007;
    private final String NOTIFICATION_TAG = "Androchat";

    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String serverKey =
            "key=" + "AAAAakZrs2s:APA91bFVjBN2JmjoCWj-LAzu2S5CO7-VHMasuR1PMvGYfW0Uj7zpWEoM5y28YqWn0s4oGPfR7D8lhhKAdbxBkZ9XWdNbb3nx0X19Y_KzTHhyJNvS5mId0PcCSjfa8iKIgW2xOlnfZXPN";
    private static final String contentType = "application/json";


    /*
    * Sending notification
    * @Params :
    *   title = conversation Id or user Id
    *   message = message
    *  topic = ConversationId
    *  handler = action after sending
    */
    public static void sendMessage(String topic, String messageId, AsyncHttpResponseHandler handler){
        try {   //Enter your notification message
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", serverKey);
            client.addHeader("Content-Type", contentType);
            JSONObject notifcationBody = new JSONObject();
            notifcationBody.put("body", messageId);
            notifcationBody.put("title", topic);
            RequestParams params = new RequestParams();
            params.setUseJsonStreamer(true);
            params.put("data", notifcationBody);
            params.put("to", "/topics/" + topic);
            client.post(FCM_API, params, handler);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendNewConversation(Conversation conversation, String googleId){
        try {   //Enter your notification message
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", serverKey);
            client.addHeader("Content-Type", contentType);
            JSONObject notifcationBody = new JSONObject();
            notifcationBody.put("body", conversation.getId());
            notifcationBody.put("title", CONVERSATION);
            RequestParams params = new RequestParams();
            params.setUseJsonStreamer(true);
            params.put("data", notifcationBody);
            params.put("to", "/topics/" + googleId);
            client.post(FCM_API, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void sendRequest(String to, String from) {
        try {   //Enter your notification message
            AsyncHttpClient client = new AsyncHttpClient();
            client.addHeader("Authorization", serverKey);
            client.addHeader("Content-Type", contentType);
            JSONObject notifcationBody = new JSONObject();
            notifcationBody.put("body", from);
            notifcationBody.put("title", FRIEND_REQUEST);
            RequestParams params = new RequestParams();
            params.setUseJsonStreamer(true);
            params.put("data", notifcationBody);
            params.put("to", "/topics/" + to);
            client.post(FCM_API, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
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
            switch (title){
                case FRIEND_REQUEST:
                    String from = remoteMessage.getData().get("body");
                    newFriendRequest(from);
                    break;
                case CONVERSATION:
                    String conversationId = remoteMessage.getData().get("body");
                    newConversation(conversationId);
                    break;
                default:
                    String messageId = remoteMessage.getData().get("body");
                    newMessageReceived(title, messageId);
                    break;
            }
        }
    }

    private void newConversation(String conversationId) {
        subscribe(conversationId);
        ConversationManagement.getConversationById(conversationId).continueWith(new Continuation<Conversation, Object>() {
            @Override
            public Object then(@NonNull Task<Conversation> task) throws Exception {
                createNewConversationNotification(task.getResult());
                return null;
            }
        });
    }

    private void newFriendRequest(String from) {
        UserManagement.getUserByGoogleId(from)
                .continueWith(new Continuation<List<User>, Object>() {
                    @Override
                    public Object then(@NonNull Task<List<User>> task) throws Exception {
                        List<User> users = task.getResult();
                        if(users.size() > 0){
                            createNewFriendRequestNotification(users.get(0));
                        }
                        return null;
                    }
                });
    }

    private void newMessageReceived(String conversationId, final String messageId) {
        ConversationManagement.getConversationById(conversationId).continueWith(new Continuation<Conversation, Object>() {
            @Override
            public Object then(@NonNull Task<Conversation> task) throws Exception {
                final Conversation conversation = task.getResult();
                MessageManagement.getMessageById(messageId).continueWith(new Continuation<Message, Object>() {
                    @Override
                    public Object then(@NonNull Task<Message> task) throws Exception {
                        Message message = task.getResult();
                        AndrochatWidget.updateWidget(
                                getApplicationContext(),
                                AppWidgetManager.getInstance(getApplicationContext()),
                                AndrochatWidget.appwidgetid,
                                conversation,
                                message
                        );
                        AndrochatWidget.conversation = conversation;
                        AndrochatWidget.message = message;
                        int[] ids = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), AndrochatWidget.class));
                        AndrochatWidget myAndrochatWidget = new AndrochatWidget();
                        myAndrochatWidget.onUpdate(getApplicationContext(), AppWidgetManager.getInstance(getApplicationContext()),ids);
                        createMessageNotification(conversation, message);
                        return null;
                    }
                });
                return null;
            }
        });
    }

    private void createMessageNotification(Conversation conversation, Message message){
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(Conversation.SERIAL_KEY, conversation);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(conversation.getName());

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
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(conversation.getName())
                        .setContentText(message.getContent())
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 7 - Show notification
        notificationManager.notify(new Random().nextInt(9999 - 1000) + 1000 + "", NOTIFICATION_ID, notificationBuilder.build());
    }

    private void createNewConversationNotification(Conversation conversation){
        Intent intent = new Intent(this, MessagesActivity.class);
        intent.putExtra(Conversation.SERIAL_KEY, conversation);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(conversation.getName());

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
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(conversation.getName())
                        .setContentText(this.getResources().getString(R.string.invitation))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 7 - Show notification
        notificationManager.notify(new Random().nextInt(9999 - 1000) + 1000 + "", NOTIFICATION_ID, notificationBuilder.build());

    }

    private void createNewFriendRequestNotification(User user){Intent intent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(user.getEmail());

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
                        .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                        .setContentTitle(user.getEmail())
                        .setContentText(this.getResources().getString(R.string.friend_request))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 7 - Show notification
        notificationManager.notify(new Random().nextInt(9999 - 1000) + 1000 + "", NOTIFICATION_ID, notificationBuilder.build());
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
}
