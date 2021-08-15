package com.example.myapplication.ui.team;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.TeamAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentGalleryBinding;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.Team;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment {

    private TeamViewModel teamViewModel;
    private FragmentGalleryBinding binding;
    RecyclerView recyclerView;
    List<Team> teams;
    TeamAdapter teamAdapter;
    DatabaseHelper db;
    Button todisable;
    ProgressBar pb;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        teamViewModel =
                new ViewModelProvider(this).get(TeamViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        db = new DatabaseHelper(this.getContext());
        pb = root.findViewById(R.id.teamspinner);
        pb.setVisibility(View.VISIBLE);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.teamrecycleview);
        teams = new ArrayList<>();

        if(this.isNetworkAvailable()){
            extractTeam();
        }
        else if(!this.isNetworkAvailable() || this.isNetworkAvailable() == null ) {
            ModeOfflineTeams();

        }
    }

    private void extractTeam() {

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, "https://backend-javaa-mbds272957.herokuapp.com/getallteam", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0 ; i < response.length();i++){
                    try {
                        JSONObject matchObject = response.getJSONObject(i);
                        Team team = new Team();
                        //Log.e("logo",String.valueOf(matchObject.getDouble("odds1")));
                        team.setName(matchObject.getString("nom"));
                        team.setTeam_id(matchObject.getInt("idTeam"));
                        team.setTag(matchObject.getString("tag").toString());
                        team.setLogo_url(matchObject.getString("logo").toString());
                        teams.add(team);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                teamAdapter = new TeamAdapter(getActivity().getApplicationContext(),teams);

                recyclerView.setAdapter(teamAdapter);
                db.initializeteamoffline();
                db.deleteteams();
                db.insertofflineteam(teams);
                pb.setVisibility(View.INVISIBLE);

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error",error.toString());

                    }
                }
        );
        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                25000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(objectRequest);
        requestQueue.add(objectRequest);
    }
    public Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void ModeOfflineTeams(){
        teams = db.getOfflineTeam();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        teamAdapter = new TeamAdapter(getActivity().getApplicationContext(),teams);
        recyclerView.setAdapter(teamAdapter);
        pb.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}