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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

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

public class MessageAdapter extends ArrayAdapter<Message> {
    LifecycleOwner activity;

    public MessageAdapter(LifecycleOwner activity, @NonNull Context context, @NonNull List<Message> objects){
        super(context, 0, objects);
        this.activity = activity;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Message message = getItem(position);

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        final View messageRow = layoutInflater.inflate(R.layout.message_list_item, null);

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
        ((TextView)messageRow.findViewById(R.id.dateTxt)).setText(date);

        ((TextView)messageRow.findViewById(R.id.messageTxt)).setText(message.getContent());

        if(message.getId_user().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            ((LinearLayout)messageRow.findViewById(R.id.list_message_main)).setGravity(Gravity.RIGHT);
            ((LinearLayout)messageRow.findViewById(R.id.userPanel)).setGravity(Gravity.RIGHT);
        }

        new UserDataAccessRepository(getContext()).getUserByGoogleId(message.getId_user()).observe(activity, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if (users.size() > 0){
                    setUserDatas(users.get(0), messageRow);
                }
                else{
                    UserManagement.getUserByGoogleId(message.getId_user()).continueWith(new Continuation<List<User>, Object>() {
                        @Override
                        public Object then(@NonNull Task<List<User>> task) throws Exception {
                            List<User> users = task.getResult();
                            if(users.size() > 0)
                                setUserDatas(users.get(0), messageRow);
                            return null;
                        }
                    });
                }
            }
        });

        return messageRow;
    }

    private void setUserDatas(User user, View messageRow){
        ((TextView)messageRow.findViewById(R.id.emailTxt)).setText(user.getEmail());
        String avatar = user.getAvatar();

        ImageView imageView = messageRow.findViewById(R.id.userAvatar);
        if (avatar != null) {
            Picasso.get().load(avatar).into(imageView);
        } else {
            Picasso.get().load("https://i1.pngguru.com/preview/792/29/287/button-ui-2-google-android-robot-icon-png-clipart.jpg").into(imageView);
        }
    }
}
