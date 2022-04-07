package com.example.go4lunch.utils;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;


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

    public static void createUser(String uid) {
        mUserRepository.createUserInFirestore(uid);
    }

    public Task<DocumentSnapshot> getUserData(String uid) {
        //String uid =mUserRepository.getCurrentUserUID();
        mUserRepository.getUserData(uid);
        return mUserRepository.getUsersCollection().document(uid).get();
    }

    public static CollectionReference getUsersCollection() {
        return UserRepository.getUsersCollection();
    }
    /*public static CollectionReference getUserCollection(){
        UserRepository.getUsersCollection();
    }*/

    public static Task<Void> deleteLike(String uid, String idOfPlace) {
        return mUserRepository.deleteLike(uid, idOfPlace);
    }

    public static Task<Void> updateLike(String uid, String idOfPlace) {
        return mUserRepository.updateLike(uid, idOfPlace);
    }

    public static Task<Void> deleteIdOfPlace(String uid) {
        return mUserRepository.deleteIdOfPlace(uid);
    }

    public static Task<Void> updateIdOfPlace(String uid, String idOfPlace, int currentTime) {
        return mUserRepository.updateIdOfPlace(uid, idOfPlace, currentTime);
    }

}
