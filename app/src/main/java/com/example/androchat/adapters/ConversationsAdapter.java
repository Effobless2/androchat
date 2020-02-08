package com.example.androchat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androchat.R;
import com.example.androchat.conversations.MessagesActivity;
import com.example.model.Conversation;
import com.example.model.User;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ConversationsAdapter extends ArrayAdapter<Conversation> {

    public ConversationsAdapter(@NonNull Context context, @NonNull List<Conversation> objects){
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Conversation conversation = getItem(position);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View convRow = layoutInflater.inflate(R.layout.conversation_list_item, null);
        ((TextView)convRow.findViewById(R.id.convName)).setText(conversation.getName());

        String date;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        String today = formatter.format(new Date());
        String convDate = formatter.format(conversation.getLast_message_date());
        if (today.compareTo(convDate) == 0){
            date = new SimpleDateFormat("hh:mm").format(conversation.getLast_message_date());
        }
        else {
            date = convDate;
        }

        ((TextView)convRow.findViewById(R.id.convDate)).setText(date);

        convRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MessagesActivity.class);
                intent.putExtra(Conversation.SERIAL_KEY, getItem(position));
                getContext().startActivity(intent);
            }
        });
        return convRow;
    }
}
