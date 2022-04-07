package com.example.go4lunch.activities.ui.fragments.chat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.models.Message;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.ChatManager;
import com.example.go4lunch.utils.UserManager;
import com.example.go4lunch.views.ChatAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

import org.w3c.dom.Text;

public class ChatFragment extends BaseFragment implements ChatAdapter.Listener {

    @BindView(R.id.fragment_chat_RV_container)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_chat_empty_RV)
    TextView ThreadTitle;
    @BindView(R.id.fragment_chat_RV)
    RecyclerView messageRecyclerView;
    @BindView(R.id.fragment_chat_image_PV)
    ImageView imageContainer;
    @BindView(R.id.fragment_chat_message_container)
    LinearLayout messageContainer;
    @BindView(R.id.fragment_chat_addFileButton)
    ImageButton imageAddButton;
    @BindView(R.id.fragment_chat_chatEditText)
    TextView TextEdit;
    @BindView(R.id.fragment_chat_sendBtn)
    Button sendButton;

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    //    DECLARATION
    private ChatAdapter chatAdapter;
    @Nullable
    private User modelCurrentUser;
    private String chat;
    private Uri uriImageSelected;

    public ChatFragment() {
        //EMPTY PUBLIC CONSTRUCTOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //INFLATE THE LAYOUT FOR THIS FRAGMENT
        View view = inflater.inflate(R.layout.fragment_chat, container, false );
        ButterKnife.bind(this,view);

        this.configureRecyclerView();
        this.getCurrentUserFromFirestore();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        getActionBar().setTitle(R.string.chat_coworkers);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {

    }






    @Override
    public void onDataChanged() {
        //Show TextView in case RecyclerView is empty
        ThreadTitle.setVisibility(this.chatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }








    /*// DATA FOR PICTURE

    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    //DECLARATION VIEW
    @BindView(R.id.chat_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.chat_text_view_recycler_view_empty)
    TextView mTextViewRecyclerViewEmpty;
    @BindView(R.id.chat_message_edit_text)
    EditText mEditTextMessage;
    @BindView(R.id.chat_image_chosen_preview)
    ImageView mImageViewPreview;

    //DECLARATION
    private ChatAdapter mChatAdapter;
    @Nullable
    private User mCurrentUser;
    private String chat;
    private Uri mUriImageSelected;


    public ChatFragment() {
        //EMPTY CONSTRUCTOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //INFLATE LAYOUT FOR THIS FRAGMENT
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,view);

        this.configureRecyclerView();
        this.getCurrentUserFromFirestore();

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        getActionBar().setTitle(R.string.chat_coworkers);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grandResults);
        
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    private void handleResponse(int requestCode, int resultCode, Intent data) {

    }
   *//* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }*//*

    @Override
    public void onDataChanged() {

    }*/

}
