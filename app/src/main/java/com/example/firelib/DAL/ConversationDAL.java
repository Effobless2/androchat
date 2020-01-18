package com.example.firelib.DAL;

import com.example.model.Conversation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class ConversationDAL {

    public static Query getAllConversation(){
        return DbConnect.getDatabase()
                .collection(Conversation.COLLECTION_DATABASE_NAME)
                .orderBy(Conversation.LAST_MESSAGE_DATE_DATABASE_FIELD);
    }

    public static Query getConversationByName(String name){
        return DbConnect.getDatabase()
                .collection(Conversation.COLLECTION_DATABASE_NAME)
                .whereEqualTo(Conversation.NAME_DATABASE_FIELD, name);
    }

    public static DocumentReference getConversationById(String id){
        return DbConnect.getDatabase()
                .collection(Conversation.COLLECTION_DATABASE_NAME)
                .document(id);
    }

    public static Task<DocumentReference> createConversation(Conversation conv){
        return  DbConnect.getDatabase()
                .collection(Conversation.COLLECTION_DATABASE_NAME)
                .add(conv);
    }

}