package com.example.androchat.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androchat.R;
import com.example.model.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.ViewHolder> {
    List<User> users;
    boolean[] checked;

    public ContactRecyclerAdapter(List<User> users) {
        this.users = users;
        checked = new boolean[users.size()];
    }

    public boolean[] getChecked() {
        return checked;
    }

    public List<User> getUsers() {
        return users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row_new_conv, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ((TextView)holder.getView().findViewById(R.id.email)).setText(users.get(position).getEmail());
        ImageView imageView = holder.getView().findViewById(R.id.avatar);
        String avatar = users.get(position).getAvatar();
        if (avatar != null) {
            Picasso.get().load(avatar).into(imageView);
        } else {
            Picasso.get().load("https://i1.pngguru.com/preview/792/29/287/button-ui-2-google-android-robot-icon-png-clipart.jpg").into(imageView);
        }

        holder.getView().findViewById(R.id.checkBox).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked[position] = ((CheckBox) v).isChecked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public View getView() {
            return view;
        }
    }
}
