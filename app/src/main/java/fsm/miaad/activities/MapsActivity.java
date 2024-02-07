package fsm.miaad.activities;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.Manifest;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import fsm.miaad.DAO.DBManager;
import fsm.miaad.databinding.ActivityMapsBinding;
import fsm.miaad.R;
import fsm.miaad.models.History;
import fsm.miaad.models.User;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private String emailConnect;
    private DBManager dbManager;
    User userConnect;
    String activity;
    String dateDe;
    String dateFi;
    double LatitudeStart;
    double LongitudeStart;
    double LatitudeFinish;
    double LongitudeFinish;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    ImageView menu_botton;
    TextView maptext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.hide();
//        }
        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        emailConnect= intent.getStringExtra("emailConnect");
        dbManager = new DBManager(this);
        dbManager.open();

        userConnect=dbManager.getUserByEmail(emailConnect);
        activity=intent.getStringExtra("activity");
        dateDe=intent.getStringExtra("dateDe");
        dateFi=intent.getStringExtra("dateFi");

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        LatitudeStart=intent.getDoubleExtra("LatitudeStart",0);
//        LongitudeStart=intent.getDoubleExtra("LongitudeStart",0);
//        LatitudeFinish=intent.getDoubleExtra("LatitudeFinish",0);
//        LongitudeFinish=intent.getDoubleExtra("LongitudeFinish",0);




        menu_botton=findViewById(R.id.menu_botton);
        menu_botton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu(view);
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_map);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_profile:
                    Intent intent1=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent1.putExtra("emailConnect",emailConnect);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_map:
                    return true;
                case R.id.bottom_app:
                    Intent intent2=new Intent(getApplicationContext(),AppActivity.class);
                    intent2.putExtra("emailConnect",emailConnect);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });

//        menu_botton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                menu(view);
//            }
//        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double coor1,coor2;
        if(activity!=null || dateDe!=null || dateFi!=null){// && LatitudeStart!=0 && LongitudeStart!=0 && LatitudeFinish!=0 && LongitudeFinish!=0){
//            Toast.makeText(MapsActivity.this, activity, Toast.LENGTH_SHORT).show();

            History history=dbManager.HistoryByAll(emailConnect,activity,dateDe,dateFi);
            if(history!=null) {
                LongitudeFinish = history.getLongitudeFinish();
                LatitudeFinish = history.getLatitudeFinish();
                LongitudeStart = history.getLongitudeStart();
                LatitudeStart = history.getLatitudeStart();
                LatLng startLatLng = new LatLng(LatitudeStart, LongitudeStart);
                LatLng finishLatLng = new LatLng(LatitudeFinish, LongitudeFinish);

                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(startLatLng)
                        .add(finishLatLng)
                        .width(5)
                        .color(Color.RED);

                mMap.addPolyline(polylineOptions);

// Optionally, zoom the map to display the entire polyline
//                LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                builder.include(startLatLng);
//                builder.include(finishLatLng);
//                LatLngBounds bounds = builder.build();
//                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));

            }
        }
        else {
//            Toast.makeText(MapsActivity.this, "else", Toast.LENGTH_SHORT).show();

            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
            Location location_GPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location location_NETWORK = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location_GPS != null) {
                coor1=location_GPS.getLatitude() ;
                coor2=location_GPS.getLongitude() ;

            } else {
                coor1=location_NETWORK.getLatitude() ;
                coor2=location_NETWORK.getLongitude() ;
            }

            LatLng coor = new LatLng(coor1, coor2 );
            mMap.addMarker(new MarkerOptions().position(coor).title("You are here Now"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(coor));
        }
    }

    public void MapsShow(){

        LatLng startLatLng = new LatLng(LatitudeStart, LongitudeStart);
        LatLng finishLatLng = new LatLng(LatitudeFinish, LongitudeFinish);

        PolylineOptions polylineOptions = new PolylineOptions()
                .add(startLatLng)
                .add(finishLatLng)
                .width(5)
                .color(Color.RED);

        mMap.addPolyline(polylineOptions);

// Optionally, zoom the map to display the entire polyline
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(startLatLng);
        builder.include(finishLatLng);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));


    }

    public void menu(View v){


        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(MapsActivity.this, menu_botton);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.user);

        // Change the title of the menu item
        String name=userConnect.getLastname()+" "+userConnect.getFirstname();
        menuItem.setTitle(name);

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.user) {
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    intent.putExtra("emailConnect",emailConnect);
                    startActivity(intent);
                } else if (menuItem.getItemId() == R.id.disconnect) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
//                Toast.makeText(ProfileActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }



//    public void ReturnClick(View v){
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//
//        intent.putExtra("emailConnect",emailConnect);
//        startActivity(intent);
//    }
}