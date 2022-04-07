package com.example.go4lunch.repository;

import com.example.go4lunch.models.Message;
import com.example.go4lunch.models.User;
import com.example.go4lunch.utils.UserManager;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ChatRepository {

    public static final String COLLECTION_NAME ="chats";

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

    public static CollectionReference getChatCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public static Query getAllMessageForChat(String chat) {
        return FirebaseFirestore.getInstance()

                .collection(COLLECTION_NAME)
                .orderBy("dateCreated")
                .limit(50);
    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, User userSender) {
        // CREATE THE MESSAGE OBJECT
        Message message = new Message(textMessage, userSender);
        //STORE MESSSAGE TO FIRESTORE
        return ChatRepository.getChatCollection()
                .add(message);
    }

    public static Task<DocumentReference> createMessageWithImageForChat(String urlImage, String textMessage, User userSender) {
        Message message = new Message(textMessage, urlImage, userSender);
        return ChatRepository.getChatCollection()
                .add(message);
    }

}
