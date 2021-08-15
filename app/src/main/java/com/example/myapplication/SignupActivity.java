package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.service.AuthAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignupActivity extends AppCompatActivity {

    Button inscription;
    String URL = "https://backend-node-mbds272957.herokuapp.com/api/users";
    EditText user ;
    EditText password;
    EditText nom;
    EditText prenom;
    DatabaseHelper db;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
      db = new DatabaseHelper(this);
        inscription = findViewById(R.id.inscription);
        user = findViewById(R.id.pseudosigninput);
        password = findViewById(R.id.passwordsigninput);
        nom = findViewById(R.id.nominput);
        prenom = findViewById(R.id.prenominput);
        pb = findViewById(R.id.inscriptionspinner);
        pb.setVisibility(View.INVISIBLE);
        inscription.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                inscription.setVisibility(View.INVISIBLE);
                pb.setVisibility(View.VISIBLE);

                JSONObject jsonBody = new JSONObject();
                try {
                    jsonBody.put("sequence", 1);
                    jsonBody.put("nom", nom.getText().toString());
                    jsonBody.put("prenom", prenom.getText().toString());
                    jsonBody.put("username", user.getText().toString());
                    jsonBody.put("password", password.getText().toString());
                    final String requestBody = jsonBody.toString();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast t = Toast.makeText(getApplicationContext(),"Inscription Réussi",Toast.LENGTH_LONG);
                        t.show();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(response);
                            String regex = "/['\"]+/g";
                            String tokenlite = obj.getString("token").replaceAll(regex,"");
                            db.insertToken(tokenlite);
                            AuthAPI.getCurrentUser(getApplicationContext(),db.getToken());
                            Intent i = new Intent(SignupActivity.this, MenuActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if(db.getToken()!=null){
                            db.deleteToken();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       Toast t = Toast.makeText(getApplicationContext(),"Oups ce pseudo est déja pris",Toast.LENGTH_LONG);
                        inscription.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.INVISIBLE);
                        t.show();
                    }
                })
                {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    // this is the relevant method
                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return jsonBody.toString() == null ? null : jsonBody.toString().getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonBody.toString(), "utf-8");
                            return null;
                        }
                    }
                };
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);
            }
        }) ;

        // A null listener allows the button to dismiss the dialog and take no further action.



    }
}