package com.example.myapplication.service;

import android.content.Context;
import android.provider.ContactsContract;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.model.DatabaseHelper;
import com.example.myapplication.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AuthAPI {
   static String URL = "https://backend-node-mbds272957.herokuapp.com/api/users";
   public static User me   ;
    public static void getCurrentUser(Context ctx , String token){
        me = new User();
        User toreturn = new User();
        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    me.set_id(response.getString("_id"));
                    me.setId(response.getInt("id"));
                    me.setUsername(response.getString("username"));
                    me.setNom(response.getString("nom"));
                    me.setPrenom(response.getString("prenom"));
                    me.setIdrole(response.getInt("idrole"));
                    me.setSolde(response.getDouble("solde"));
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
