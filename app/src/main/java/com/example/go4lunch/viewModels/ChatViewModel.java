package com.example.go4lunch.viewModels;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.ChatRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.UploadTask;

import java.util.concurrent.Executor;

public class ChatViewModel extends ViewModel {


    private final ChatRepository chatDataSource;
    private final Executor executor;


    public final MutableLiveData<Query> allMessage= new MutableLiveData<>();
//    public final MutableLiveData<UploadTask> uploadImage = new MutableLiveData<>();


    public ChatViewModel(ChatRepository chatDataSource, Executor executor) {

        this.chatDataSource = chatDataSource;
        this.executor = executor;
    }

    public void init() {

        allMessage.setValue(chatDataSource.getAllMessageForChat());
//        uploadImage.setValue(chatDataSource.uploadImage(imageUri));
        //SOIT LIGNE 36 ET LA METHODE GETUPLOADIMAGE PLUS BAS SOIT SEND MESSAGE WITH IMAGE APPELLE 2 FOIS LE REPOSITORY
    }

    public MutableLiveData<Query> getAllMessage() {
        return allMessage;
    }

//    public MutableLiveData<UploadTask> getUploadImage() {
//        return uploadImage;
//    }


    public void createMessageForChat(String textMessage, User userSender) {
        //return ChatRepository.createMessageForChat(textMessage, userSender);

        executor.execute(() -> {

            chatDataSource.createMessageForChat(textMessage, userSender);
        });
    }

    /*public void sendMessageWithImageForChat( String textMessage, User userSender) {
        executor.execute(() -> {

            uploadImage.getValue() -> { chatDataSource.createMessageWithImageForChat(uploadImage.toString(), textMessage, userSender) } ;
        });


    }*/

    public void sendMessageWithImageForChat(Uri imageUri, String textMessage, User userSender) {

        executor.execute(() -> {

            chatDataSource.uploadImage(imageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> chatDataSource.createMessageWithImageForChat(uri.toString(), textMessage, userSender)));

        });

    }



}
