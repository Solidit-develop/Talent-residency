package com.example.servicesolidit.ConversationFlow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.Model.Responses.Conversatoins.ConversationDto;
import com.example.servicesolidit.R;

import java.util.List;

public class AdapterConversation extends RecyclerView.Adapter<AdapterConversation.ConversationViewHolder> {
    private List<ConversationDto> conversations;
    private OnConversationClickListener onConversationClickListener;

    public AdapterConversation(List<ConversationDto> conversations, OnConversationClickListener onConversationClickListener) {
        this.conversations = conversations;
        this.onConversationClickListener = onConversationClickListener;
    }

    public interface OnConversationClickListener {
        void onConversationClick(String conversationId);
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        ConversationDto conversation = conversations.get(position);
        holder.bind(conversation);
    }

    @Override
    public int getItemCount() {
        return conversations.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProfile;
        TextView txtName, txtLastMessage, txtTimeLastMessage;

        public ConversationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            txtName = itemView.findViewById(R.id.txtName);
            txtLastMessage = itemView.findViewById(R.id.txtLastMessage);
            txtTimeLastMessage = itemView.findViewById(R.id.txtTimeLastMessage);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    onConversationClickListener.onConversationClick(conversations.get(position).getId());
                }
            });
        }

        public void bind(ConversationDto conversation) {
            txtName.setText(conversation.getName());
            txtLastMessage.setText(conversation.getLastMessage());
            txtTimeLastMessage.setText(conversation.getTimeLastMessage());
            // Carga de imagen (puedes usar una biblioteca como Glide o Picasso)
            // Glide.with(itemView.getContext()).load(conversation.getImageUrl()).into(imgProfile);
        }
    }
}
