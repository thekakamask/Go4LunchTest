package com.example.go4lunch.repository;

import android.net.Uri;

import com.example.go4lunch.models.User;
import com.google.firebase.firestore.Query;

public class ChatManager {

    private static volatile ChatManager instance;
    private ChatRepository chatRepository;

    private ChatManager() {
        chatRepository= ChatRepository.getInstance();

    }

    public static ChatManager getInstance() {
        ChatManager result = instance;
        if (result != null) {
            return result;
        }
        synchronized(ChatManager.class) {
            if (instance == null) {
                instance = new ChatManager();
            }
            return instance;
        }
    }

    /*public static CollectionReference getUsersCollection() {
        //return ChatRepository.getChatCollection();
        return ChatRepository.getChatCollection();
    }*/

    public void createMessageForChat(String textMessage, User userSender) {
        //return ChatRepository.createMessageForChat(textMessage, userSender);
        chatRepository.createMessageForChat(textMessage, userSender);
    }

    /*public static Task<DocumentReference> createMessageWithImageForChat(String urlImage, String textMessage, User userSender ) {
        //return ChatRepository.createMessageWithImageForChat(urlImage, textMessage, userSender);
        return ChatRepository.createMessageWithImageForChat(urlImage, textMessage, userSender);
    }*/

    public void sendMessageWithImageForChat(Uri imageUri, String textMessage, User userSender) {
        chatRepository.uploadImage(imageUri).addOnSuccessListener(taskSnapshot -> taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> chatRepository.createMessageWithImageForChat(uri.toString(), textMessage, userSender)));
    }

    /*public static Task<DocumentReference> sendMessageWithImageForChatTest(Uri imageUri, String textMessage, User userSender) {
        ChatRepository.uploadImage(imageUri);
        return ChatRepository.createMessageWithImageForChat(imageUri.toString(), textMessage, userSender);
    }*/

    public Query getAllMessageForChat() {
        //return ChatRepository.getAllMessageForChat(chat);
        return chatRepository.getAllMessageForChat();

    }

}
