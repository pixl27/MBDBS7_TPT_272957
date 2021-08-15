package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class DetailMatchActivity extends AppCompatActivity {

    String idmatch;
    TextView nomEquipe1;
    TextView nomEquipe2;
    TextView odds1;
    TextView odds2;
    TextView tournois;
    TextView date;
    TextView textView7;
    TextView textView9;
    TextView corner1;
    TextView corner2;
    TextView possession1;
    TextView possession2;
    TextView odd1_map2;
    TextView odd2_map2;
    TextView odd1_map3;
    TextView odd2_map3;
    TextView odd1_fb_map1;
    TextView odd2_fb_map1;
    TextView odd1_fb_map2;
    TextView odd2_fb_map2;
    TextView odd1_fb_map3;
    TextView odd2_fb_map3;
    ImageView logo1;
    ImageView logo2;
    Button bet1;
    Button bet2;
    Button bet3;
    Button bet4;
    Button bet5;
    Button bet6;
    Button bet7;
    Button bet8;
    Button bet9;
    Button bet10;
    Button bet11;
    Button bet12;
    Button bet13;
    Button bet14;
    private String idUser = "";
    private int idMatch;
    private String type;
    private int idTeamParier;
    private Double odds;
    ProgressBar pb;
    DatabaseHelper db;
    String URL = "https://backend-javaa-mbds272957.herokuapp.com/parier";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_match);
        db = new DatabaseHelper(this);
        Intent intent = getIntent();
        idmatch = intent.getStringExtra("id");
        bet1 = (Button) findViewById(R.id.bet1);
        bet2 = (Button) findViewById(R.id.bet2);
        bet3 = (Button) findViewById(R.id.bet3);
        bet4 = (Button) findViewById(R.id.bet4);
        bet5 = (Button) findViewById(R.id.bet5);
        bet6 = (Button) findViewById(R.id.bet6);
        bet7 = (Button) findViewById(R.id.bet7);
        bet8 = (Button) findViewById(R.id.bet8);
        bet9 = (Button) findViewById(R.id.bet9);
        bet10 = (Button) findViewById(R.id.bet10);
        bet11 = (Button) findViewById(R.id.bet11);
        bet12 = (Button) findViewById(R.id.bet12);
        bet13 = (Button) findViewById(R.id.bet13);
        bet14 = (Button) findViewById(R.id.bet14);
