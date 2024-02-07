package fsm.miaad.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.History;
import fsm.miaad.models.User;

public class AppActivity extends AppCompatActivity implements SensorEventListener {
    private String emailConnect;
    private DBManager dbManager;
    User userConnect;

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float[] gravity = new float[3];
    private float[] linear_acceleration = new float[3];
    private int[] confidenceValues = new int[5]; //running,sitting,jumping,standing,walking

    TextView sitting,jumping,walking,standing,running,stat;
    ImageView menu_botton;
    History historique;
    Date startDate;
    List<Double> coor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        getSupportActionBar().hide();

        setContentView(R.layout.activity_app);
        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        emailConnect= intent.getStringExtra("emailConnect");
        dbManager = new DBManager(this);
        dbManager.open();


        userConnect = dbManager.getUserByEmail(emailConnect);

        menu_botton=findViewById(R.id.menu_botton);

        historique = new History();
        historique.setEmail(emailConnect);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_app);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_map:
                    Intent intent2=new Intent(getApplicationContext(),MapsActivity.class);
                    intent2.putExtra("emailConnect",emailConnect);
                    startActivity(intent2);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.bottom_app:
                    return true;
                case R.id.bottom_profile:
                    Intent intent1=new Intent(getApplicationContext(),ProfileActivity.class);
                    intent1.putExtra("emailConnect",emailConnect);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });


        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        sitting=findViewById(R.id.sitting);
        jumping=findViewById(R.id.jumping);
        walking=findViewById(R.id.walking);
        standing=findViewById(R.id.standing);
        running=findViewById(R.id.running);
        stat=findViewById(R.id.stat);

        startDate = new Date();

        coor = location();
        historique.setLatitudeStart(coor.get(0));
        historique.setLongitudeStart(coor.get(1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long delay = 1 * 60 * 1000;
        historique.setDateDe(startDate+"");
//        historique.setLatitudeStart(33.895000);
//        historique.setLongitudeStart(-5.554722);
        Date currentDate = new Date();
//        long elapsedTime = currentDate.getTime() - startDate.getTime();
//        long elapsedSeconds = elapsedTime / 1000;

//        Toast.makeText(AppActivity.this, "before", Toast.LENGTH_SHORT).show();


        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            final float alpha = 0.8f;
            gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
            gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
            gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];

            linear_acceleration[0] = event.values[0] - gravity[0];
            linear_acceleration[1] = event.values[1] - gravity[1];
            linear_acceleration[2] = event.values[2] - gravity[2];

            // Determine the activity based on the accelerometer data
            int activity = getActivity(linear_acceleration);



            // Update the confidence values in the table
            confidenceValues[activity] += 1;
            int totalConfidence = 0;
            for (int i = 0; i < confidenceValues.length; i++) {
                totalConfidence += confidenceValues[i];
            }



            if (totalConfidence > 0) {
                int standingCal=(confidenceValues[0] * 100 / totalConfidence);
                int sittingCal=(confidenceValues[1] * 100 / totalConfidence);
                int walkingCal=(confidenceValues[2] * 100 / totalConfidence);
                int jumpingCal=(confidenceValues[4] * 100 / totalConfidence);
                int runningCal=(confidenceValues[3] * 100 / totalConfidence);
                standing.setText("standing % = "+standingCal + "%");
                sitting.setText("sitting % = "+sittingCal+ "%");
                walking.setText("walking % = "+walkingCal + "%");
                jumping.setText("jumping % = "+jumpingCal + "%");
                running.setText("running % = "+runningCal+"%");

                int max;
                max=Math.max(standingCal,Math.max(sittingCal,Math.max(walkingCal,Math.max(jumpingCal,runningCal))));
                if(max==standingCal) {
                    stat.setText("standing");

                }
                else if(max==sittingCal) {
                    stat.setText("sitting");


                }
                else if(max==walkingCal) {
                    stat.setText("walking");
                }
                else if(max==jumpingCal) {
                    stat.setText("jumping");
                }
                else if(max==runningCal) {
                    stat.setText("running");
                }
            }
        }

        long elapsedTime = currentDate.getTime() - startDate.getTime();
        long elapsedSeconds = elapsedTime / 1000;
//        stat.setText("running");
        if(elapsedSeconds>60 && (stat.getText().toString().equals("running") || stat.getText().toString().equals("walking"))) {
            // Define the desired date and time format
            String pattern = "yyyy/MM/dd-HH:mm:ss";

            // Create a SimpleDateFormat object with the specified pattern
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            // Format the Date object using the SimpleDateFormat
            String formattedDate = sdf.format(new Date());
            historique.setDateFi(formattedDate);
            historique.setActivity(stat.getText().toString());
            coor = location();
            historique.setLatitudeFinish(coor.get(0));
            historique.setLongitudeFinish(coor.get(1));
            boolean insert = dbManager.insertHistory(emailConnect, historique);
            if (insert) {
                Toast.makeText(AppActivity.this, "Your activity historical have been successfully saved!!", Toast.LENGTH_SHORT).show();

            }
            startDate=new Date();

        }
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
////                Toast.makeText(AppActivity.this, "after", Toast.LENGTH_SHORT).show();
//
//
//            }
//        },delay);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }

    private int getActivity(float[] acceleration) {
        // Determine the activity based on the accelerometer data
        int activity;
        Float x = acceleration[0];
        Float y = acceleration[1];
        Float z = acceleration[2];

        Float magnitude = (float) Math.sqrt(x * x + y * y + z * z);

        if (magnitude < 2.0f) {
            activity = 0; // Standing
        } else if (magnitude < 5.0f) {
            activity = 1; // Sitting
        } else if (magnitude < 8.0f) {
            activity = 2; // Walking
        } else if (magnitude < 12.0f) {
            activity = 3; // Running
        } else {
            activity = 4; // Jumping
        }
        return activity;
    }


    public void menu(View v){


        // Initializing the popup menu and giving the reference as current context
        PopupMenu popupMenu = new PopupMenu(AppActivity.this, menu_botton);

        // Inflating popup menu from popup_menu.xml file
        popupMenu.getMenuInflater().inflate(R.menu.profilehis_menu, popupMenu.getMenu());
        MenuItem menuItem = popupMenu.getMenu().findItem(R.id.user);
//        MenuItem menuItem2 = popupMenu.getMenu().findItem(R.id.disconnect);

        // Change the title of the menu item

//        View actionView = menuItem.getActionView();
//        View actionView2 = menuItem2.getActionView();
//
//        // Set the background resource for the ActionView
//        actionView.setBackgroundResource(R.color.col2);
//        actionView2.setBackgroundResource(R.color.col2);

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
                } else if (menuItem.getItemId() == R.id.his) {
                    Intent intent = new Intent(getApplicationContext(), HistoryActivity.class);
                    intent.putExtra("emailConnect",emailConnect);
                    startActivity(intent);
                }
//                Toast.makeText(ProfileActivity.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        // Showing the popup menu
        popupMenu.show();
    }


    public List<Double> location(){
        List<Double> coor=new ArrayList<>();
        double coor1,coor2;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//            return;
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
        coor.add(coor1);
        coor.add(coor2);
        return coor;

    }

//    public void ReturnClick(View v){
//        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
//
//        intent.putExtra("emailConnect",emailConnect);
//        startActivity(intent);
//    }
}