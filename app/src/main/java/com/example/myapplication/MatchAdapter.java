package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.Match;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Match> matchs;
    DatabaseHelper db;
    public MatchAdapter(Context ctx, List<Match> matches){
        this.inflater = LayoutInflater.from(ctx);
        this.matchs = matches;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = inflater.inflate(R.layout.customlistmatch,parent,false);
    db = new DatabaseHelper(view.getContext());
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatchAdapter.ViewHolder holder, int position) {
    if(db.getToken()==null){
        holder.button2.setEnabled(false);
    }

        holder.button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), DetailMatchActivity.class);
                intent.putExtra("id", String.valueOf(matchs.get(position).getIdMatchRivalry()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                v.getContext().startActivity(intent);

            }
        });
    holder.idmatch.setText(String.valueOf(matchs.get(position).getIdMatchRivalry()));
        holder.nomTeam.setText(matchs.get(position).getNomTeam1());

        holder.nomTeam2.setText(matchs.get(position).getNomTeam2());
        holder.tagTeam.setText("Odds : " + String.valueOf(matchs.get(position).getOdds1()));
        holder.tagTeam2.setText("Odds : " +String.valueOf(matchs.get(position).getOdds2()));
        if(matchs.get(position).getLogo1() == "null"  ){

        Picasso.get().load("https://seeklogo.com/images/D/dota-2-logo-A8CAC9B4C9-seeklogo.com.png").into(holder.logoteam);

    }



        else {
            try {
                int i = Integer.valueOf(matchs.get(position).getLogo1());
                Picasso.get().load(i).into(holder.logoteam);

            }
            catch (NumberFormatException e) {
                Picasso.get().load(matchs.get(position).getLogo1()).into(holder.logoteam);

            }



        }
        if(matchs.get(position).getLogo2()=="null" ){
            Picasso.get().load("https://seeklogo.com/images/D/dota-2-logo-A8CAC9B4C9-seeklogo.com.png").into(holder.logoteam2);

        }
        else {
            try {
                int i = Integer.valueOf(matchs.get(position).getLogo2());
                Picasso.get().load(i).into(holder.logoteam2);

            }
            catch (NumberFormatException e) {
                Picasso.get().load(matchs.get(position).getLogo2()).into(holder.logoteam2);

            }
        }

     //   Picasso.get().load(R.drawable.nointernet).into(holder.logoteam);
     //   Picasso.get().load(R.drawable.nointernet).into(holder.logoteam2);

    }

    @Override
    public int getItemCount() {
        return matchs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
TextView nomTeam;
TextView idmatch;

TextView tagTeam;
ImageView logoteam;
        TextView tagTeam2;
        TextView nomTeam2;
        ImageView logoteam2;
        Button button2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nomTeam = itemView.findViewById(R.id.nomteam);
            tagTeam = itemView.findViewById(R.id.tagTeam);
            logoteam = itemView.findViewById(R.id.imageView);
            nomTeam2 = itemView.findViewById(R.id.nomteam2);
            tagTeam2 = itemView.findViewById(R.id.tagTeam2);
            logoteam2 = itemView.findViewById(R.id.imageView2);
            button2 = itemView.findViewById(R.id.button2);
            idmatch = itemView.findViewById(R.id.idmatch);

        }
    }

}
