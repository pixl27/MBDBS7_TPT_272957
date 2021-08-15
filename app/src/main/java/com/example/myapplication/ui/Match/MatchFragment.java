package com.example.myapplication.ui.Match;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MatchAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentFirstBinding;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.Match;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MatchFragment extends Fragment {

    private FragmentFirstBinding binding;
    RecyclerView recyclerView;
    List<Match> matches;
    MatchAdapter matchAdapter;
    DatabaseHelper db;
    ProgressBar pb;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        pb = binding.getRoot().findViewById(R.id.betspinner);
        pb.setVisibility(View.VISIBLE);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = new DatabaseHelper(this.getContext());
        recyclerView = getActivity().findViewById(R.id.recyclevue);
        matches = new ArrayList<>();
        if(this.isNetworkAvailable()){
            extractTeam();
        }
        else if(!this.isNetworkAvailable() || this.isNetworkAvailable() == null ) {
            ModeOffline();

        }
       // extractTeam();

    }
    public Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void ModeOffline(){
        matches = db.getOfflineMatch();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        matchAdapter = new MatchAdapter(getActivity().getApplicationContext(),matches);
        recyclerView.setAdapter(matchAdapter);
        pb.setVisibility(View.INVISIBLE);

    }
    private void extractTeam() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, "https://backend-javaa-mbds272957.herokuapp.com/getallmatchtest", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0 ; i < response.length();i++){
                    try {
                        JSONObject matchObject = response.getJSONObject(i);
                        Match match = new Match();
                        match.setIdMatchRivalry(matchObject.getInt("idMatchRivalry"));

                        match.setNomTeam1(matchObject.getString("nomTeam1").toString());
                        match.setOdds1(matchObject.getDouble("odds1"));
                        match.setLogo1(matchObject.getString("logo1").toString());
                        match.setNomTeam2(matchObject.getString("nomTeam2").toString());
                        match.setOdds2(matchObject.getDouble("odds2"));
                        match.setLogo2(matchObject.getString("logo2").toString());
                        matches.add(match);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                matchAdapter = new MatchAdapter(getActivity().getApplicationContext(),matches);
                recyclerView.setAdapter(matchAdapter);
                pb.setVisibility(View.INVISIBLE);

                //db.deletetablematchs();
                db.initializeoffline();
                db.deletematch();
                db.insertofflinematch(matches);
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

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}