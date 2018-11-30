package com.example.neeraj.chatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Status_activity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private Button mChangeStatus;
    private EditText mStatus;
    private ProgressDialog mProgressBar;

    //Firebase

    FirebaseUser mFirebaseUser;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        //firebase
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = mFirebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(UID);
        mProgressBar = new ProgressDialog(this);
        String UserStatus = getIntent().getStringExtra("status");

        mToolbar = findViewById(R.id.status_appBar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Status");

        mStatus = findViewById(R.id.status_statusInput);
        mChangeStatus = findViewById(R.id.status_change_stat_btn);

        mStatus.setText(UserStatus);
        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String Status = mStatus.getText().toString();
                if (!Status.isEmpty()) {
                    mProgressBar.setTitle("Status");
                    mProgressBar.setMessage("Please wait while we updating your Status");
                    mProgressBar.show();
                    mDatabase.child("Status").setValue(Status).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                mProgressBar.dismiss();
                                Intent SettingIntent = new Intent(Status_activity.this,Setting_activity.class);
                                startActivity(SettingIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(Status_activity.this, "Some Error has occured",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }

        });
    }
}