package com.example.androchat.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.androchat.MainActivity;
import com.example.androchat.R;
import com.example.androchat.conversations.MessagesActivity;
import com.example.model.Conversation;
import com.example.model.Message;

/**
 * Implementation of App Widget functionality.
 */
public class widget extends AppWidgetProvider {
    private static final String YOUR_AWESOME_ACTION = "GO_TO_CONV";
    public static int appwidgetid = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static Conversation conversation;

    public static Message message;

    public static void updateWidget(
            Context context,
            AppWidgetManager appWidgetManager,
            int appWidgetId,
            Conversation curConversation,
            Message curMessage
    ){
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
        conversation = curConversation;
        message = curMessage;
        rv.setTextViewText(R.id.widgetMessageText, message.getContent());
        rv.setTextViewText(R.id.widgetConvNameText, conversation.getName());
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra(Conversation.SERIAL_KEY, conversation);
        intent.setAction(YOUR_AWESOME_ACTION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,intent,0);
        rv.setOnClickPendingIntent(R.id.btn_widget, pendingIntent);
        appWidgetManager.updateAppWidget(appWidgetId, rv);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId :
                appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId, conversation, message);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

