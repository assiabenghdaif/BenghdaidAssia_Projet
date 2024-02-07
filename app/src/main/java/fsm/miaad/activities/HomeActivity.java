package fsm.miaad.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.User;

public class HomeActivity extends AppCompatActivity {
    TextView fullname;
    private String emailConnect;
    private DBManager dbManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

//        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        fullname=findViewById(R.id.fullname);
        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        emailConnect= intent.getStringExtra("emailConnect");
        dbManager = new DBManager(this);
        dbManager.open();

        User userConnect=dbManager.getUserByEmail(emailConnect);
        fullname.setText("Hi "+userConnect.getFirstname()+" "+userConnect.getLastname()+" welcome to my App");
    }

    public void HomeClick(View v){
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }

    public void ProfileClick(View v){
        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }

    public void MapsClick(View v){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }

    public void AppClick(View v){
        Intent intent = new Intent(getApplicationContext(), AppActivity.class);
        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }
}