package com.example.servicesolidit.Search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.FeedFlow.CardAdapter;
import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Constants;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderDto;
import com.example.servicesolidit.Utils.Dtos.Responses.SearchProvider.SearchProviderResponseDto;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchProviderAdapter extends RecyclerView.Adapter<SearchProviderAdapter.ProviderFoundViewHolder> {

    private ArrayList<SearchProviderDto> providersFound;
    private CardAdapter.OnCardClickListener listener;
    public SearchProviderAdapter (ArrayList<SearchProviderDto> providersFound, CardAdapter.OnCardClickListener listener){
        this.providersFound = providersFound;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SearchProviderAdapter.ProviderFoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_view, parent, false);
        return new ProviderFoundViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchProviderAdapter.ProviderFoundViewHolder holder, int position) {
        SearchProviderDto item = this.providersFound.get(position);
        String urlImage = !item.getRelacionImagen().isEmpty() ? item.getRelacionImagen().get(0).getUrlLocation() : "no-located";
        Picasso.get()
                .load(Constants.BASE_URL + "images/print/" + urlImage)
                .placeholder(R.drawable.load)
                .error(R.drawable.lost)
                .into(holder.imgView);

        holder.workshopName.setText(item.getProvedor().getWorkshopName());
        holder.workshopPhone.setText(item.getProvedor().getWorkshopPhoneNumber());
        holder.skills.setText(item.getProvedor().getSkills().size() > 0 ? item.getProvedor().getSkills().get(0).getName() : "Negocio de confianza");
        holder.itemView.setOnClickListener(v->{
            listener.onCardClick(item.getProvedor().getIdProvider());
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
