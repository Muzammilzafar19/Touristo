package com.ecomway.touristo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;
import com.ecomway.touristo.utils.SharePref;
import com.ecomway.touristo.utils.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import dmax.dialog.SpotsDialog;

public class LoginActivity extends AppCompatActivity {
    private ConstraintLayout constraintLayout;
    private EditText etEmail, etPassword;
   // private AnimationDrawable animationDrawable;
    private FirebaseAuth auth;
    SpotsDialog dialog;
    private SharePref pref;
    DatabaseReference mDatabase,refroot;
    private Button btnLogin, btnSignUp,btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();
        init();
        onClicked();


      /*  // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) constraintLayout.getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);*/
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btnSignUp);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnReset =  findViewById(R.id.btn_reset_password);
        dialog = new SpotsDialog(this);
        pref=new SharePref(LoginActivity.this);
        // init constraintLayout
        constraintLayout = findViewById(R.id.constraintLayout);

    }

    private void onClicked() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String password = etPassword.getText().toString();

                if (TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(getApplicationContext(), "Enter email in Correct format!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                dialog.show();
                dialog.setTitle("Please Wait");

                //authenticate user
                auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                dialog.dismiss();
                                if (!task.isSuccessful()) {
                                    // there was an error
                                    if (password.length() < 6) {
                                        etPassword.setError(getString(R.string.minimum_password));
                                    } else {
                                        Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    mDatabase = FirebaseDatabase.getInstance().getReference();
                                    // FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                                    refroot=mDatabase.child("users").child(auth.getUid());
                                    final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                                    progressDialog.setTitle("Check Authentication");
                                    progressDialog.setIcon(R.drawable.logosplash);
                                    progressDialog.setMessage("Please Wait.........");
                                    progressDialog.show();
                                    ValueEventListener eventListener = new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()) {
                                                //create new user
                                                progressDialog.dismiss();
                                                pref.setLoginIndicator("yes");
                                                pref.setName(dataSnapshot.child("name").getValue().toString());
                                                pref.setGender(dataSnapshot.child("gender").getValue().toString());
                                                pref.setAge(dataSnapshot.child("age").getValue().toString());
                                                pref.setCountry(dataSnapshot.child("country").getValue().toString());
                                                pref.setImagePath(dataSnapshot.child("imagepath").getValue().toString());
                                                pref.setUserId(auth.getUid());
                                                pref.setEmail(dataSnapshot.child("email").getValue().toString());
                                                syncCities(Urls.BASE_URL + "cityApi.php");
                                                syncTouringPoint(Urls.BASE_URL + "tour.php");


                                                // Toast.makeText(ProfileActivity.this,"Value Exist",Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                progressDialog.dismiss();
                                                pref.setLoginIndicator("yes");
                                                startActivity(new Intent(LoginActivity.this, ProfileActivity.class).putExtra("email",email));
                                                finish();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                    };
                                    refroot.addListenerForSingleValueEvent(eventListener);
                                    // finish();
                                }
                            }
                        });
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    /*    if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }*/

    }

    @Override
    protected void onPause() {
        super.onPause();
  /*      if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }*/
    }

    private void syncCities(String url) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait");
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Cities");
                    if (tableArray.length() > 0) {
                        RoomDatabaseClient.getInstance(LoginActivity.this).getAppDatabase().citiesTableDao().deleteAll();
                        for (int i = 0; i < tableArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) tableArray.get(i);
                            CitiesTable cm = new CitiesTable();
                            cm.setCityIdFromServer(feedObj.getString("city_id"));
                            cm.setCityName(feedObj.getString("name"));
                            cm.setCityImagePath("http://www.fivewaterstourism.com/assets/images/" + feedObj.getString("image"));
                            cm.setCityDescription(feedObj.getString("description"));
                            cm.setCityLat(feedObj.getString("city_latitude"));
                            cm.setCityLng(feedObj.getString("city_longitude"));
                            RoomDatabaseClient.getInstance(LoginActivity.this).getAppDatabase().citiesTableDao().insertAll(cm);
                        }
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("registerError", error.getMessage() + "");
                if (error instanceof ServerError) {
                    Toast.makeText(LoginActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog.show();
        queue.add(request);
    }

    private void syncTouringPoint(String url) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Please wait");
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("TourPoints");
                    if (tableArray.length() > 0) {
                        RoomDatabaseClient.getInstance(LoginActivity.this).getAppDatabase().touringPointsTableDao().deleteAll();
                        for (int i = 0; i < tableArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) tableArray.get(i);
                            TouringPointsTable tpt = new TouringPointsTable();
                            tpt.setTourCity(feedObj.getString("city_id"));
                            tpt.setTouringPointName(feedObj.getString("name"));
                            tpt.setTouringPointImagePath("http://www.fivewaterstourism.com/assets/images/" + feedObj.getString("image"));
                            tpt.setTouringPointDescription(feedObj.getString("description"));
                            tpt.setTourPointType(feedObj.getString("type"));
                            tpt.setTourLat(feedObj.getString("tourpoint_latitude"));
                            tpt.setTourLng(feedObj.getString("tourpoint_longnitude"));
                            RoomDatabaseClient.getInstance(LoginActivity.this).getAppDatabase().touringPointsTableDao().insertAll(tpt);
                        }
                        startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                        finish();
                    }

                } catch (JSONException e) {
                    Log.e("JSONException", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.i("registerError", error.getMessage() + "");
                if (error instanceof ServerError) {
                    Toast.makeText(LoginActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(LoginActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                9000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(this);
        progressDialog.show();
        queue.add(request);
    }


}
