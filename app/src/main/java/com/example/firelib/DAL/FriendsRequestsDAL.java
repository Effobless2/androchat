package com.example.firelib.DAL;

import com.example.model.FriendRequest;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class FriendsRequestsDAL {

    public static Query getRequestAssignedTo(String googleId){
        return DbConnect.getDatabase()
                .collection(FriendRequest.COLLECTION_DATABASE_NAME)
                .whereEqualTo(FriendRequest.TO_DATABASE_FIELD, googleId);
    }

    public static Query getRequestFrom(String googleId){
        return DbConnect.getDatabase()
                .collection(FriendRequest.COLLECTION_DATABASE_NAME)
                .whereEqualTo(FriendRequest.FROM_DATABASE_FIELD, googleId);
    }

    public static Task<DocumentReference> createFriendRequest(FriendRequest friendRequest){
        return DbConnect.getDatabase()
                .collection(FriendRequest.COLLECTION_DATABASE_NAME)
                .add(friendRequest);
    }

    public static Task<Void> deleteFriendRequest(String documentId){
        return DbConnect.getDatabase()
                .collection(FriendRequest.COLLECTION_DATABASE_NAME)
                .document(documentId)
                .delete();
    }
}
