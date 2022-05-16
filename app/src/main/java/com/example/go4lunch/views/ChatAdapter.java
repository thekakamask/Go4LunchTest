package com.example.go4lunch.views;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.bumptech.glide.RequestManager;
import com.example.go4lunch.R;
import com.example.go4lunch.models.Message;
import com.example.go4lunch.viewModels.UserManager;
import com.example.go4lunch.viewModels.UserViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatAdapter extends FirestoreRecyclerAdapter<com.example.go4lunch.models.Message,ChatViewHolder> {



    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     *
     */

    public interface Listener {
        void onDataChanged();
    }


    // VIEW TYPES
    private static final int SENDER_TYPE = 1;
    private static final int RECEIVER_TYPE=2;


    //DATA
    private final RequestManager glide;
    //private final String idCurrentUser;

    //FOR COMMUNICATION
    private final Listener callback;

    public ChatAdapter(@NonNull FirestoreRecyclerOptions<Message> options, RequestManager glide, Listener callback) {
        super(options);
        this.glide= glide;
        this.callback = callback;
        //this.idCurrentUser = idCurrentUser;
    }

    @Override
    public int getItemViewType(int position) {
        //DETERMINE THE TYPE OF THE MESAGE BY IF THE USER IS THE SENDER OR NOT

        //INIT VIEWMODEL WITH PROVIDERS
        UserViewModel userViewModel = new ViewModelProvider((ViewModelStoreOwner) this).get(UserViewModel.class);

        //String currentUserId = UserManager.getInstance().getCurrentUser().getUid(); INTEAD LINE 61

        String currentUserId = userViewModel.getCurrentUser().getValue().getUid();
        boolean isSender = getItem(position).getUserSender().getUid().equals(currentUserId);

        return (isSender) ? SENDER_TYPE : RECEIVER_TYPE;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_item, parent, false));
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatViewHolder holder, int position, @NonNull Message model) {
        holder.itemView.invalidate();
        holder.updateWithMessage(model, this.glide);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        this.callback.onDataChanged();
    }


}
