package com.example.servicesolidit.HouseFlow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder>{

    private List<CardModel> cardList;
    private Context context;

    public CardAdapter(List<CardModel> cardList, Context context){
        this.cardList = cardList;
        this.context = context;
    }

    @NonNull
    @Override
    public CardAdapter.CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent,false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.CardViewHolder holder, int position) {
        CardModel card= cardList.get(position);

        Picasso.get()
                .load(card.getImageUrl())
                .placeholder(R.drawable.load)
                .error(R.drawable.lost)
                .into(holder.imageView);

        holder.nameBussines.setText(card.getNameBussines());
        holder.description.setText(card.getDescription());
        holder.location.setText(card.getLocation());

        holder.description.setSelected(true);
        holder.location.setSelected(true);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TEST_CARDADAPTER","clickaste aqui: " + card.getIdProvider());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameBussines;
        TextView description;
        TextView location;

        public CardViewHolder(@NonNull View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.img_target);
            nameBussines =itemView.findViewById(R.id.txt_name_bussines);
            description = itemView.findViewById(R.id.txt_description);
            location = itemView.findViewById(R.id.txt_location);
        }
    }
}
