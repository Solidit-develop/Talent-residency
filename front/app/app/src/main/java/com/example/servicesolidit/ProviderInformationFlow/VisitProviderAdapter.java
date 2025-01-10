package com.example.servicesolidit.ProviderInformationFlow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servicesolidit.R;
import com.example.servicesolidit.Utils.Dtos.Responses.Comments.CommentsDto;

import java.util.ArrayList;

public class VisitProviderAdapter extends RecyclerView.Adapter<VisitProviderAdapter.ViewHolder> {

    private ArrayList<CommentsDto> comentarios;

    public VisitProviderAdapter(ArrayList<CommentsDto> comentarios){
        this.comentarios =  comentarios;
    }

    @NonNull
    @Override
    public VisitProviderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitProviderAdapter.ViewHolder holder, int position) {
        CommentsDto comment = comentarios.get(position);

        holder.comentario.setText(comment.getComentario());
    }

    @Override
    public int getItemCount() {
        return comentarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView comentario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            comentario = itemView.findViewById(R.id.item_commentario);
        }
    }
}
