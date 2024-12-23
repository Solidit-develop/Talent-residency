package com.example.servicesolidit.MessageFlow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.Utils.Dtos.Responses.Messages.MessageDto;
import com.example.servicesolidit.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_RECEIVED = 2;
    private static final int VIEW_TYPE_SENT = 1;

    private static  final String TAG_MESSAGE_ADAPTER = "TAG MESSAGE ADAPTER";

    private List<MessageDto> messages;
    private int userId;

    public MessageAdapter(List<MessageDto> messages, int userId) {
        this.messages = messages;
        this.userId = userId;
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("MessageAdapter", "Pintar como enviado: " + messages.get(position).isSent());
        return messages.get(position).isSent() ? VIEW_TYPE_RECEIVED : VIEW_TYPE_SENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            if (viewType == VIEW_TYPE_RECEIVED) {
                Log.i("MessageAdapter", "ocvh: True");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
                return new ReceivedMessageViewHolder(view);
            } else {
                Log.i("MessageAdapter", "ocvh: False");
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_messsage_send, parent, false);
                return new SentMessageViewHolder(view);
            }
        }catch (Exception e){
            Log.i("MessageAdapter", "Error on Catch: "+ e.getMessage());
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageDto message = messages.get(position);
        if (holder instanceof ReceivedMessageViewHolder) {
            ((ReceivedMessageViewHolder) holder).bind(message);
        } else {
            ((SentMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        void bind(MessageDto message) {
            messageText.setText(message.getContect());
        }
    }

    static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        SentMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
        }

        void bind(MessageDto message) {
            messageText.setText(message.getContect());
        }
    }

}
