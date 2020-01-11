package com.example.firelib;

public interface FirebaseContract {

        public boolean register();

        public boolean login();

        public String getConverssationById(String id);

        public boolean requestAddition(String id);

        public boolean confirmationAddContact(String id);

        public String createConverssation(String name);

        public boolean sendMessage(String idConv, String msg);

        public boolean deleteConverssation(String idConv);

}
