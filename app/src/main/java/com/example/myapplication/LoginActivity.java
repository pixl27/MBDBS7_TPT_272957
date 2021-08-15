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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.User;
import com.example.myapplication.service.AuthAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    Button connexion;
    String URL = "https://backend-node-mbds272957.herokuapp.com/api/user";
    EditText user ;
    EditText password;
    DatabaseHelper db;
    AuthAPI loginservice;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    connexion = findViewById(R.id.connexion);
    user = findViewById(R.id.pseudoinput);
    pb = findViewById(R.id.connexionspinner);
        pb.setVisibility(View.INVISIBLE);

        password = findViewById(R.id.passwordinput);
    db = new DatabaseHelper(this);
        loginservice = new AuthAPI();

        connexion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                connexion.setVisibility(View.INVISIBLE);
                                JSONObject jsonBody = new JSONObject();
                                try {
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
                                        try {
                                            JSONObject obj = new JSONObject(response);
                                            String regex = "/['\"]+/g";
                                            String tokenlite = obj.getString("token").replaceAll(regex,"");
                                            if(db.getToken()!=null){
                                                db.deleteToken();
                                            }
                                            db.insertToken(tokenlite);


                                            JSONObject jsonBody = new JSONObject();
                                            try {
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
                                                    try {
                                                        JSONObject obj = new JSONObject(response);
                                                        String regex = "/['\"]+/g";
                                                        String tokenlite = obj.getString("token").replaceAll(regex,"");
                                                        if(db.getToken()!=null){
                                                            db.deleteToken();
                                                        }
                                                        db.insertToken(tokenlite);

                                                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                                        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL + "s", null, new Response.Listener<JSONObject>() {
                                                            @Override
                                                            public void onResponse(JSONObject response) {

                                                                try {

                                                                    db.initializeiduser();
                                                                    db.deleteuser();
                                                                    db.insertIdUser(response.getString("_id"),String.valueOf(response.getDouble("solde")));

                                                                    Intent i = new Intent(LoginActivity.this, MenuActivity.class);
                                                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                                    startActivity(i);
                                                                    finish();


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
                                                                params.put("x-access-token",db.getToken());

                                                                return params;
                                                            }
                                                        };
                                                        objectRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                                0,
                                                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                        requestQueue.add(objectRequest);



                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {
                                                    Log.e("VOLLEY","Connéxion Echouer");
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




                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        pb.setVisibility(View.INVISIBLE);
                                        connexion.setVisibility(View.VISIBLE);
                                        Toast t = Toast.makeText(getApplicationContext(),"Erreur dans le mot de passe ou pseudo",Toast.LENGTH_LONG);
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
    public void gotoinscription(View view) {

        Intent intent = new Intent(view.getContext(), SignupActivity.class);
        view.getContext().startActivity(intent);

    }
    public  void getMyProfil(String token){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL + "s", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    Log.e("User has been insert",response.getString("_id"));
                    db.deletetableuser();
                    db.initializeiduser();
                    db.deleteuser();
                    db.insertIdUser(response.getString("_id"),String.valueOf(response.getDouble("solde")));
                    Log.e("User Solde",db.getIdUser()[1]);
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