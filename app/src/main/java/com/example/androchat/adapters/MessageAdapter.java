package com.example.androchat.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androchat.R;
import com.example.firelib.managers.UserManagement;
import com.example.localDB.repositories.userRepositories.UserDataAccessRepository;
import com.example.model.Message;
import com.example.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    List<Message> messages;
    private LayoutInflater layoutInflater;
    LifecycleOwner activity;

    public MessageAdapter(Context context, LifecycleOwner lifecycleOwner) {
        layoutInflater = LayoutInflater.from(context);
        activity = lifecycleOwner;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = layoutInflater.inflate(R.layout.message_list_item, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (messages != null) {
            Message message = messages.get(position);
            holder.bind(message);
        }else{
        }
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final View view;
        private Message message;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.view = view;
        }

        public void bind(Message message){
            this.message = message;
            displayDate();
            displayContent();
            definePosition();
            displayUserDatas();
        }

        private void displayDate(){
            String date;
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            String today = formatter.format(new Date());
            String convDate = formatter.format(message.getDate());
            if (today.compareTo(convDate) == 0){
                date = new SimpleDateFormat("hh:mm").format(message.getDate());
            }
            else {
                date = convDate;
            }
            ((TextView)view.findViewById(R.id.dateTxt)).setText(date);
        }

        private void displayContent(){
            ((TextView)view.findViewById(R.id.messageTxt)).setText(message.getContent());
        }

        private void definePosition(){
            if(message.getId_user().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                ((LinearLayout)view).setGravity(Gravity.RIGHT);
                ((LinearLayout)view.findViewById(R.id.userPanel)).setGravity(Gravity.RIGHT);
                ((TextView)view.findViewById(R.id.messageTxt)).setBackground(layoutInflater.getContext().getResources().getDrawable(R.drawable.outcoming_message_shape));
            }
            else {
                ((LinearLayout)view).setGravity(Gravity.LEFT);
                ((LinearLayout)view.findViewById(R.id.userPanel)).setGravity(Gravity.LEFT);
                ((TextView)view.findViewById(R.id.messageTxt)).setBackground(layoutInflater.getContext().getResources().getDrawable(R.drawable.incoming_message_shape));
            }
        }

        private void displayUserDatas(){
            new UserDataAccessRepository(layoutInflater.getContext()).getUserByGoogleId(message.getId_user()).observe(activity, new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                    if (users.size() > 0){
                        setUserDatas(users.get(0));
                    }
                    else{
                        UserManagement.getUserByGoogleId(message.getId_user()).continueWith(new Continuation<List<User>, Object>() {
                            @Override
                            public Object then(@NonNull Task<List<User>> task) throws Exception {
                                List<User> users = task.getResult();
                                if(users.size() > 0)
                                    setUserDatas(users.get(0));
                                return null;
                            }
                        });
                    }
                }
            });
        }
        private void setUserDatas(User user){
            ((TextView)view.findViewById(R.id.emailTxt)).setText(user.getEmail());
            String avatar = user.getAvatar();

            ImageView imageView = view.findViewById(R.id.userAvatar);
            if (avatar != null) {
                Picasso.get().load(avatar).into(imageView);
            } else {
                Picasso.get().load("https://i1.pngguru.com/preview/792/29/287/button-ui-2-google-android-robot-icon-png-clipart.jpg").into(imageView);
            }
        }
    }
}
