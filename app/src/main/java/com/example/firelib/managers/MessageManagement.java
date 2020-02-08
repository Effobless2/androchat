package com.example.firelib.managers;

import com.example.firelib.DAL.MessageDAL;
import com.example.model.Message;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class MessageManagement {
    private static final String LOG_CATEGORY = "MessageManagement";

    public static Task<List<Message>> getAllMessage(){
        return MessageDAL.getAllMessage().get()
                .continueWith(new Continuation<QuerySnapshot, List<Message>>() {
                    @Override
                    public List<Message> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<Message> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Message message = documentSnapshot.toObject(Message.class);
                            result.add(message);
                        }
                        return result;
                    }
                });
    }

    public static Task<List<Message>> getMessagesByIdUser(String id_user){
        return MessageDAL.getMessagesByIdUser(id_user).get()
                .continueWith(new Continuation<QuerySnapshot, List<Message>>() {
                    @Override
                    public List<Message> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<Message> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Message message = documentSnapshot.toObject(Message.class);
                            message.setId(documentSnapshot.getId());
                            result.add(message);
                        }
                        return result;
                    }
                });
    }

    public static Task<List<Message>> getMessagesByIdConv(String id_conv){
        return MessageDAL.getMessagesByIdConv(id_conv).get()
                .continueWith(new Continuation<QuerySnapshot, List<Message>>() {
                    @Override
                    public List<Message> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<Message> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Message message = documentSnapshot.toObject(Message.class);
                            message.setId(documentSnapshot.getId());
                            result.add(message);
                        }
                        return result;
                    }
                });
    }

    public static Task<Message> getMessageById(String id){
        return MessageDAL.getMessageById(id).get()
                .continueWith(new Continuation<DocumentSnapshot, Message>() {
                    @Override
                    public Message then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        DocumentSnapshot snapshot = task.getResult();
                        return snapshot.toObject(Message.class);
                    }
                });
    }

    public static Task<String> create(Message newMsg){
        return MessageDAL.createMessage(newMsg)
                .continueWith(new Continuation<DocumentReference, String>() {
                    @Override
                    public String then(@NonNull Task<DocumentReference> task) throws Exception {
                        DocumentReference documentReference = task.getResult();
                        return documentReference.getId();
                    }
                });
    }

}
