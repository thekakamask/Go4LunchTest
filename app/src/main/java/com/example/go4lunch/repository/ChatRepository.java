package com.example.go4lunch.repository;

import android.net.Uri;

import com.example.go4lunch.models.Message;
import com.example.go4lunch.models.User;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public final class ChatRepository {

    public static final String CHAT_COLLECTION ="chats";

    private static volatile ChatRepository instance;

    private ChatRepository() {
    }

    public static ChatRepository getInstance() {
        ChatRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (ChatRepository.class) {
            if(instance == null) {
                instance= new ChatRepository();
            }
            return instance;
        }
    }

    public CollectionReference getChatCollection() {
        return FirebaseFirestore.getInstance().collection(CHAT_COLLECTION);
    }

    public Query getAllMessageForChat() {
        return FirebaseFirestore.getInstance()

                .collection(CHAT_COLLECTION)
                .orderBy("dateCreated")
                .limit(50);
    }

    public void createMessageForChat(String textMessage, User userSender) {

        // CREATE THE MESSAGE OBJECT
        Message message = new Message(textMessage, userSender);
        //STORE MESSSAGE TO FIRESTORE
        instance.getChatCollection()
                .add(message);
    }

    public void createMessageWithImageForChat(String urlImage, String textMessage, User userSender) {
        Message message = new Message(textMessage, urlImage, userSender);

        //STORE MESSAGE ON FIRESTORE
        instance.getChatCollection()
                .add(message);
    }

    public UploadTask uploadImage(Uri imageUri) {
        String uuid = UUID.randomUUID().toString(); //GENERATE UNIQUE STRING

        StorageReference mImageRef = FirebaseStorage.getInstance().getReference(uuid);

        return mImageRef.putFile(imageUri);

    }

}
