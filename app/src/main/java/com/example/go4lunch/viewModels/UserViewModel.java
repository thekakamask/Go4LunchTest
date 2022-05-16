package com.example.go4lunch.viewModels;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.models.Message;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.ChatRepository;
import com.example.go4lunch.repository.UserRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Collection;
import java.util.concurrent.Executor;

public class UserViewModel extends ViewModel {

    private final UserRepository userDataSource;
    private final Executor executor;


    public final MutableLiveData<FirebaseUser> currentUser = new MutableLiveData<>();
    public final MutableLiveData<Task<DocumentSnapshot>> userData = new MutableLiveData();
    public final MutableLiveData<CollectionReference> userCollection = new MutableLiveData<>();


    public UserViewModel(UserRepository userDataSource, Executor executor) {
        this.userDataSource = userDataSource;
        this.executor = executor;
    }

    public void init (String uid) {

        currentUser.setValue(userDataSource.getCurrentUser());
        userCollection.setValue(userDataSource.getUsersCollection());
        userData.setValue(userDataSource.getUserData(uid));
        //SOIT LIGNE 43 SOIT LIGNE 60


    }

    public MutableLiveData<FirebaseUser> getCurrentUser() {
        return currentUser;
    }

    public MutableLiveData<CollectionReference> getUsersCollection() {
        //return UserRepository.getUsersCollection();
        return userCollection;
    }

    /*public MutableLiveData<Task<DocumentSnapshot>> setUserData(String uid) {
        //String uid =mUserRepository.getCurrentUserUID();
        userData.setValue(userRepository.getUserData(uid));
        return userData;
        SOIT LIGNE 43 SOIT CETTE METHODE LA
    }*/

    public MutableLiveData<Task<DocumentSnapshot>> getUserData (String uid) {

        return userData;
    }

    public void createUser(String uid) {

        executor.execute(() -> {

            userDataSource.createUserInFirestore(uid);
        });
    }

    public void deleteLike(String uid, String idOfPlace) {

        executor.execute(() -> {

            userDataSource.deleteLike(uid, idOfPlace);
        });
    }

    public void updateLike(String uid, String idOfPlace) {
        executor.execute(() -> {

            userDataSource.updateLike(uid, idOfPlace);
        });
    }

    public void deleteIdOfPlace(String uid) {

        executor.execute(() -> {

            userDataSource.deleteIdOfPlace(uid);
        });
    }

    public void updateIdOfPlace(String uid, String idOfPlace, int currentTime) {

        executor.execute(() -> {

            userDataSource.updateIdOfPlace(uid, idOfPlace, currentTime);

        });

    }










}
