package com.example.androchat.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.androchat.R;
import com.example.firelib.managers.FriendShipManagement;
import com.example.model.FriendRequest;
import com.example.model.User;
import com.example.notifications.NotificationsService;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserAdapter extends ArrayAdapter<User> {

    List<String> contactIds = new ArrayList<>();
    List<String> receivedIds = new ArrayList<>();
    HashMap<String, FriendRequest> friendRequests = new HashMap<>();
    List<String> sentIds = new ArrayList<>();

    public UserAdapter(@NonNull Context context, @NonNull List<User> objects){
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        final View userRow = layoutInflater.inflate(R.layout.user_row, null);
        final User user = getItem(position);

        TextView emailTextView = (TextView)userRow.findViewById(R.id.email);

        emailTextView.setText(user.getEmail());

        ImageView imageView = userRow.findViewById(R.id.avatar);
        String avatar = user.getAvatar();
        if (avatar != null) {
            Picasso.get().load(avatar).into(imageView);
        } else {
            Picasso.get().load("https://i1.pngguru.com/preview/792/29/287/button-ui-2-google-android-robot-icon-png-clipart.jpg").into(imageView);
        }

        if (user.getGoogleId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            inContact(userRow);
        } else{

            //I don't like async Tasks in Java ....
            FriendShipManagement.getAllContactOfUser(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .continueWith(new Continuation<List<String>, Object>() {
                    @Override
                    public Object then(@NonNull Task<List<String>> task) throws Exception {
                        contactIds = task.getResult();
                        for (String contactId : contactIds) {
                            if(contactId.equals(user.getGoogleId())){
                                inContact(userRow);
                                return null;
                            }
                        }
                        FriendShipManagement.getFriendRequestFrom(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .continueWith(new Continuation<List<FriendRequest>, Object>() {
                                    @Override
                                    public Object then(@NonNull Task<List<FriendRequest>> task) throws Exception {
                                        boolean found = false;
                                        for (FriendRequest friendRequest : task.getResult()) {
                                            sentIds.add(friendRequest.getTo());
                                            friendRequests.put(friendRequest.getTo(), friendRequest);
                                            if(friendRequest.getTo().equals(user.getGoogleId())){
                                                found = true;
                                                inRequestSent(userRow, user);
                                            }
                                        }
                                        if(!found){
                                            FriendShipManagement.getFriendRequestTo(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .continueWith(new Continuation<List<FriendRequest>, Object>() {
                                                        @Override
                                                        public Object then(@NonNull Task<List<FriendRequest>> task) throws Exception {
                                                            boolean found = false;
                                                            for (FriendRequest friendRequest : task.getResult()) {
                                                                receivedIds.add(friendRequest.getTo());
                                                                friendRequests.put(friendRequest.getFrom(), friendRequest);
                                                                if(friendRequest.getFrom().equals(user.getGoogleId())){
                                                                    found = true;
                                                                    inRequestReceived(userRow, user);
                                                                }
                                                            }
                                                            if(!found){
                                                                inNothing(userRow, user);
                                                            }
                                                            return null;
                                                        }
                                                    });
                                        }
                                        return null;
                                    }
                                });
                        return null;
                    }
                });
        }
        return userRow;
    }

    //Removes buttons for adding it in contact list
    private void inContact(View userRow){
        View ly = userRow.findViewById(R.id.userRowBtnLayout);
        ((ViewGroup)ly.getParent()).removeView(ly);
    }

    //Removes send button and display annulation Button for Friend Request
    private void inRequestSent(final View userRow, final User user){
        View btn = userRow.findViewById(R.id.sendRequestBtn);
        ((ViewGroup)btn.getParent()).removeView(btn);
        Button annulBtn = (Button) userRow.findViewById(R.id.annulationRequest);
        annulBtn.setText("Annul");
        annulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(friendRequests.get(user.getGoogleId()));
                Toast.makeText(getContext(), "Request Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Set Buttons for accepting friend request and Rejet it
    private void inRequestReceived(final View userRow, final User user){
        Button annulBtn = (Button) userRow.findViewById(R.id.annulationRequest);
        annulBtn.setText("Reject");

        Button acceptBtn = userRow.findViewById(R.id.sendRequestBtn);
        acceptBtn.setText("Accept");
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateRequest(friendRequests.get(user.getGoogleId()));
                Toast.makeText(getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
            }
        });
        annulBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest(friendRequests.get(user.getGoogleId()));
                Toast.makeText(getContext(), "Request Rejected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Display button for sending Friend Request
    private void inNothing(final View userRow, final User user){
        View btn = userRow.findViewById(R.id.annulationRequest);
        ((ViewGroup)btn.getParent()).removeView(btn);
        Button acceptBtn = userRow.findViewById(R.id.sendRequestBtn);
        acceptBtn.setText("Send");
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FriendRequest friendRequest = new FriendRequest();
                friendRequest.setFrom(FirebaseAuth.getInstance().getCurrentUser().getUid());
                friendRequest.setTo(user.getGoogleId());

                FriendShipManagement.sendFriendRequest(friendRequest);
                NotificationsService.sendRequest(friendRequest.getTo(), friendRequest.getFrom());
                Toast.makeText(getContext(), "Request Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void validateRequest(FriendRequest friendRequest) {
        FriendShipManagement.validateFriendRequest(friendRequest);
    }

    private void rejectRequest(FriendRequest friendRequest){
        FriendShipManagement.rejectRequest(friendRequest.getDocumentId());
    }
}
