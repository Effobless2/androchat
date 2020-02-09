package com.example.firelib.managers;

import androidx.annotation.NonNull;

import com.example.firelib.DAL.FriendsRequestsDAL;
import com.example.firelib.DAL.RelContactsDAL;
import com.example.model.FriendRequest;
import com.example.model.RelContacts;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FriendShipManagement {
    public static Task<List<FriendRequest>> getFriendRequestFrom(String googleId){
        return FriendsRequestsDAL.getRequestFrom(googleId).get()
                .continueWith(new Continuation<QuerySnapshot, List<FriendRequest>>() {
                    @Override
                    public List<FriendRequest> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<FriendRequest> result = new ArrayList<>();
                        QuerySnapshot snapshots = task.getResult();
                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            FriendRequest request = document.toObject(FriendRequest.class);
                            request.setDocumentId(document.getId());
                            result.add(request);
                        }
                        return result;
                    }
                });
    }

    public static Task<List<FriendRequest>> getFriendRequestTo(String googleId){
        return FriendsRequestsDAL.getRequestAssignedTo(googleId).get()
                .continueWith(new Continuation<QuerySnapshot, List<FriendRequest>>() {
                    @Override
                    public List<FriendRequest> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<FriendRequest> result = new ArrayList<>();
                        QuerySnapshot snapshots = task.getResult();
                        for (DocumentSnapshot document : snapshots.getDocuments()) {
                            FriendRequest request = document.toObject(FriendRequest.class);
                            request.setDocumentId(document.getId());
                            result.add(request);
                        }
                        return result;
                    }
                });
    }

    public static void rejectRequest(String documentId){
        FriendsRequestsDAL.deleteFriendRequest(documentId);
    }

    public static void validateFriendRequest(FriendRequest friendRequest){
        FriendsRequestsDAL.deleteFriendRequest(friendRequest.getDocumentId());
        RelContacts relContacts1 = new RelContacts();
        relContacts1.setFrom(friendRequest.getFrom());
        relContacts1.setTo(friendRequest.getTo());

        RelContacts relContacts2 = new RelContacts();
        relContacts1.setFrom(friendRequest.getTo());
        relContacts1.setTo(friendRequest.getFrom());

        RelContactsDAL.createRelation(relContacts1);
        RelContactsDAL.createRelation(relContacts2);
    }

    public static Task<FriendRequest> sendFriendRequest(final FriendRequest friendRequest){
        return FriendsRequestsDAL.createFriendRequest(friendRequest)
                .continueWith(new Continuation<DocumentReference, FriendRequest>() {
                    @Override
                    public FriendRequest then(@NonNull Task<DocumentReference> task) throws Exception {
                        friendRequest.setDocumentId(task.getResult().getId());
                        return friendRequest;
                    }
                });
    }
}
