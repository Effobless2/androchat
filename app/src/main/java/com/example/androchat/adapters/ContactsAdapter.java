package com.example.androchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androchat.R;
import com.example.model.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactsAdapter extends ArrayAdapter<User> {

    public ContactsAdapter(@NonNull Context context, @NonNull List<User> objects){
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View userRow = layoutInflater.inflate(R.layout.contact_row, null);
        User user = getItem(position);

        TextView emailTextView = (TextView)userRow.findViewById(R.id.email);

        emailTextView.setText(user.getEmail());

        ImageView imageView = userRow.findViewById(R.id.avatar);
        String avatar = user.getAvatar();
        if (avatar != null) {
            Picasso.get().load(avatar).into(imageView);
        } else {
            Picasso.get().load("https://i1.pngguru.com/preview/792/29/287/button-ui-2-google-android-robot-icon-png-clipart.jpg").into(imageView);
        }

        return userRow;
    }
}
