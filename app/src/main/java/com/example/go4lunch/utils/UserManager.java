package com.example.go4lunch.utils;

import android.annotation.SuppressLint;
import com.example.go4lunch.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserManager {

    private static volatile UserManager instance;
    @SuppressLint("StaticFieldLeak")
    private final UserRepository userRepository;

    private UserManager() {
        userRepository= UserRepository.getInstance();
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

    public FirebaseUser getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    /*public Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }*/

    public void createUser(String uid) {
        userRepository.createUserInFirestore(uid);
    }

    public Task<DocumentSnapshot> getUserData(String uid) {
        //String uid =mUserRepository.getCurrentUserUID();
        userRepository.getUserData(uid);
        return userRepository.getUsersCollection().document(uid).get();
    }

    public CollectionReference getUsersCollection() {
        //return UserRepository.getUsersCollection();
        return userRepository.getUsersCollection();
    }
    /*public static CollectionReference getUserCollection(){
        UserRepository.getUsersCollection();
    }*/

    public void deleteLike(String uid, String idOfPlace) {
        userRepository.deleteLike(uid, idOfPlace);
    }

    public void updateLike(String uid, String idOfPlace) {
        userRepository.updateLike(uid, idOfPlace);
    }

    public void deleteIdOfPlace(String uid) {
        userRepository.deleteIdOfPlace(uid);
    }

    public void updateIdOfPlace(String uid, String idOfPlace, int currentTime) {
        userRepository.updateIdOfPlace(uid, idOfPlace, currentTime);
    }

}
