package com.example.giuseppe.pervasivewater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Pop_add extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_add);
        Intent intent = getIntent();
        String address = intent.getStringExtra("myaddress"); //get position from MapsActivity
        String latlong = intent.getStringExtra("myposition"); //get position from MapsActivity
        Toast.makeText(getBaseContext(), latlong, Toast.LENGTH_LONG).show();

        TextView myTextView = (TextView) findViewById(R.id.myaddress);
        myTextView.setText(address);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* .9),(int)(height* .5));

        //pop-Up
        Button b3=(Button) findViewById(R.id.delete1);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Pop_add.this,MapsActivity.class));
                //overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
                finish();
            }
        });

        Button b4=(Button) findViewById(R.id.send1);
        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Address sent!", Toast.LENGTH_LONG).show();
                finish();
                //startActivity(new Intent(Pop_add.this,MapsActivity.class));
               // overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
            }
        });

    }


}
