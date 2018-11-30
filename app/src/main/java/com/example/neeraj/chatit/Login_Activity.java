package com.example.neeraj.chatit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login_Activity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLoginBtn;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth =FirebaseAuth.getInstance();
        mToolbar = findViewById(R.id.login_app_bar);
        mToolbar.setTitle("Login");
        mLoginEmail = findViewById(R.id.login_email);
        mLoginPassword =findViewById(R.id.login_password);
        mLoginBtn = findViewById(R.id.login_lon_btn);
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("User");

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = mLoginEmail.getText().toString();
                String loginpass = mLoginPassword.getText().toString();
                if(TextUtils.isEmpty(loginEmail)|| TextUtils.isEmpty(loginpass)){

                }
                else{
                    signIn(loginEmail,loginpass);
                }
            }
        });
    }

    private void signIn(String loginEmail, String loginpass) {
        mAuth.signInWithEmailAndPassword(loginEmail, loginpass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            String current_user_id = mAuth.getCurrentUser().getUid();
                            String deviceToken = FirebaseInstanceId.getInstance().getToken();
                            mUserDatabase.child(current_user_id).child("device_token").setValue(deviceToken).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Intent StartIntent = new Intent(Login_Activity.this,MainActivity.class);
                                    StartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(StartIntent);
                                    finish();
                                }
                            });



                        } else {

                                   Toast.makeText(Login_Activity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}
