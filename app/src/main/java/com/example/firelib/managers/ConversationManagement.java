package com.example.firelib.managers;

import androidx.annotation.NonNull;

import com.example.firelib.DAL.ConversationDAL;
import com.example.model.Conversation;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ConversationManagement {
    private static final String LOG_CATEGORY = "ConversationManagement";

    public static Task<List<Conversation>> getAllConversation(){
        return ConversationDAL.getAllConversation().get()
                .continueWith(new Continuation<QuerySnapshot, List<Conversation>>() {
                    @Override
                    public List<Conversation> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<Conversation> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Conversation conv = documentSnapshot.toObject(Conversation.class);
                            conv.setId(documentSnapshot.getId());
                            result.add(conv);
                        }
                        return result;
                    }
                });
    }

    public static Task<List<Conversation>> getConversationByName(String name){
        return ConversationDAL.getConversationByName(name).get()
                .continueWith(new Continuation<QuerySnapshot, List<Conversation>>() {
                    @Override
                    public List<Conversation> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        List<Conversation> result = new ArrayList<>();
                        QuerySnapshot snapshot = task.getResult();
                        List<DocumentSnapshot> documentSnapshots = snapshot.getDocuments();
                        for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                            Conversation conv = documentSnapshot.toObject(Conversation.class);
                            conv.setId(documentSnapshot.getId());
                            result.add(conv);
                        }
                        return result;
                    }
                });
    }

    public static Task<Conversation> getConversationById(String id){
        return ConversationDAL.getConversationById(id).get()
                .continueWith(new Continuation<DocumentSnapshot, Conversation>() {
                    @Override
                    public Conversation then(@NonNull Task<DocumentSnapshot> task) throws Exception {
                        DocumentSnapshot snapshot = task.getResult();
                        Conversation conversation = snapshot.toObject(Conversation.class);
                        conversation.setId(snapshot.getId());
                        return conversation;
                    }
                });
    }

    public static Task<String> create(Conversation newConv){
        return ConversationDAL.createConversation(newConv)
                .continueWith(new Continuation<DocumentReference, String>() {
                    @Override
                    public String then(@NonNull Task<DocumentReference> task) throws Exception {
                        DocumentReference documentReference = task.getResult();
                        return documentReference.getId().toString();
                    }
                });
    }

}
