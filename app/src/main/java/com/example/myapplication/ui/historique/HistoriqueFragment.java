package com.example.myapplication.ui.historique;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Adapter.HistoriqueAdapter;
import com.example.myapplication.Adapter.TeamAdapter;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHistoriqueBinding;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.Historique;
import com.example.myapplication.model.Team;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoriqueFragment extends Fragment {

    private HistoriqueViewModel historiqueViewModel;
    private FragmentHistoriqueBinding binding;
    RecyclerView recyclerView;
    List<Historique> historiques;
    HistoriqueAdapter historiqueAdapter;
    DatabaseHelper db ;
    ProgressBar pb;
    private String userid;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        db = new DatabaseHelper(this.getContext());
        binding = FragmentHistoriqueBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        pb = root.findViewById(R.id.historiquespinner);
        pb.setVisibility(View.VISIBLE);

        return root;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = getActivity().findViewById(R.id.historiqueview);
        historiques = new ArrayList<>();
        getMyProfil(db.getToken());


    }
    public  void getMyProfil(String token){

        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, "https://backend-node-mbds272957.herokuapp.com/api/users", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

              userid = response.getString("_id");
                    extractHistoriques();
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
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("x-access-token",token);

                return params;
            }
        };

        requestQueue.add(objectRequest);
    }

    private void extractHistoriques() {
        SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest objectRequest = new JsonArrayRequest(Request.Method.GET, "https://backend-node-mbds272957.herokuapp.com/api/historiques" + "/" + userid, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0 ; i < response.length();i++){
                    try {
                        JSONObject matchObject = response.getJSONObject(i);
                        Historique h = new Historique();
                        h.setType(matchObject.getString("type"));
                        if(matchObject.getString("description") == null){
                            h.setDescription("aucune déscription");
                        }
                        else {
                            h.setDescription(matchObject.getString("description"));

                        }
                        h.setMontant(matchObject.getDouble("montant"));
                        h.setDatehistorique(formatter1.parse(matchObject.getString("datehistorique").replaceAll("T00:00:00.000Z","")));
                        historiques.add(h);
                    } catch (JSONException | ParseException e) {
                        e.printStackTrace();
                    }
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
                historiqueAdapter = new HistoriqueAdapter(getActivity().getApplicationContext(),historiques);
                recyclerView.setAdapter(historiqueAdapter);
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
        requestQueue.add(objectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}