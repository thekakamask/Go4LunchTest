package com.example.go4lunch.utils;

import com.example.go4lunch.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Source;



public class UserManager {

    private static volatile UserManager instance;
    private static UserRepository mUserRepository;

    private UserManager() {
        mUserRepository= UserRepository.getInstance();
    }

    public static UserManager getInstance() {
        UserManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserManager();
            }
            return instance;
        }
    }

    public static FirebaseUser getCurrentUser() {
        return mUserRepository.getCurrentUser();
    }

    public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    public static void createUser() {
        mUserRepository.createUserInFirestore();
    }

    public Task<DocumentSnapshot> getUserData() {
        String uid =mUserRepository.getCurrentUserUID();
        mUserRepository.getUserData();
        return mUserRepository.getUsersCollection().document(uid).get();
    }
}
