package com.example.go4lunch.activities.ui.fragments.chat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.models.Message;
import com.example.go4lunch.models.User;
import com.example.go4lunch.repository.ChatManager;
import com.example.go4lunch.utils.UserManager;
import com.example.go4lunch.views.ChatAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static com.example.go4lunch.utils.UserManager.getCurrentUser;

import org.w3c.dom.Text;

import java.util.Objects;

public class ChatFragment extends BaseFragment implements ChatAdapter.Listener {

    /*@BindView(R.id.fragment_chat_RV_container)
    LinearLayout LinearLayout;*/
    @BindView(R.id.chat_fragment_layout)
    RelativeLayout mRelativeLayout;
    @BindView(R.id.fragment_chat_empty_RV)
    TextView ThreadTitle;
    @BindView(R.id.fragment_chat_RV)
    RecyclerView messageRecyclerView;
    @BindView(R.id.fragment_chat_image_PV)
    ImageView imagePreviewContainer;
    @BindView(R.id.fragment_chat_message_container)
    LinearLayout messageContainer;
    @BindView(R.id.fragment_chat_addFileButton)
    ImageButton imageAddButton;
    @BindView(R.id.fragment_chat_chatEditText)
    TextView textEdit;
    @BindView(R.id.fragment_chat_sendBtn)
    Button sendButton;

    // STATIC DATA FOR PICTURE
    private static final String PERMS = Manifest.permission.READ_EXTERNAL_STORAGE;

    private static final int RC_IMAGE_PERMS = 100;
    private static final int RC_CHOOSE_PHOTO = 200;

    //    DECLARATION
    private ChatAdapter chatAdapter;
    private ChatManager chatManager;
    private UserManager userManager;


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
        this.setupListeners();
        getCurrentUserFromFirestore();
        return view;
    }



    private void getCurrentUserFromFirestore() {
        UserManager.getUserData(Objects.requireNonNull(getCurrentUser()).getUid()).addOnSuccessListener(documentSnapshot -> modelCurrentUser = documentSnapshot.toObject(User.class));
    }

    private void configureRecyclerView() {
        chatAdapter = new ChatAdapter(generateOptionsForAdapter(chatManager.getAllMessageForChat(chat)),
                Glide.with(this), this);

        chatAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                messageRecyclerView.smoothScrollToPosition(chatAdapter.getItemCount());
            }
        });

        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        messageRecyclerView.setAdapter(this.chatAdapter);

    }

    private void setupListeners() {
        sendButton.setOnClickListener(view -> {sendMessage();
        });

        imageAddButton.setOnClickListener(view -> {addFile();
        });

    }

    private void sendMessage() {
        boolean canSendMessage = !TextUtils.isEmpty(textEdit.getText().toString());

        if (canSendMessage) {
            chatManager.createMessageForChat(textEdit.getText().toString(), modelCurrentUser);
        }
        textEdit.setText("");
    }

    @AfterPermissionGranted(RC_IMAGE_PERMS)
    private void addFile() {
        if (!EasyPermissions.hasPermissions(requireContext(), PERMS)) {
            EasyPermissions.requestPermissions(this,
                    getString(R.string.popup_title_permission_files_access), RC_IMAGE_PERMS, PERMS);
            return;
        }

        showSnackBar(getString(R.string.files_access_granted));

        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pictureLauncher.launch(i);

        //Toast.makeText(this, "you have the right to access to images",
        // Toast.LENGTH_SHORT).show();
    }

    private final ActivityResultLauncher<Intent> pictureLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            pictureAccess(result);
                        }
                    }
            );

    private void pictureAccess(ActivityResult result) {
        if(result.getResultCode() == RESULT_OK) { //SUCCESS
            Intent data;
            data = result.getData();
            this.uriImageSelected = data.getData();
            Glide.with(this) //SHOWING PREVIEW OF IMAGE
                    .load(this.uriImageSelected)
                    .apply(RequestOptions.circleCropTransform())
                    .into(imagePreviewContainer);
        } else {
            showSnackBar(getString(R.string.toast_title_no_image_choosen));
        }
    }



    private void showSnackBar (String message) {
        Snackbar.make(mRelativeLayout, message, Snackbar.LENGTH_SHORT).show();
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }*/

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }

    // HANDLE ACTIVITY RESPONSE (AFTER USER HAS CHOSEND OR NOT A PICTURE)
    private void handleResponse(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_CHOOSE_PHOTO) {
            if (resultCode == RESULT_OK) {
                this.uriImageSelected = data.getData(); //SUCCESS
                Glide.with(this) // SHOWING PREVIEW OF IMAGE
                        .load(this.uriImageSelected)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imagePreviewContainer);
            } else {
                showSnackBar(getString(R.string.toast_title_no_image_choosen));
            }
        }
    }*/

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        getActionBar().setTitle(R.string.chat_coworkers);
    }


    @Override
    public void onDataChanged() {
        //Show TextView in case RecyclerView is empty
        ThreadTitle.setVisibility(this.chatAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    private FirestoreRecyclerOptions<Message> generateOptionsForAdapter(Query query) {
        return new FirestoreRecyclerOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLifecycleOwner(this)
                .build();
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
