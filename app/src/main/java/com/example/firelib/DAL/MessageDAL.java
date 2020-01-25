package com.example.firelib.DAL;

import com.example.model.Message;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class MessageDAL {

    public static Query getAllMessage(){
        return DbConnect.getDatabase()
                .collection(Message.COLLECTION_DATABASE_NAME)
                .orderBy(Message.DATE_DATABASE_FIELD);
    }

    public static Query getMessagesByIdUser(String id_user){
        return DbConnect.getDatabase()
                .collection(Message.COLLECTION_DATABASE_NAME)
                .whereEqualTo(Message.ID_USER_DATABASE_FIELD, id_user);
    }

    public static Query getMessagesByIdConv(String id_conv){
        return DbConnect.getDatabase()
                .collection(Message.COLLECTION_DATABASE_NAME)
                .whereEqualTo(Message.ID_CONVERSATION_DATABASE_FIELD, id_conv);
    }

    public static DocumentReference getMessageById(String id){
        return DbConnect.getDatabase()
                .collection(Message.COLLECTION_DATABASE_NAME)
                .document(id);
    }

    public static Task<DocumentReference> createMessage(Message message){
        return  DbConnect.getDatabase()
                .collection(Message.COLLECTION_DATABASE_NAME)
                .add(message);
    }

}
