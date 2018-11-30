package com.example.neeraj.chatit;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Prof_act extends AppCompatActivity {

    private ImageView mProfilePic;
    private TextView mStatus;
    private TextView mProfilename;
    private TextView mFriendCount;
    private Button mSendFrndReq;
    private DatabaseReference mDatabase;
    private ProgressDialog mProgressBar;
    private DatabaseReference mFriendDatabase;
    private DatabaseReference mFrdDatabase;
    private String mCurrent_state;
    private Button  mDeclineBtn;
    private DatabaseReference mNotificationDatabase;

    private FirebaseUser mCurrentUser;
    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_act);

        final String UID = getIntent().getStringExtra("UID");

        mProgressBar = new ProgressDialog(this);
        mProgressBar.setTitle("Please Wait");
        mProgressBar.setMessage("Page is loading");
        mProgressBar.show();
        mProgressBar.setCanceledOnTouchOutside(false);
        mProfilePic = findViewById(R.id.Pro_Act_ImageView);

        mStatus = findViewById(R.id.pro_act_Status);
        mProfilename = findViewById(R.id.pro_act_Name);
        mSendFrndReq = findViewById(R.id.Pro_act_req_btn);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(UID);
        mFriendDatabase = FirebaseDatabase.getInstance().getReference().child("Friend_req");
        mFrdDatabase = FirebaseDatabase.getInstance().getReference().child("Friends");
        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        mDeclineBtn = (Button) findViewById(R.id.profile_decline_btn);
        mNotificationDatabase = FirebaseDatabase.getInstance().getReference().child("notifications");



        mCurrent_state = "not_friends";
        mDeclineBtn.setVisibility(View.INVISIBLE);
        mDeclineBtn.setEnabled(false);


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String DisplayName = dataSnapshot.child("Name").getValue().toString();
                String Status = dataSnapshot.child("Status").getValue().toString();
                String DisplayImage = dataSnapshot.child("Image").getValue().toString();

                mProfilename.setText(DisplayName);
                mStatus.setText(Status);
                Picasso.with(Prof_act.this).load(DisplayImage).placeholder(R.drawable.download).into(mProfilePic);

                mFriendDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(UID)){
                            String req_type = dataSnapshot.child(UID).child("request_type").getValue().toString();

                            if(req_type.equals("received")){
                                //Toast.makeText(Prof_act.this,"fuck",Toast.LENGTH_SHORT).show();
                                mCurrent_state ="req_received";
                                mSendFrndReq.setText("ACCEPT FRIEND REQUEST");
                                mDeclineBtn.setVisibility(View.VISIBLE);
                                mDeclineBtn.setEnabled(true);


                            }
                            if(req_type.equals("sent")){
                                mCurrent_state ="req_sent";
                                mSendFrndReq.setText("CANCEL FRIEND REQUEST");
                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                mDeclineBtn.setEnabled(false);

                            }
                            mProgressBar.dismiss();
                            if(true){
                                mFrdDatabase.child(mCurrentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        if(dataSnapshot.hasChild(UID)){

                                            mCurrent_state = "friends";
                                            mSendFrndReq.setText("Unfriend this Person");
                                            mDeclineBtn.setVisibility(View.INVISIBLE);
                                            mDeclineBtn.setEnabled(false);
                                        }
                                        mProgressBar.dismiss();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        mProgressBar.dismiss();
                                    }
                                });
                            }

                        }
                        mProgressBar.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mProgressBar.dismiss();
                    }
                });

