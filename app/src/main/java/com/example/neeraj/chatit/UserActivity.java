package com.example.neeraj.chatit;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mUserList;
    private DatabaseReference mFirebasedatabase;
    FirebaseRecyclerAdapter<User,ViewHolder> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<User> options;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());

        }

        mUserList = findViewById(R.id.UserAct_recy_view);
        mUserList.setHasFixedSize(true);

        mFirebasedatabase=FirebaseDatabase.getInstance().getReference().child("User");

        mToolbar = findViewById(R.id.User_Act_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All User");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         options= new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(mFirebasedatabase, User.class)
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<User, ViewHolder>(options) {

            @Override

            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull User model) {
                holder.mTextName.setText(model.getName());
                    holder.mTextSatus.setText(model.getStatus());
                Picasso.with(UserActivity.this).load(model.getImage()).placeholder(R.drawable.download).into( holder.mImage);

               final  String UID = getRef(position).getKey();
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ProfileIntent = new Intent(UserActivity.this,Prof_act.class);
                        ProfileIntent.putExtra("UID",UID);
                        startActivity(ProfileIntent);
                    }
                });
            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_user_layout, parent, false);

                return new ViewHolder(view);
            }


        };
        mUserList.setLayoutManager(new LinearLayoutManager(this));
        firebaseRecyclerAdapter.startListening();
        mUserList.setAdapter(firebaseRecyclerAdapter);


    }

    @Override
    protected void onStart() {

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){

            mUserRef.child("online").setValue(true);
        }


        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){

            mUserRef.child("online").setValue(false);
        }

        if(firebaseRecyclerAdapter!=null)
        firebaseRecyclerAdapter.stopListening();
    }

    @Override
    protected void onPause() {
        super.onPause();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){

            mUserRef.child("online").setValue(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}

