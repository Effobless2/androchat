package com.example.firelib.managers;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.example.firelib.DAL.UserDAL;
import com.example.firelib.exceptions.RegistrationErrors;
import com.example.firelib.exceptions.RegistrationException;
import com.example.model.User;
import com.example.model.UserRegistration;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.List;

class RegistrationAsyncTask extends AsyncTask<Void, Void, Void> {
    private static final long INITIAL_TIMEOUT = 300_000;
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
    }

    private void initUnityVerificationTasks(){
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
    protected Void doInBackground(Void... voids) {
        boolean loginStrVerif = UserManagement.loginOrPseudoValidation(newUser.getLogin());
        boolean pseudoStrVerif = UserManagement.loginOrPseudoValidation(newUser.getPseudo());

        if(!loginStrVerif || !pseudoStrVerif){
            List<RegistrationErrors> errors = new ArrayList<>();
            if(!loginStrVerif)errors.add(RegistrationErrors.LOGIN_NOT_IN_CORRECT_FORMAT);
            if(!pseudoStrVerif)errors.add(RegistrationErrors.PSEUDO_NOT_IN_CORRECT_FORMAT);
            result.setException(new RegistrationException(errors));
            return null;
        }
        try{
            long timeout = INITIAL_TIMEOUT;
            long currentH;

            initUnityVerificationTasks();
            currentH = System.currentTimeMillis();
            while((!loginVerificationTask.isComplete() || !pseudoVerifiedTask.isComplete()) && timeout >= 0){
                timeout -= System.currentTimeMillis() - currentH;
                currentH = System.currentTimeMillis();
            }

            if(!loginVerificationTask.isComplete() || !pseudoVerifiedTask.isComplete()){
                List<RegistrationErrors> errors = new ArrayList<>();
                errors.add(RegistrationErrors.REQUEST_NOT_CACHED);
                result.setException(new RegistrationException(errors));
                return null;
            }

            boolean verifyLogin = loginVerificationTask.getResult();
            boolean verifyPseudo = pseudoVerifiedTask.getResult();

            if(!verifyLogin || !verifyPseudo){
                List<RegistrationErrors> errors = new ArrayList<>();
                if(!verifyLogin) errors.add(RegistrationErrors.LOGIN_ALREADY_USED);
                if(!verifyPseudo) errors.add(RegistrationErrors.PSEUDO_ALREADY_USED);
                result.setException(new RegistrationException(errors));
                return null;
            }

            timeout = INITIAL_TIMEOUT;
            Task<DocumentReference> resultTask = UserDAL.register(newUser);

            currentH = System.currentTimeMillis();
            while(!resultTask.isComplete() && timeout >= 0){
                timeout -= System.currentTimeMillis() - currentH;
                currentH = System.currentTimeMillis();
            }

            if(!resultTask.isComplete()){
                List<RegistrationErrors> errors = new ArrayList<>();
                errors.add(RegistrationErrors.REQUEST_CACHED);
                result.setException(new RegistrationException(errors));
                return null;
            }

            result.setResult(resultTask.getResult().getId());
            return null;

        } catch (Exception e){
            List<RegistrationErrors> errors = new ArrayList<>();
            errors.add(RegistrationErrors.SYSTEM_ERROR);
            result.setException(new RegistrationException(errors));
            return null;
        }
    }
}
