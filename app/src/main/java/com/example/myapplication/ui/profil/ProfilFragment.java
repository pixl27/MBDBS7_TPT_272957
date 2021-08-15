package com.example.myapplication.ui.profil;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.databinding.FragmentHomeBinding;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.User;
import com.example.myapplication.service.AuthAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class ProfilFragment extends AppCompatActivity {
    private DatabaseHelper db;
    String URL = "https://backend-node-mbds272957.herokuapp.com/api/users";
    private FragmentHomeBinding binding;
    TextView nomfirst ; TextView nomprofil; TextView prenomprofil; TextView pseudoprofil; TextView soldeprofil;
    ProgressBar pb;
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profil);
        pb = findViewById(R.id.profilspinner);
        findViewById(R.id.profill).setVisibility(View.INVISIBLE);
        findViewById(R.id.rellay1).setVisibility(View.INVISIBLE);
        findViewById(R.id.linlay1).setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        db = new DatabaseHelper(this);

        nomfirst =findViewById(R.id.nommonprofil);
        nomprofil = findViewById(R.id.nompr);
        prenomprofil = findViewById(R.id.prenompr);
        pseudoprofil = findViewById(R.id.pseudopr);
        soldeprofil = findViewById(R.id.soldepr);
        if(db.getToken()!=null){
           getMyProfil(db.getToken());
        }



    }

    public  void getMyProfil(String token){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    pseudoprofil.setText(response.getString("username"));
                    nomfirst.setText(response.getString("nom") + " " + response.getString("prenom") );
                    nomprofil.setText(response.getString("nom"));
                    prenomprofil.setText(response.getString("prenom"));
                    soldeprofil.setText(String.valueOf(response.getDouble("solde")));
                    findViewById(R.id.profill).setVisibility(View.VISIBLE);
                    findViewById(R.id.rellay1).setVisibility(View.VISIBLE);
                    findViewById(R.id.linlay1).setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
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




}