package com.ecomway.touristo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

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
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ecomway.touristo.fragments.SearchableFragment;
import com.ecomway.touristo.helpinginterface.IndicationInterface;
import com.ecomway.touristo.room.database.RoomDatabaseClient;
import com.ecomway.touristo.room.entities.CitiesTable;
import com.ecomway.touristo.room.entities.TouringPointsTable;
import com.ecomway.touristo.utils.SharePref;
import com.ecomway.touristo.utils.Urls;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, IndicationInterface {
    private SharePref pref;
    private FirebaseAuth auth;
    private TextView txtName;
    Spinner search;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    private AppBarConfiguration mAppBarConfiguration;
    private CircleImageView profileimg;
    DrawerLayout drawer;
    FrameLayout frame;
    SearchableFragment searchableFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        pref = new SharePref(HomeActivity.this);
        setSupportActionBar(toolbar);
        search = findViewById(R.id.search);
        searchFn();
        search.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(HomeActivity.this,TouringPointsDetailActivity.class).putExtra("touringName",list.get(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        frame = findViewById(R.id.frame1);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(HomeActivity.this);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_logout,
                R.id.nav_instructions, R.id.nav_event, R.id.nav_helpline, R.id.nav_thingtokeep, R.id.nav_tourType, R.id.nav_spotSearch)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View hView = navigationView.getHeaderView(0);

        profileimg = hView.findViewById(R.id.profileimg);
        txtName = hView.findViewById(R.id.txtname);
        profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class).putExtra("email", pref.getEmail()));
            }
        });


        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.person);
        txtName.setText(pref.getName());
        Glide.with(HomeActivity.this).load(pref.getImagePath()).apply(requestOptions).into(profileimg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_sync:
                syncCities(Urls.BASE_URL + "cityApi.php");
                syncTouringPoint(Urls.BASE_URL + "tour.php");

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void searchFn() {
        list = new ArrayList<>();
        for(TouringPointsTable ct:RoomDatabaseClient.getInstance(HomeActivity.this).getAppDatabase().touringPointsTableDao().getAll())
        {
            list.add(ct.getTouringPointName());
        }

        adapter = new ArrayAdapter<String>(HomeActivity.this, android.R.layout.simple_list_item_1, list);
        search.setAdapter(adapter);


    }

    public void replaceToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        if (addToBackStack) {
            transaction.addToBackStack(fragment.getClass().getSimpleName());
        }
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();
        if (id == R.id.action_sync) {


        }
        //close navigation drawer
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void syncCities(String url) {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please wait");
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("Cities");
                    if (tableArray.length() > 0) {
                        RoomDatabaseClient.getInstance(HomeActivity.this).getAppDatabase().citiesTableDao().deleteAll();
                        for (int i = 0; i < tableArray.length(); i++) {
                            JSONObject feedObj = (JSONObject) tableArray.get(i);
                            CitiesTable cm = new CitiesTable();
                            cm.setCityIdFromServer(feedObj.getString("city_id"));
                            cm.setCityName(feedObj.getString("name"));
                            cm.setCityImagePath("http://www.fivewaterstourism.com/assets/images/" + feedObj.getString("image"));
                            cm.setCityDescription(feedObj.getString("description"));
                            cm.setCityLat(feedObj.getString("city_latitude"));
                            cm.setCityLng(feedObj.getString("city_longitude"));
                            RoomDatabaseClient.getInstance(HomeActivity.this).getAppDatabase().citiesTableDao().insertAll(cm);
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
                    Toast.makeText(HomeActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(HomeActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(HomeActivity.this);
        progressDialog.setMessage("Please wait");
        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONArray tableArray = parentObject.getJSONArray("TourPoints");
                    if (tableArray.length() > 0) {
                        RoomDatabaseClient.getInstance(HomeActivity.this).getAppDatabase().touringPointsTableDao().deleteAll();
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
                            RoomDatabaseClient.getInstance(HomeActivity.this).getAppDatabase().touringPointsTableDao().insertAll(tpt);
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
                    Toast.makeText(HomeActivity.this, "Server error occurred", Toast.LENGTH_SHORT).show();
                } else if (error instanceof NetworkError) {
                    Toast.makeText(HomeActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(HomeActivity.this, "Connection timeout error", Toast.LENGTH_SHORT).show();
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



    @Override
    public void toggleSearchBar(String indication) {

    }


}
