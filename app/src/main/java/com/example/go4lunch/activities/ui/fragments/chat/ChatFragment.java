package com.example.go4lunch.activities.ui.fragments.chat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.activities.ui.fragments.BaseFragment;
import com.example.go4lunch.models.User;
import com.example.go4lunch.views.ChatAdapter;

import javax.annotation.Nullable;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

public class ChatFragment extends BaseFragment implements ChatAdapter.Listener {

    // DATA FOR PICTURE

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

    //VARIABLES NEEDED FOR ACTIVITY RESULTLAUNCHER
    final String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    //BEGIN STANDARD CALL SPECIFICATION AND PREDEFINED TO AN ACTIVITY INITIALISED IN THE ONCREATE
    private ActivityResultContracts.RequestMultiplePermissions mRequestMultiplePermissionsContract;
    //LAUNCHER SPECIFICATION FOR A PREVIOUSLY PREPARED CALL TO START THE PROCESS OF EXECUTING AN ACTIVITYRESULT CONTRACT
    //INITIALISED ON THE ONCREATE
    private ActivityResultLauncher<String[]> multiplePermissionActivityResultLauncher;
    //END ZONE FOR NECESSAIRELY VARIABLES OF ACTIVITYRESULTLAUCHER


    public ChatFragment() {
        //EMPTY CONSTRUCTOR
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //INFLATE LAYOUT FOR THIS FRAGMENT
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this,view);

        /*this.configureRecyclerView();
        this.getCurrentUserFromFirestore();*/

        // BEGIN INITIALISED OBJECTS ACTIVITYRESULTCONTRACTS AND ACTIVITYRESULTLAUNCHER
        mRequestMultiplePermissionsContract = new ActivityResultContracts.RequestMultiplePermissions();
        multiplePermissionActivityResultLauncher = registerForActivityResult(mRequestMultiplePermissionsContract, isGranted -> {
            Log.d(TAG, "launcher result :" + isGranted.toString());
            if (isGranted.containsValue(false)) {
                Log.d(TAG, "At least one of the permissions was not granted, launching again...");
                multiplePermissionActivityResultLauncher.launch(PERMISSIONS);
            }
        });
        //CHECKING PERMISSIONS
        askPermissions(PERMISSIONS);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle saveInstanceState) {
        super.onViewCreated(view, saveInstanceState);
        getActionBar().setTitle(R.string.chat_coworkers);
    }

    private void askPermissions(String [] PERMISSIONS) {

    }

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponse(requestCode, resultCode, data);
    }*/

    @Override
    public void onDataChanged() {

    }

}
