package fsm.miaad.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.List;

import fsm.miaad.DAO.DBManager;
import fsm.miaad.R;
import fsm.miaad.models.History;
import fsm.miaad.models.User;

public class HistoryActivity extends AppCompatActivity {
    ImageView activity;
    TextView debut,fin;
    LinearLayout activitylinlay;
    private DBManager dbManager;
    private String emailConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        Intent intent = getIntent();
        // receive the value by getStringExtra() method and
        // key must be same which is send by first activity
        emailConnect= intent.getStringExtra("emailConnect");
        dbManager = new DBManager(this);
        dbManager.open();
//        debut=findViewById(R.id.debut);
        fin=findViewById(R.id.fin);
//        activity=findViewById(R.id.activity);
        activitylinlay=findViewById(R.id.activitylinlay);

        User userConnect=dbManager.getUserByEmail(emailConnect);

        Cursor histories =dbManager.HistoryByEmail(emailConnect);
        int count=0;
        if (histories.moveToFirst()) {
            do{
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER_VERTICAL);
                linearLayout.setPadding(0, 0, 10, 0);

                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        (40), // Convert dp to pixels if needed
                        (40)));
                imageView.setBackgroundResource(R.drawable.circular_grey_bordersolid);
                imageView.setPadding((10), (10), (10), (10));
                if(histories.getString(0).contains("standing")) {
                    imageView.setImageResource(R.drawable.standing);
                    imageView.setTag("standing");
                }
                else if (histories.getString(0).contains("running")) {
                    imageView.setImageResource(R.drawable.running);
                    imageView.setTag("running");
                }
                imageView.setId(View.generateViewId());

                Space space1 = new Space(this);
                space1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1));

                TextView textView1 = new TextView(this);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
//            textView1.setMargin(dpToPx(20));
                textView1.setText(histories.getString(1));
                textView1.setTextColor(ContextCompat.getColor(this, R.color.goodgrey));
                textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView1.setId(View.generateViewId());

                Space space2 = new Space(this);
                space2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        1));

                TextView textView2 = new TextView(this);
                textView2.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
//            textView2.setMarginLeft(dpToPx(20));
                textView2.setText(histories.getString(2));
                textView2.setTextColor(ContextCompat.getColor(this, R.color.goodgrey));
                textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                textView2.setId(View.generateViewId());

                linearLayout.addView(imageView);
                linearLayout.addView(space1);
                linearLayout.addView(textView1);
                linearLayout.addView(space2);
                linearLayout.addView(textView2);

                activitylinlay.addView(linearLayout);
                //0         1       2       3           4               5               6
                //activity,dateDe,dateFi,LatitudeStart,LongitudeStart,LatitudeFinish,LongitudeFinish
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        fin.setText(histories.getString(0));
//                        History history=dbManager.HistoryByAll(emailConnect,(String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());
                        Maps((String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());//,history.getLatitudeStart(),history.getLongitudeStart(),history.getLatitudeFinish(),history.getLongitudeFinish());
                    }
                });
                textView1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        fin.setText(histories.getString(0));
//                        History history=dbManager.HistoryByAll(emailConnect,(String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());
                        Maps((String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());//,history.getLatitudeStart(),history.getLongitudeStart(),history.getLatitudeFinish(),history.getLongitudeFinish());
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        fin.setText(histories.getString(0));
                        //History history=dbManager.HistoryByAll(emailConnect,(String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());
                        Maps((String) imageView.getTag(),textView1.getText().toString(),textView2.getText().toString());//,history.getLatitudeStart(),history.getLongitudeStart(),history.getLatitudeFinish(),history.getLongitudeFinish());
                    }
                });

            }while (histories.moveToNext());
        }

//        for (History history:histories) count++;
//        for (int i = 0; i < count; i++) {
//            LinearLayout linearLayout = new LinearLayout(this);
//            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
//            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
//            linearLayout.setGravity(Gravity.CENTER_VERTICAL);
//            linearLayout.setPadding(0, 0, 10, 0);
//
//            ImageView imageView = new ImageView(this);
//            imageView.setLayoutParams(new LinearLayout.LayoutParams(
//                    (40), // Convert dp to pixels if needed
//                    (40)));
//            imageView.setBackgroundResource(R.drawable.circular_grey_bordersolid);
//            imageView.setPadding((10), (10), (10), (10));
//            imageView.setImageResource(R.drawable.sitting);
//            imageView.setId(View.generateViewId());
//
//            Space space1 = new Space(this);
//            space1.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    1));
//
//            TextView textView1 = new TextView(this);
//            textView1.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
////            textView1.setMargin(dpToPx(20));
//            textView1.setText("sitting %");
//            textView1.setTextColor(ContextCompat.getColor(this, R.color.goodgrey));
//            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//            textView1.setId(View.generateViewId());
//
//            Space space2 = new Space(this);
//            space2.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    1));
//
//            TextView textView2 = new TextView(this);
//            textView2.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT));
////            textView2.setMarginLeft(dpToPx(20));
//            textView2.setText("sitting %");
//            textView2.setTextColor(ContextCompat.getColor(this, R.color.goodgrey));
//            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//            textView2.setId(View.generateViewId());
//
//            linearLayout.addView(imageView);
//            linearLayout.addView(space1);
//            linearLayout.addView(textView1);
//            linearLayout.addView(space2);
//            linearLayout.addView(textView2);
//
//            activitylinlay.addView(linearLayout);
//        }
    }



    public void ReturnClick(View v){
        Intent intent = new Intent(getApplicationContext(), AppActivity.class);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }

    public void Maps(String activity,String dateDe,String dateFi){//,double LatitudeStart,double LongitudeStart,double LatitudeFinish,double LongitudeFinish){
        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
        intent.putExtra("activity",activity);
        intent.putExtra("dateDe",dateDe);
        intent.putExtra("dateFi",dateFi);
//        intent.putExtra("LatitudeStart",LatitudeStart);
//        intent.putExtra("LongitudeStart",LongitudeStart);
//        intent.putExtra("LatitudeFinish",LatitudeFinish);
//        intent.putExtra("LongitudeFinish",LongitudeFinish);

        intent.putExtra("emailConnect",emailConnect);
        startActivity(intent);
    }
}