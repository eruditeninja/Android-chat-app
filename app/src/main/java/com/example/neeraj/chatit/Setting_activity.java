package com.example.neeraj.chatit;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.URI;

import de.hdodenhof.circleimageview.CircleImageView;

public class Setting_activity extends AppCompatActivity {

    StorageReference mFireBaseStorage;
    DatabaseReference mDatabase;
    FirebaseUser firebaseUser;
    CircleImageView circleImageView;
    TextView mStatus;
    TextView mDisplayname;
    Button mChangeStatus;
    Button mChangeImage;
    private ProgressDialog progressDialog;

    private final static int GALLARY_PICK = 1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        progressDialog = new ProgressDialog(this);
        mChangeStatus = findViewById(R.id.setting_stat_btn);
        mChangeImage = findViewById(R.id.setting_btn_chgImg);
        circleImageView = findViewById(R.id.setting_image);
        mStatus = findViewById(R.id.setting_status);
        mDisplayname = findViewById(R.id.setting_name);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String UID = firebaseUser.getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(UID);
        mFireBaseStorage = FirebaseStorage.getInstance().getReference();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String DisplayName = dataSnapshot.child("Name").getValue().toString();
                String Status = dataSnapshot.child("Status").getValue().toString();
                String DisplayImage = dataSnapshot.child("Image").getValue().toString();
                String ThumbImage = dataSnapshot.child("ThumbImage").getValue().toString();

                mStatus.setText(Status);
                mDisplayname.setText(DisplayName);
                Picasso.with(Setting_activity.this).load(DisplayImage).into(circleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Status = mStatus.getText().toString();
                Intent StatusIntent = new Intent(Setting_activity.this, Status_activity.class);
                StatusIntent.putExtra("status", Status);
                startActivity(StatusIntent);

            }
        });

        mChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallaryIntent = new Intent();
                gallaryIntent.setType("image/*");
                gallaryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(gallaryIntent, "SELECT IMAGE"), GALLARY_PICK);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARY_PICK && resultCode == RESULT_OK) {
            Uri ImgURI = data.getData();
            CropImage.activity(ImgURI)
                    .setAspectRatio(1, 1)
                    .start(Setting_activity.this);

        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode==RESULT_CANCELED){Toast.makeText(Setting_activity.this,"Upload Successful",Toast.LENGTH_SHORT).show();}
            if (resultCode == RESULT_OK) {

                progressDialog.setTitle("Uploading");
                progressDialog.setMessage("Profile Image Uploading");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                Uri resultUri = result.getUri();
                String UserID = firebaseUser.getUid();

                final StorageReference filePath = mFireBaseStorage.child("profile_image").child(UserID+".jpg");

                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                mDatabase.child("Image").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Setting_activity.this,"Upload Successful",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                    }


            });

        }
    }}}

