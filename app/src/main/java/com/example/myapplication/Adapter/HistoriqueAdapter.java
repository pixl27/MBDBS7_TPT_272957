package com.example.myapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.DetailTeamActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Historique;
import com.example.myapplication.model.Team;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoriqueAdapter extends RecyclerView.Adapter<HistoriqueAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Historique> historiques;

    public HistoriqueAdapter(Context ctx, List<Historique> historiques){
        this.inflater = LayoutInflater.from(ctx);
        this.historiques = historiques;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customtablehistorique,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoriqueAdapter.ViewHolder holder, int position) {


        holder.description.setText(historiques.get(position).getDescription());

        if(historiques.get(position).getType().compareTo("debit") == 0){


            holder.montant.setText(String.valueOf(historiques.get(position).getMontant()));


        }
        else {
            holder.montant.setText("-" + historiques.get(position).getMontant());

        }
        holder.date.setText(historiques.get(position).getDatehistorique().toString());






    }

    @Override
    public int getItemCount() {
        return historiques.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView description;
        TextView montant;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.descriptionh);
            montant = itemView.findViewById(R.id.montanth);
            date = itemView.findViewById(R.id.dateh);


        }
    }
}
