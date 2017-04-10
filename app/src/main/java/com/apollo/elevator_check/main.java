package com.apollo.elevator_check;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.SDKInitializer;


public class main extends Activity {

    Button button1;
    Button button2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        SDKInitializer.initialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button1 =(Button)findViewById(R.id.work);
        button2 =(Button)findViewById(R.id.upload);
        button1.setOnClickListener(onClickListenerwork);
        button2.setOnClickListener(onClickListenerupload);


    }


    View.OnClickListener onClickListenerwork =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(main.this,createtask.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
    };

    View.OnClickListener onClickListenerupload =new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(main.this,uploaddata.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
    };





}
