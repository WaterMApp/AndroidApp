package com.example.giuseppe.pervasivewater;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

public class Pop extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.pop_window);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width* .9),(int)(height* .5));

        //pop-Up
        Button b1=(Button) findViewById(R.id.delete);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(Pop.this,MarkerActivity.class));
               // overridePendingTransition( R.anim.fade_in, R.anim.fade_out );
                finish();
            }
        });

        Button b2=(Button) findViewById(R.id.send);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getBaseContext(), "Comment sent!", Toast.LENGTH_LONG).show();
                finish();
            }
        });

    }


}
