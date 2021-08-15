package com.example.myapplication.Adapter;

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

import com.example.myapplication.DetailTeamActivity;
import com.example.myapplication.R;
import com.example.myapplication.model.Match;
import com.example.myapplication.model.Team;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {

    LayoutInflater inflater;
    List<Team> teams;

    public TeamAdapter(Context ctx, List<Team> teams){
        this.inflater = LayoutInflater.from(ctx);
        this.teams = teams;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customlistteams,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeamAdapter.ViewHolder holder, int position) {
        holder.detail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

              Intent intent = new Intent(v.getContext(), DetailTeamActivity.class);
              intent.putExtra("id", String.valueOf(teams.get(position).getTeam_id()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               v.getContext().startActivity(intent);

            }
        });

        holder.nom.setText(teams.get(position).getName());

        holder.tag.setText(String.valueOf(teams.get(position).getTag()));

        if(teams.get(position).getLogo_url() == "null"  ){
            Picasso.get().load("https://seeklogo.com/images/D/dota-2-logo-A8CAC9B4C9-seeklogo.com.png").into(holder.logo);

        }
        else {
            try {
                int i = Integer.valueOf(teams.get(position).getLogo_url());
                Picasso.get().load(i).into(holder.logo);

            }
            catch (NumberFormatException e) {
                Picasso.get().load(teams.get(position).getLogo_url()).into(holder.logo);

            }



        }



    }

    @Override
    public int getItemCount() {
        return teams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nom;
        TextView tag;
        ImageView logo;

        Button detail;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nom = itemView.findViewById(R.id.nom);
            tag = itemView.findViewById(R.id.tag);
            logo = itemView.findViewById(R.id.logo);
            detail = itemView.findViewById(R.id.detail);

            if(this.isNetworkAvailable(itemView)){
            }
            else if(!this.isNetworkAvailable(itemView) || this.isNetworkAvailable(itemView) == null ) {
                detail.setEnabled(false);

            }
        }
        public Boolean isNetworkAvailable(View v){
            ConnectivityManager connectivityManager = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null;
        }
    }

}
