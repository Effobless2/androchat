package com.example.firelib;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.model.User;
import com.example.model.UserRegistration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;

import java.util.List;

class RegistrationAsyncTask extends AsyncTask<Void, Void, TaskCompletionSource<String>> {
    Task<Boolean> loginVerificationTask;
    Task<Boolean> pseudoVerifiedTask;
    UserRegistration newUser;
    TaskCompletionSource<String> result;

    public Task<String> getTask(){
        return result.getTask();
    }

    public RegistrationAsyncTask(UserRegistration newUser){
        result = new TaskCompletionSource<>();
        this.newUser = newUser;
        loginVerificationTask = UserManagement.getUserByLogin(newUser.getLogin())
                .continueWith(new Continuation<List<User>, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<List<User>> task) throws Exception {
                        List<User> users = task.getResult();
                        return users.size() == 0;
                    }
                });
        pseudoVerifiedTask = UserManagement.getUserByPseudo(newUser.getPseudo())
                .continueWith(new Continuation<List<User>, Boolean>() {
                    @Override
                    public Boolean then(@NonNull Task<List<User>> task) throws Exception {
                        List<User> users = task.getResult();
                        return users.size() == 0;
                    }
                });
    }

    @Override
    protected TaskCompletionSource<String> doInBackground(Void... voids) {
        while(!loginVerificationTask.isComplete() || !pseudoVerifiedTask.isComplete()){}
        boolean verifLogin = loginVerificationTask.getResult();
        boolean verifPseudo = pseudoVerifiedTask.getResult();
        if(verifLogin && verifPseudo){
            Task<DocumentReference> resultTask = UserDAL.register(newUser);
            while(!resultTask.isComplete()){}
            result.setResult(resultTask.getResult().getId());
            return null;
        }
        result.setResult(null);
        return null; //TODO: Throw Some Big Shits

    }


}
