package com.example.neeraj.chatit;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class startactivity extends AppCompatActivity {

    private Button mRegButton;
    private Button mLoginbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mLoginbtn = findViewById(R.id.start_login_btn);
        mRegButton = findViewById(R.id.start_reg_btn);

        mLoginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent LoginIntent = new Intent(startactivity.this,Login_Activity.class);
                startActivity(LoginIntent);
            }
        });

        mRegButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent regIntent = new Intent(startactivity.this,RegisterActivity.class);
                startActivity(regIntent);
            }
        });

    }
}
