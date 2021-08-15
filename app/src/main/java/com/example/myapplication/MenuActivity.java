package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
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
import com.example.myapplication.service.MyFirebaseMessagingService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.databinding.ActivityMenuBinding;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static android.content.ContentValues.TAG;

public class MenuActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMenuBinding binding;
    private DatabaseHelper db;
    private String url = "https://backend-javaa-mbds272957.herokuapp.com/insererNotifWeb";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        invalidateOptionsMenu();
        MyFirebaseMessagingService mf = new MyFirebaseMessagingService();
        setuppermission();

db = new DatabaseHelper(this);

        if(this.isNetworkAvailable()){
        //db.deletetableuser();
            //db.initializeiduser();
            if(db.getToken()!=null && db.getIdUser()[0]!=null)
            {
                GetNotification();

            }
        }
        else if(!this.isNetworkAvailable() || this.isNetworkAvailable() == null ) {
           db.deleteToken();

        }

        binding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

       setSupportActionBar(binding.appBarMenu.toolbar);
        if(db.getToken()!=null){
         //   AuthAPI.getCurrentUser(getApplicationContext(),db.getToken());
        }
        binding.appBarMenu.fab.setVisibility(View.INVISIBLE);
        binding.appBarMenu.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        if(db.getToken()!=null){
            navigationView.getMenu().findItem(R.id.nav_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_qrcode).setVisible(false);

            navigationView.getMenu().findItem(R.id.nav_deconnexion).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    db.deleteToken();
                    db.deleteuser();
                    Intent i = new Intent(MenuActivity.this, MenuActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(i);
                    finish();
                    return true;
                }
            });
        }

        else {
            navigationView.getMenu().findItem(R.id.nav_profil).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_historique).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_deconnexion).setVisible(false);


        }
        navigationView.setNavigationItemSelectedListener(this);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow , R.id.nav_login,R.id.nav_profil,R.id.nav_historique,R.id.nav_deconnexion,R.id.nav_qrcode)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        invalidateOptionsMenu();

    }

    public void GetNotification(){
        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();

                        // Log and toast
                        String msg = token;

                        JSONObject jsonBody = new JSONObject();
                        try {
                            jsonBody.put("idUser",db.getIdUser()[0]);
                            jsonBody.put("token", msg);

                            final String requestBody = jsonBody.toString();



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.i("VOLLEY", response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e("VOLLEY", error.toString());
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


                        //Toast.makeText(MenuActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }


                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);


        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }
    public void LogOut(){
        db.deleteToken();
    }
    public void showmessage(){
        Log.e("logout","true");
    }
    public Boolean isNetworkAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    private void setuppermission(){
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        if(permission != PackageManager.PERMISSION_GRANTED){
            makeRequest();
        }
    }

    private void makeRequest() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

        }else{
            ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.CAMERA}, 0);
        }
    }

}