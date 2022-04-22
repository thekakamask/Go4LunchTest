package com.example.go4lunch.repository;

import android.net.Uri;

import com.example.go4lunch.models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;

public class ChatManager {

    private static volatile ChatManager instance;
    private static ChatRepository mChatRepository;

    private ChatManager() {
        mChatRepository = ChatRepository.getInstance();
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

    public static CollectionReference getUsersCollection() {
        //return ChatRepository.getChatCollection();
        return mChatRepository.getChatCollection();
    }

    public static Task<DocumentReference> createMessageForChat(String textMessage, User userSender) {
        //return ChatRepository.createMessageForChat(textMessage, userSender);
        return mChatRepository.createMessageForChat(textMessage, userSender);
    }

    public static Task<DocumentReference> createMessageWithImageForChat(String urlImage, String textMessage, User userSender ) {
        //return ChatRepository.createMessageWithImageForChat(urlImage, textMessage, userSender);
        return mChatRepository.createMessageWithImageForChat(urlImage, textMessage, userSender);
    }

    public void sendMessageWithImageForChat(Uri imageUri, String textMessage, User userSender) {
        mChatRepository.uploadImage(imageUri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                mChatRepository.createMessageWithImageForChat(uri.toString(), textMessage, userSender);
            });
        });
    }

    public static Task<DocumentReference> sendMessageWithImageForChatTest(Uri imageUri, String textMessage, User userSender) {
        mChatRepository.uploadImage(imageUri);
        return mChatRepository.createMessageWithImageForChat(imageUri.toString(), textMessage, userSender);
    }

    public static Query getAllMessageForChat(String chat) {
        //return ChatRepository.getAllMessageForChat(chat);
        return mChatRepository.getAllMessageForChat(chat);

    }

}
