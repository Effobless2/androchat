package com.example.localDB.repositories;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.firelib.DAL.RelUserConvDAL;
import com.example.localDB.DAO.DBConnect;
import com.example.localDB.DAO.RelUserConvDAO;
import com.example.model.RelUserConv;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class RelUserConvRepository implements EventListener<QuerySnapshot> {
    private RelUserConvDAO relUserConvDAO;
    private Query query;
    private ListenerRegistration registration;

    public RelUserConvRepository(Context context){
        relUserConvDAO = DBConnect.getInstance(context).relUserConvDAO();
        query = RelUserConvDAL.getAllRelUserConv();
        startQuery();
    }

    public void startQuery(){
        if(query != null && registration == null){
            registration = query.addSnapshotListener(this);
        }
    }

    private void insert(RelUserConv relUserConv) {
        new InserAsyncTask(relUserConvDAO).execute(relUserConv);
    }

    private void delete(RelUserConv relUserConv) {
        new DeleteAsyncTask(relUserConvDAO).execute(relUserConv);
    }

    private void update(RelUserConv relUserConv) {
        new UpdateAsyncTask(relUserConvDAO).execute(relUserConv);
    }

    public LiveData<List<RelUserConv>> getAll(){
        return relUserConvDAO.getAll();
    }

    @Override
    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
        if(e != null)
            return;
        for (DocumentChange documentChange : queryDocumentSnapshots.getDocumentChanges()) {
            RelUserConv relUserConv = documentChange.getDocument().toObject(RelUserConv.class);
            relUserConv.setId(documentChange.getDocument().getId());
            switch (documentChange.getType()){
                case ADDED:
                    insert(relUserConv);
                    break;
                case REMOVED:
                    delete(relUserConv);
                    break;
                case MODIFIED:
                    update(relUserConv);
                    break;
            }
        }
    }

    public static class InserAsyncTask extends AsyncTask<RelUserConv, Void, Void>{
        private RelUserConvDAO relUserConvDAO;

        public InserAsyncTask(RelUserConvDAO relUserConvDAO) {
            this.relUserConvDAO = relUserConvDAO;
        }

        @Override
        protected Void doInBackground(RelUserConv... relUserConvs) {
            for (RelUserConv relUserConv : relUserConvs) {
                try{
                    relUserConvDAO.insert(relUserConv);
                } catch (Exception e){

                }
            }
            return null;
        }
    }

    public static class DeleteAsyncTask extends AsyncTask<RelUserConv, Void, Void>{
        private RelUserConvDAO relUserConvDAO;

        public DeleteAsyncTask(RelUserConvDAO relUserConvDAO) {
            this.relUserConvDAO = relUserConvDAO;
        }


        @Override
        protected Void doInBackground(RelUserConv... relUserConvs) {
            for (RelUserConv relUserConv : relUserConvs) {
                relUserConvDAO.delete(relUserConv);
            }
            return null;
        }
    }

    public static class UpdateAsyncTask extends AsyncTask<RelUserConv, Void, Void>{
        private RelUserConvDAO relUserConvDAO;

        public UpdateAsyncTask(RelUserConvDAO relUserConvDAO) {
            this.relUserConvDAO = relUserConvDAO;
        }


        @Override
        protected Void doInBackground(RelUserConv... relUserConvs) {
            for (RelUserConv relUserConv : relUserConvs) {
                relUserConvDAO.update(relUserConv);
            }
            return null;
        }
    }

}
