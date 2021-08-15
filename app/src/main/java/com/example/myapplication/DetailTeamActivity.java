package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.model.Match;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailTeamActivity extends AppCompatActivity {

    TextView nomdetail;TextView wins;TextView losses; TextView rating;TextView lastmatch;
    ImageView logo;
    String idteam;
    String uriopendota = "https://api.opendota.com/api/teams";
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_team);
        Intent intent = getIntent();
        idteam = intent.getStringExtra("id");
        nomdetail = findViewById(R.id.nomdetail);
        wins = findViewById(R.id.wins);
        losses = findViewById(R.id.losses);
        rating = findViewById(R.id.rating);
        lastmatch = findViewById(R.id.last_match_time);
        logo = findViewById(R.id.logodetail);
        pb = findViewById(R.id.detailteamspinner);
        nomdetail.setVisibility(View.INVISIBLE);
        wins.setVisibility(View.INVISIBLE);
        losses.setVisibility(View.INVISIBLE);
        rating.setVisibility(View.INVISIBLE);
        lastmatch.setVisibility(View.INVISIBLE);
        logo.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);

        getTeambyId();
    }

    private void getTeambyId() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, uriopendota + "/" + idteam, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    nomdetail.setText(response.getString("name"));
                    wins.setText(String.valueOf(response.getDouble("wins")));
                    losses.setText(String.valueOf(response.getDouble("losses")));
                    rating.setText(String.valueOf(response.getDouble("rating")));
                    lastmatch.setText(String.valueOf(response.getLong("last_match_time")));
                    nomdetail.setVisibility(View.VISIBLE);
                    wins.setVisibility(View.VISIBLE);
                    losses.setVisibility(View.VISIBLE);
                    rating.setVisibility(View.VISIBLE);
                    lastmatch.setVisibility(View.VISIBLE);
                    logo.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                    if(response.getString("logo_url")=="null"){

                        Picasso.get().load("https://seeklogo.com/images/D/dota-2-logo-A8CAC9B4C9-seeklogo.com.png").into(logo);

                    }
                    else {
                        Picasso.get().load(response.getString("logo_url")).into(logo);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());

                    }
                }
        );
        requestQueue.add(objectRequest);
        }
}