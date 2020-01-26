package com.example.localDB.repositories.relUsersConversationsRepositories;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.firelib.managers.ConversationManagement;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.repositories.conversationRepositories.ConversationDataListenerRepository;
import com.example.model.Conversation;
import com.example.model.RelUserConv;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;


public class RelUsersConversationsRepository implements EventListener<QuerySnapshot> {
    private Context context;
    private Query query;
    private ListenerRegistration registration;

    public RelUsersConversationsRepository(Context context, Query query) {
        this.context = context;
        this.query = query;
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e != null)
            return;
        for (final DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            RelUserConv rel = documentChange.getDocument().toObject(RelUserConv.class);

            ConversationManagement.getConversationById(rel.getId_conv()).continueWith(new Continuation<Conversation, Object>() {
                @Override
                public Object then(@NonNull Task<Conversation> task) throws Exception {
                    Conversation conversation = task.getResult();
                    switch (documentChange.getType()){
                        case ADDED:
                            new ConversationDataListenerRepository
                                    .InsertAsyncTask(DBConnect.getInstance(context).conversationDataUpdatesDAO())
                                    .execute(conversation);
                            break;
                        case REMOVED:
                            new ConversationDataListenerRepository
                                    .RemoveAsyncTask(DBConnect.getInstance(context).conversationDataUpdatesDAO())
                                    .execute(conversation);
                            break;
                        case MODIFIED:
                            new ConversationDataListenerRepository
                                    .UpdateAsyncTask(DBConnect.getInstance(context).conversationDataUpdatesDAO())
                                    .execute(conversation);
                            break;
                    }
                    return null;
                }
            });
        }
    }
}
