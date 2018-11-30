package com.example.neeraj.chatit;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ViewPager mViewPager;
    private SectionPagerAdapter mSectionPager;
    private TabLayout mTabLayout;
    private DatabaseReference mUserRef;

    private android.support.v7.widget.Toolbar mToolbar;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle("ChatIT");

        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("User").child(mAuth.getCurrentUser().getUid());

        }

        mViewPager = findViewById(R.id.main_viewpager);
        mSectionPager = new SectionPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mSectionPager);

        mTabLayout = findViewById(R.id.main_tablayout);
        mTabLayout.setupWithViewPager(mViewPager);

    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){

            sendtoStart();
        }
        else {

            mUserRef.child("online").setValue(true);

        }

    }
    protected void onPause() {
        super.onPause();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(false);

        }
    }


    private void sendtoStart() {
        Intent startIntent = new Intent(MainActivity.this,startactivity.class);
        startActivity(startIntent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu,menu);

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         super.onOptionsItemSelected(item);
         if(item.getItemId()==R.id.main_logout){
             FirebaseAuth.getInstance().signOut();
             sendtoStart();
         }
         if(item.getItemId()==R.id.main_account_set){
             Intent settingIntent = new Intent(MainActivity.this,Setting_activity.class);
             startActivity(settingIntent);
             finish();
         }
         if(item.getItemId()==R.id.main_all_user){
             Intent UserIntent = new Intent(MainActivity.this,UserActivity.class);
             startActivity(UserIntent);
         }

         return  true;
    }
}
