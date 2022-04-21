package com.example.go4lunch.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.solver.state.State;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.models.Message;

import bolts.Bolts;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.go4lunch.utils.DatesHours.convertDateHours;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatViewHolder extends RecyclerView.ViewHolder {

    //ROOTVIEW
    @BindView(R.id.fragment_chat_item_layout)
    ConstraintLayout rootView;

    //CONTAINER PROFILE
    @BindView(R.id.fragment_chat_item_profileContainer)
    LinearLayout profileContainer;
    @BindView(R.id.fragment_chat_item_profileImage)
    ImageView imageViewProfile;
    @BindView(R.id.fragment_chat_item_profileCoworker)
    ImageView imageViewCoworker;

    //MESSAGE CONTAINER
    @BindView(R.id.fragment_chat_item_messageContainer)
    LinearLayout messageContainer;
    //IMAGE SENDED CONTAINER
    @BindView(R.id.fragment_chat_item_imageCardView)
    CardView cardViewImageSent;
    @BindView(R.id.fragment_chat_item_ImageViewSend)
    ImageView imageViewSent;
    //TEXT MESSAGE CONTAINER
    @BindView(R.id.fragment_chat_item_messageTextContainer)
    LinearLayout textMessageContainer;
    // TEXT MESSAGE
    @BindView(R.id.fragment_chat_item_messageTextView)
    TextView messageText;
    @BindView(R.id.fragment_chat_item_dateTextView)
    TextView dateText;

    //FOR DATA
    private final int colorCurrentUser;
    private final int colorRemoteUser;
    private boolean isSender;

    public ChatViewHolder(@NonNull View itemView, boolean isSender) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        //this.isSender=isSender;

        //SETUP COLORS
        colorCurrentUser = ContextCompat.getColor(itemView.getContext(), R.color.colorAccent);
        colorRemoteUser = ContextCompat.getColor(itemView.getContext(), R.color.colorPrimary);
    }

    public void updateWithMessage(Message message, RequestManager glide) {
        //UPDATE MESSAGE
        messageText.setText(message.getMessage());
        messageText.setTextAlignment(isSender? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

        //UPDATE DATE
        if (message.getDateCreated() != null) dateText.setText(this.convertDateToHour((message.getDateCreated())));

        //Update COWORKER
        imageViewCoworker.setVisibility(message.getUserSender().getUserChat() ? View.VISIBLE : View.INVISIBLE);

        //UPDATE PROFILE PICTURE
        if (message.getUserSender().getUrlPicture() != null)
            glide.load(message.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);

        //UPDATE IMAGE SENT
        if(message.getUrlImage()!= null) {
            glide.load(message.getUrlImage())
                    .into(imageViewSent);
            imageViewSent.setVisibility(View.VISIBLE);
        } else {
            imageViewSent.setVisibility(View.GONE);
        }

        updateLayoutFromSenderType();

    }

    private void updateLayoutFromSenderType() {
        // UPDATE MESSAGE BUBBLE COLOR BACKGROUND
        ((GradientDrawable) textMessageContainer.getBackground()).setColor(isSender ? colorCurrentUser : colorRemoteUser);
        textMessageContainer.requestLayout();

        if(!isSender) {
            updateProfileContainer();
            updateMessageContainer();
        }

    }

    private void updateProfileContainer() {
        //UPDATE THE CONSTRAINT FOR THE PROFILE CONTAINER (PUSH IT TO THE LEFT FOR RECEIVER MESSAGE)
        ConstraintLayout.LayoutParams profileContainerLayoutParams =
                (ConstraintLayout.LayoutParams) profileContainer.getLayoutParams();
        profileContainerLayoutParams.endToEnd = ConstraintLayout.LayoutParams.UNSET;
        profileContainerLayoutParams.startToStart=ConstraintLayout.LayoutParams.PARENT_ID;
        profileContainer.requestLayout();
    }

    private void updateMessageContainer() {
        //UPDATE THE CONSTRAINT FOR THE MESSAGE CONTAINER (PUSH IT TO THE RIGHT OF THE PROFILE CONTAINER FOR RECEIVER MESSAGE)
        ConstraintLayout.LayoutParams messageContainerLayoutParams = (ConstraintLayout.LayoutParams) messageContainer.getLayoutParams();
        messageContainerLayoutParams.startToStart = ConstraintLayout.LayoutParams.UNSET;
        messageContainerLayoutParams.endToStart = ConstraintLayout.LayoutParams.UNSET;
        messageContainerLayoutParams.startToEnd = profileContainer.getId();
        messageContainerLayoutParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        messageContainerLayoutParams.horizontalBias = 0.0f;
        messageContainer.requestLayout();

        //UPDATE THE CONSTRAINT GRAVITY FOR THE TEXT OF THE MESSAGE (CONTENT+DATE) (ALIGN IT TO THE LEFT FOR RECEIVER MESSAGE)
        LinearLayout.LayoutParams messageTextLayoutParams = (LinearLayout.LayoutParams) textMessageContainer.getLayoutParams();
        messageTextLayoutParams.gravity = Gravity.START;
        textMessageContainer.requestLayout();

        LinearLayout.LayoutParams dateLayoutParams = (LinearLayout.LayoutParams) dateText.getLayoutParams();
        dateLayoutParams.gravity = Gravity.BOTTOM | Gravity.START;
        dateText.requestLayout();

    }

    private String convertDateToHour(Date date) {
        DateFormat dfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dfTime.format(date);
    }

    /*public void updateWithMessage(Message message, String currentUserId, RequestManager glide) {
        //CHECK IF CURRENT USER IS THE SENDER
        Boolean isCurrentUser = message.getUserSender().getUid().equals(currentUserId);

        //UPDATE MESSAGE TEXTVIEW
        this.messageText.setText(message.getMessage());
        this.messageText.setTextAlignment(isCurrentUser ? View.TEXT_ALIGNMENT_TEXT_END : View.TEXT_ALIGNMENT_TEXT_START);

        //UPDATE DATE TEXTVIEW
        if(message.getDateCreated() != null)
            this.dateText.setText(convertDateHours(message.getDateCreated()));

        //UPDATE IMAGEVIEW COWORKER
        this.imageViewCoworker.setVisibility(message.getUserSender().getUserChat() ? View.VISIBLE : View.INVISIBLE);

        //UPDATE PROFILE PICTURE IMAGEVIEW
        if(message.getUserSender().getUrlPicture() != null)
            glide.load(message.getUserSender().getUrlPicture())
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageViewProfile);

        //UPDATE IMAGE SENDED
        if(message.getUrlImage() != null) {
            glide.load(message.getUrlImage())
                    .into(imageViewSent);
            this.imageViewSent.setVisibility(View.VISIBLE);
        } else {
            this.imageViewSent.setVisibility(View.GONE);
        }

        //UPDATE MESSAGE BUBLE COLOR BACKGROUND
        textMessageContainer.setBackgroundColor(isCurrentUser ? colorCurrentUser : colorRemoteUser);

        //UPDATE ALL VIEWS ALIGNEMENT DEPENDING IS CURENT USER OR NOT
        this.updateDesignDependingUser(isCurrentUser);
    }

    private void updateDesignDependingUser(Boolean isSender) {

        // PROFILE CONTAINER
        RelativeLayout.LayoutParams paramsLayoutHeader = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutHeader.addRule(isSender ? RelativeLayout.ALIGN_PARENT_RIGHT : RelativeLayout.ALIGN_PARENT_LEFT);
        this.profileContainer.setLayoutParams(paramsLayoutHeader);

        // MESSAGE CONTAINER
        RelativeLayout.LayoutParams paramsLayoutContent = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsLayoutContent.addRule(isSender ? RelativeLayout.LEFT_OF : RelativeLayout.RIGHT_OF, R.id.fragment_chat_item_profileContainer);
        this.messageContainer.setLayoutParams(paramsLayoutContent);

        // CARDVIEW IMAGE SEND
        RelativeLayout.LayoutParams paramsImageView = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        paramsImageView.addRule(isSender ? RelativeLayout.ALIGN_LEFT : RelativeLayout.ALIGN_RIGHT, R.id.fragment_chat_item_messageTextContainer);
        this.cardViewImageSent.setLayoutParams(paramsImageView);

        this.rootView.requestLayout();


    }*/



}
