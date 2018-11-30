package com.example.neeraj.chatit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private EditText mDisplayname;
    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;
    private android.support.v7.widget.Toolbar mToolbar;
    private ProgressDialog mProgressBar;
    private FirebaseAuth mAuths;

    DatabaseReference database;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mDisplayname = findViewById(R.id.reg_dis_name);
       mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateBtn = findViewById(R.id.reg_create_btn);

        mProgressBar = new ProgressDialog(this);
        mToolbar = findViewById(R.id.reg_app_bar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Registration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuths = FirebaseAuth.getInstance();



        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Displayname =  mDisplayname.getText().toString();
                String Email =  mEmail.getText().toString();
                String Password =  mPassword.getText().toString();

                if(TextUtils.isEmpty(Displayname)||TextUtils.isEmpty(Email)||TextUtils.isEmpty(Password)){
                    Toast.makeText(RegisterActivity.this, "Please Enter Details",
                            Toast.LENGTH_SHORT).show();
                }
                else{
                    mProgressBar.setTitle("Registering User");
                    mProgressBar.setMessage("Please wait while we registering you");
                    mProgressBar.setCanceledOnTouchOutside(false);
                    mProgressBar.show();
                    User_register(Displayname,Email,Password);
                }

            }
        });

    }

    private void User_register(final String displayname, String email, String password) {

        mAuths.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            String UserId = firebaseUser.getUid();
                            database = FirebaseDatabase.getInstance().getReference().child("User").child(UserId);
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();

                            HashMap<String, String> Uservalue = new HashMap<>();

                            Uservalue.put("Name", displayname);
                            Uservalue.put("Status", "Hi there ! I am Using chatIT");
                            Uservalue.put("Image", "default");
                            Uservalue.put("ThumbImage", "Default");
                            Uservalue.put("device_token", deviceToken);
                            database.setValue(Uservalue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mProgressBar.dismiss();
                                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(mainIntent);
                                        finish();
                                    }

                                }
                            });
                        }
                            else {
                                mProgressBar.hide();
                                Toast.makeText(RegisterActivity.this, "Some Error accured.Please try again.",
                                        Toast.LENGTH_SHORT).show();

                            }


                        // ...
                    }
                });

    }
}
