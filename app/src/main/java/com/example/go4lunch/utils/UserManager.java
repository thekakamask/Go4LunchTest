package com.example.go4lunch.utils;

import com.example.go4lunch.repository.UserRepository;
import com.google.firebase.auth.FirebaseUser;

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
}