pb = findViewById(R.id.detailmatchspinner);
        findViewById(R.id.cardView).setVisibility(View.INVISIBLE);
        findViewById(R.id.cardView2).setVisibility(View.INVISIBLE);
        findViewById(R.id.score1).setVisibility(View.INVISIBLE);
        findViewById(R.id.score2).setVisibility(View.INVISIBLE);
        findViewById(R.id.lieu).setVisibility(View.INVISIBLE);
        findViewById(R.id.nomEquipe1).setVisibility(View.INVISIBLE);
        findViewById(R.id.nomEquipe2).setVisibility(View.INVISIBLE);
        findViewById(R.id.etat).setVisibility(View.INVISIBLE);
        findViewById(R.id.textView8).setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        extraMatch();


    }

    private void setdefaultattribute(int idMatch, String type, int idTeamParier, Double odds) {
        this.setIdUser("60df00683e1a0b0015485ebf");
        this.setIdMatch(idMatch);
        this.setType(type);
        this.setIdTeamParier(idTeamParier);
        this.setOdds(odds);
    }

    private void extraMatch() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, "https://backend-javaa-mbds272957.herokuapp.com/getmatchbyIdRivalry?idRivalry=" + this.idmatch, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                nomEquipe1 = (TextView) findViewById(R.id.nomEquipe1);
                nomEquipe2 = (TextView) findViewById(R.id.nomEquipe2);
                textView7 = (TextView) findViewById(R.id.textView7);
                textView9 = (TextView) findViewById(R.id.textView9);
                odd1_map2 = (TextView) findViewById(R.id.odd1_map2);
                odd2_map2 = (TextView) findViewById(R.id.odd2_map2);
                odd1_map3 = (TextView) findViewById(R.id.odd1_map3);
                odd2_map3 = (TextView) findViewById(R.id.odd2_map3);
                odd1_fb_map1 = (TextView) findViewById(R.id.odd1_fb_map1);
                odd2_fb_map1 = (TextView) findViewById(R.id.odd2_fb_map1);
                odd1_fb_map2 = (TextView) findViewById(R.id.odd1_fb_map2);
                odd2_fb_map2 = (TextView) findViewById(R.id.odd2_fb_map2);
                odd1_fb_map3 = (TextView) findViewById(R.id.odd1_fb_map3);
                odd2_fb_map3 = (TextView) findViewById(R.id.odd2_fb_map3);
                corner1 = (TextView) findViewById(R.id.corner1);
                corner2 = (TextView) findViewById(R.id.corner2);
                possession1 = (TextView) findViewById(R.id.possession1);
                possession2 = (TextView) findViewById(R.id.possession2);
                tournois = (TextView) findViewById(R.id.lieu);
                date = (TextView) findViewById(R.id.etat);
                odds1 = (TextView) findViewById(R.id.score1);
                odds2 = (TextView) findViewById(R.id.score2);
                logo1 = (ImageView) findViewById(R.id.imageView9);
                logo2 = (ImageView) findViewById(R.id.imageView6);
                //here begin
                try {
                    nomEquipe1.setText(response.getString("nomTeam1"));
                    nomEquipe2.setText(response.getString("nomTeam2"));
                    textView7.setText(response.getString("nomTeam1"));
                    textView9.setText(response.getString("nomTeam2"));
                    corner1.setText(String.valueOf(response.getDouble("odds1")));
                    corner2.setText(String.valueOf(response.getDouble("odds2")));
                    possession1.setText(String.valueOf(response.getDouble("odd1_map1")));
                    possession2.setText(String.valueOf(response.getDouble("odd2_map1")));
                    tournois.setText(response.getString("tournois"));
                    date.setText(response.getString("datematch"));
                    odds1.setText(String.valueOf(response.getDouble("odds1")));
                    odds2.setText(String.valueOf(response.getDouble("odds2")));
                    odd1_map2.setText(String.valueOf(response.getDouble("odd1_map2")));
                    odd2_map2.setText(String.valueOf(response.getDouble("odd2_map2")));
                    odd1_map3.setText(String.valueOf(response.getDouble("odd1_map3")));
                    odd2_map3.setText(String.valueOf(response.getDouble("odd2_map3")));
                    odd1_fb_map1.setText(String.valueOf(response.getDouble("odd1_fb_map1")));
                    odd2_fb_map1.setText(String.valueOf(response.getDouble("odd2_fb_map1")));
                    odd1_fb_map2.setText(String.valueOf(response.getDouble("odd1_fb_map2")));
                    odd2_fb_map2.setText(String.valueOf(response.getDouble("odd2_fb_map2")));
                    odd1_fb_map3.setText(String.valueOf(response.getDouble("odd1_fb_map3")));
                    odd2_fb_map3.setText(String.valueOf(response.getDouble("odd2_fb_map3")));
                    findViewById(R.id.cardView).setVisibility(View.VISIBLE);
                    findViewById(R.id.cardView2).setVisibility(View.VISIBLE);
                    findViewById(R.id.score1).setVisibility(View.VISIBLE);
                    findViewById(R.id.score2).setVisibility(View.VISIBLE);
                    findViewById(R.id.lieu).setVisibility(View.VISIBLE);
                    findViewById(R.id.nomEquipe1).setVisibility(View.VISIBLE);
                    findViewById(R.id.nomEquipe2).setVisibility(View.VISIBLE);
                    findViewById(R.id.etat).setVisibility(View.VISIBLE);
                    findViewById(R.id.textView8).setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                    if(Double.parseDouble(odds1.getText().toString()) == 0 && Double.parseDouble(odds2.getText().toString()) == 0){
                        bet1.setEnabled(false);
                        bet2.setEnabled(false);
                    }
                    else {
                        bet1.setEnabled(true);
                        bet2.setEnabled(true);
                    }
                    if(Double.parseDouble(possession1.getText().toString()) == 0 && Double.parseDouble(possession2.getText().toString()) == 0){
                        bet3.setEnabled(false);
                        bet4.setEnabled(false);
                    }
                    else {
                        bet3.setEnabled(true);
                        bet4.setEnabled(true);
                    }
                    if(Double.parseDouble(odd1_map2.getText().toString()) == 0 && Double.parseDouble(odd2_map2.getText().toString()) == 0){
                        bet5.setEnabled(false);
                        bet6.setEnabled(false);
                    }
                    else {
                        bet5.setEnabled(true);
                        bet6.setEnabled(true);
                    }
                    if(Double.parseDouble(odd1_map3.getText().toString()) == 0 && Double.parseDouble(odd2_map3.getText().toString()) == 0){
                        bet7.setEnabled(false);
                        bet8.setEnabled(false);
                    }
                    else {
                        bet7.setEnabled(true);
                        bet8.setEnabled(true);
                    }
                    if(Double.parseDouble( odd1_fb_map1.getText().toString()) == 0 && Double.parseDouble( odd2_fb_map1.getText().toString()) == 0){
                        bet9.setEnabled(false);
                        bet10.setEnabled(false);
                    }
                    else {
                        bet9.setEnabled(true);
                        bet10.setEnabled(true);
                    }
                    if(Double.parseDouble( odd1_fb_map2.getText().toString()) == 0 && Double.parseDouble( odd2_fb_map2.getText().toString()) == 0){
                        bet11.setEnabled(false);
                        bet12.setEnabled(false);
                    }
                    else {
                        bet11.setEnabled(true);
                        bet12.setEnabled(true);
                    }
                    if(Double.parseDouble( odd1_fb_map3.getText().toString()) == 0 && Double.parseDouble( odd2_fb_map3.getText().toString()) == 0){
                        bet13.setEnabled(false);
                        bet14.setEnabled(false);
                    }
                    else {
                        bet13.setEnabled(true);
                        bet14.setEnabled(true);
                    }
// Listener Boutton
                    bet1.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {

                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_overall", response.getInt("idTeam1"), response.getDouble("odds1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {

                                                ShowToast("Veuillez patienter...");
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        ShowToast("Votre Paris a bien été enregistrer");
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        ShowToast("Ce match n'est pas encore disponilbe ou est déja terminer");
                                                    }
                                                }) {
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
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });

                    bet2.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_overall", response.getInt("idTeam2"), response.getDouble("odds2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                ShowToast("Veuillez patienter...");

                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        ShowToast("Votre paris a bien été enregistrer");

                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }

                    });

                    bet3.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_1", response.getInt("idTeam1"), response.getDouble("odd1_map1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                ShowToast("Veuillez patienter...");
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        ShowToast("Votre paris a bien été enregistrer");                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });

                    bet4.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_1", response.getInt("idTeam2"), response.getDouble("odd2_map1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                ShowToast("Veuillez patienter...");
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        ShowToast("Votre paris a bien été enregistrer");
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });

                    bet5.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_2", response.getInt("idTeam1"), response.getDouble("odd1_map2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                ShowToast("Veuillez patienter...");
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        ShowToast("Votre paris a bien été enregistrer");
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });

                    bet6.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_2", response.getInt("idTeam2"), response.getDouble("odd2_map2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                ShowToast("Veuillez patienter...");
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        ShowToast("Votre paris a bien été enregistrer");
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet7.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_3", response.getInt("idTeam1"), response.getDouble("odd1_map3"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet8.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_3", response.getInt("idTeam2"), response.getDouble("odd2_map3"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet9.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_1_fb", response.getInt("idTeam1"), response.getDouble("odd1_fb_map1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet10.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_1_fb", response.getInt("idTeam2"), response.getDouble("odd2_fb_map1"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet11.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_2_fb", response.getInt("idTeam1"), response.getDouble("odd1_fb_map2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet12.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_2_fb", response.getInt("idTeam2"), response.getDouble("odd2_fb_map2"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet13.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_3_fb", response.getInt("idTeam1"), response.getDouble("odd1_fb_map3"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    bet14.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                setdefaultattribute(response.getInt("idMatchRivalry"), "map_3_fb", response.getInt("idTeam2"), response.getDouble("odd2_fb_map3"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            final EditText input = new EditText(v.getContext());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);

                            new AlertDialog.Builder(v.getContext())
                                    .setTitle("Parier")
                                    .setMessage("Entrer Montant")
                                    .setView(input)
                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (!isNumeric(input.getText().toString()) || Double.valueOf(input.getText().toString()) <= 0 || Double.valueOf(input.getText().toString()) > Double.valueOf(db.getIdUser()[1])) {
                                                Toast toast = Toast.makeText(getApplicationContext(), "Veuillez entrer un Solde valide et qui ne dépasse pas votre solde", Toast.LENGTH_LONG);
                                                toast.show();
                                            } else {
                                                JSONObject jsonBody = new JSONObject();
                                                try {
                                                    jsonBody.put("idUser", getIdUser());
                                                    jsonBody.put("idMatch", getIdMatch());
                                                    jsonBody.put("type", getType());
                                                    jsonBody.put("idTeamParier", getIdTeamParier());
                                                    jsonBody.put("montant", Double.valueOf(input.getText().toString()));
                                                    jsonBody.put("odds", Double.valueOf(getOdds()));
                                                    final String requestBody = jsonBody.toString();


                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                                                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        Log.i("VOLLEY", response);
                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {
                                                        Log.e("VOLLEY", error.toString());
                                                    }
                                                }) {
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
                                                // Log.e("montant" , input.getText().toString());
                                                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                                                        0,
                                                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                                requestQueue.add(stringRequest);
                                            }
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    .setNegativeButton(android.R.string.no, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();

                        }
                    });
                    if (response.getString("logo1") == "null") {

                        Picasso.get().load("https://seeklogo.com/images/D/dota-2-logo-A8CAC9B4C9-seeklogo.com.png").into(logo1);

                    } else {
                        Picasso.get().load(response.getString("logo1")).into(logo1);

                    }
                    if (response.getString("logo2") == "null") {
                        Picasso.get().load("https://seeklogo.com/images/D/dota-2-logo-A8CAC9B4C9-seeklogo.com.png").into(logo2);

                    } else {
                        Picasso.get().load(response.getString("logo2")).into(logo2);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ShowToast("Ce match n'est pas encore disponilbe ou est déja terminer");

                    }
                }
        );
        requestQueue.add(objectRequest);
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public int getIdMatch() {
        return idMatch;
    }

    public void setIdMatch(int idMatch) {
        this.idMatch = idMatch;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIdTeamParier() {
        return idTeamParier;
    }

    public void setIdTeamParier(int idTeamParier) {
        this.idTeamParier = idTeamParier;
    }

    public Double getOdds() {
        return odds;
    }

    public void setOdds(Double odds) {
        this.odds = odds;
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;

    }
    public void ShowToast(String text){
        Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        toast.show();
    }
}