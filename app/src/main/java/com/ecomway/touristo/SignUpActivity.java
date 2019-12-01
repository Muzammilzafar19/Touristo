package com.ecomway.touristo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.ecomway.touristo.utils.Urls;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class SignUpActivity extends AppCompatActivity {
    TextView logiin;
    Button btnSignUp;
    EditText txtusername,txtemail,txtpassword,txtmobileno;

    SpotsDialog dialog;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        getSupportActionBar().hide();
        init();
        onClicked();

    }

    private void init() {
        logiin = findViewById(R.id.logiin);
        txtemail=findViewById(R.id.txtmail);
        txtusername=findViewById(R.id.txtusrusr);
        txtpassword=findViewById(R.id.txtpasswrd);
        txtmobileno=findViewById(R.id.txtmobphone);
        btnSignUp=findViewById(R.id.btnsup);
        dialog = new SpotsDialog(this);
        auth = FirebaseAuth.getInstance();

    }

    private void onClicked() {
        logiin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUpActivity.this.finish();
            }
        });
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email=txtemail.getText().toString();
                String username=txtusername.getText().toString();
                String pasword=txtpassword.getText().toString();
                String mobileno=txtmobileno.getText().toString();
                dialog.show();
                dialog.setTitle("Wait");
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Enter Username!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pasword)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pasword.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(mobileno)) {
                    Toast.makeText(getApplicationContext(), "Enter Mobile No!", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(email, pasword)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignUpActivity.this, "Registration Successfully Completed" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    syncCities(Urls.BASE_URL + "cityApi.php");
                                    syncTouringPoint(Urls.BASE_URL + "tour.php");

                                }
                            }
                        });
            }
        });
    }
    private void syncCities(String url) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please wait");
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Cities");
                    if (tableArray.length() > 0) {
                        RoomDatabaseClient.getInstance(SignUpActivity.this).getAppDatabase().citiesTableDao().deleteAll();
                        for (int i = 0; i < tableArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) tableArray.get(i);
                            CitiesTable cm = new CitiesTable();
                            cm.setCityIdFromServer(feedObj.getString("city_id"));
                            cm.setCityName(feedObj.getString("name"));
                            cm.setCityImagePath("http://www.fivewaterstourism.com/assets/images/"+feedObj.getString("image"));
                            cm.setCityDescription(feedObj.getString("description"));
                            cm.setCityLat(feedObj.getString("city_latitude"));
                            cm.setCityLng(feedObj.getString("city_longitude"));
                            RoomDatabaseClient.getInstance(SignUpActivity.this).getAppDatabase().citiesTableDao().insertAll(cm);
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
                    Toast.makeText(SignUpActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SignUpActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setMessage("Please wait");
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("TourPoints");
                    if (tableArray.length() > 0) {
                        RoomDatabaseClient.getInstance(SignUpActivity.this).getAppDatabase().touringPointsTableDao().deleteAll();
                        for (int i = 0; i < tableArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) tableArray.get(i);
                            TouringPointsTable tpt = new TouringPointsTable();
                            tpt.setTourCity(feedObj.getString("city_id"));
                            tpt.setTouringPointName(feedObj.getString("name"));
                            tpt.setTouringPointImagePath("http://www.fivewaterstourism.com/assets/images/"+feedObj.getString("image"));
                            tpt.setTouringPointDescription(feedObj.getString("description"));
                            tpt.setTourPointType(feedObj.getString("type"));
                            tpt.setTourLat(feedObj.getString("tourpoint_latitude"));
                            tpt.setTourLng(feedObj.getString("tourpoint_longnitude"));
                            RoomDatabaseClient.getInstance(SignUpActivity.this).getAppDatabase().touringPointsTableDao().insertAll(tpt);
                        }
                        startActivity(new Intent(SignUpActivity.this, ProfileActivity.class).putExtra("email",txtemail.getText().toString()));
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
                    Toast.makeText(SignUpActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(SignUpActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(SignUpActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
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