//----------------------decline friend request------------------------------

                mDeclineBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map declineMap = new HashMap();

                        declineMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + UID, null);
                        declineMap.put("Friend_req/" + UID + "/" + mCurrentUser.getUid(), null);

                        mRootRef.updateChildren(declineMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                if(databaseError == null)
                                {

                                    mCurrent_state = "not friends";
                                    mSendFrndReq.setText("Send Friend Request");

                                    //mDiclineBnt.setVisibility(View.INVISIBLE);
                                    //mDiclineBnt.setEnabled(false);
                                }else{
                                    String error = databaseError.getMessage();
                                    Toast.makeText(Prof_act.this, error, Toast.LENGTH_LONG).show();
                                }

                                mSendFrndReq.setEnabled(true);
                            }
                        });

                    }
                });





    // --------------------IF YOU NOT A FRIEND-----------------------------------//

                    mSendFrndReq.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSendFrndReq.setEnabled(false);
                            if(mCurrent_state.equals("not_friends")) {

                                DatabaseReference newNotificationref = mRootRef.child("notifications").child(UID).push();
                                String newNotificationId = newNotificationref.getKey();

                                HashMap<String, String> notificationData = new HashMap<>();
                                notificationData.put("from", mCurrentUser.getUid());
                                notificationData.put("type", "request");

                                Map requestMap = new HashMap();
                                requestMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + UID + "/request_type", "sent");
                                requestMap.put("Friend_req/" + UID + "/" + mCurrentUser.getUid() + "/request_type", "received");
                                requestMap.put("notifications/" + UID + "/" + newNotificationId, notificationData);

                                mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                                        if(databaseError != null){

                                            Toast.makeText(Prof_act.this, "There was some error in sending request", Toast.LENGTH_SHORT).show();

                                        } else {

                                            mCurrent_state = "req_sent";
                                            mSendFrndReq.setText("Cancel Friend Request");

                                        }

                                        mSendFrndReq.setEnabled(true);


                                    }
                                });



                            }




                            if(mCurrent_state.equals("req_sent")){
                                mFrdDatabase.child(mCurrentUser.getUid()).child(UID).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        mFrdDatabase.child(UID).child(mCurrentUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {


                                                mSendFrndReq.setEnabled(true);
                                                mCurrent_state = "not_friends";
                                                mSendFrndReq.setText("Send Friend Request");

                                                mDeclineBtn.setVisibility(View.INVISIBLE);
                                                mDeclineBtn.setEnabled(false);


                                            }
                                        });

                                    }
                                });

                            }
//------------------------------------------------REQUEST RECIEVED-------------------------//////////////


                            if(mCurrent_state.equals("req_received")){
                                final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

                                Map friendsMap = new HashMap();
                                friendsMap.put("Friends/" + mCurrentUser.getUid() + "/" + UID + "/date", currentDate);
                                friendsMap.put("Friends/" + UID + "/"  + mCurrentUser.getUid() + "/date", currentDate);


                                friendsMap.put("Friend_req/" + mCurrentUser.getUid() + "/" + UID, null);
                                friendsMap.put("Friend_req/" + UID + "/" + mCurrentUser.getUid(), null);


                                mRootRef.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                        if(databaseError == null){

                                            mSendFrndReq.setEnabled(true);
                                            mCurrent_state = "friends";
                                            mSendFrndReq.setText("Unfriend this Person");

                                            mDeclineBtn.setVisibility(View.INVISIBLE);
                                            mDeclineBtn.setEnabled(false);

                                        } else {

                                            String error = databaseError.getMessage();

                                            Toast.makeText(Prof_act.this, error, Toast.LENGTH_SHORT).show();


                                        }

                                    }
                                });
                            }

                            // ------------ UNFRIENDS ---------

                            if(mCurrent_state.equals("friends")){

                                Map unfriendMap = new HashMap();
                                unfriendMap.put("Friends/" + mCurrentUser.getUid() + "/" + UID, null);
                                unfriendMap.put("Friends/" + UID + "/" + mCurrentUser.getUid(), null);

                                mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {


                                        if(databaseError == null){

                                            mCurrent_state = "not_friends";
                                            mSendFrndReq.setText("Send Friend Request");

                                            mDeclineBtn.setVisibility(View.INVISIBLE);
                                            mDeclineBtn.setEnabled(false);

                                        } else {

                                            String error = databaseError.getMessage();

                                            Toast.makeText(Prof_act.this, error, Toast.LENGTH_SHORT).show();


                                        }

                                        mSendFrndReq.setEnabled(true);

                                    }
                                });

                            }

                        }
                    });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
