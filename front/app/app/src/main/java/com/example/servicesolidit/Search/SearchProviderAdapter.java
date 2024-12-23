package com.example.servicesolidit.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderResponseDto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchProviderAdapter extends RecyclerView.Adapter<SearchProviderAdapter.ProviderFoundViewHolder> {

    private ArrayList<SearchProviderResponseDto> providersFound;

    public SearchProviderAdapter (ArrayList<SearchProviderResponseDto> providersFound){
        this.providersFound = providersFound;
    }

    @NonNull
    @Override
    public SearchProviderAdapter.ProviderFoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_view, parent, false);
        return new ProviderFoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProviderAdapter.ProviderFoundViewHolder holder, int position) {
        SearchProviderResponseDto item = this.providersFound.get(position);

        Picasso.get()
                .load("itemShouldHadUrl")
                .placeholder(R.drawable.load)
                .error(R.drawable.lost)
                .into(holder.imgView);

        holder.workshopName.setText(item.getWorkshopName());
        holder.workshopPhone.setText(item.getWorkshopPhoneNumber());
        holder.skills.setText("Skillssssss");
        holder.itemView.setOnClickListener(v->{
            Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return this.providersFound.size();
    }

    public static class ProviderFoundViewHolder extends RecyclerView.ViewHolder{

        TextView workshopName;
        TextView workshopPhone;
        TextView skills;
        ImageView imgView;

        public ProviderFoundViewHolder(@NonNull View itemView) {
            super(itemView);
            workshopName = itemView.findViewById(R.id.tvWorkshopNameFound);
            workshopPhone = itemView.findViewById(R.id.tvWorkshopPhoneFound);
            skills = itemView.findViewById(R.id.skillsFound);
            imgView = itemView.findViewById(R.id.imgSearchView);
        }
    }
}
