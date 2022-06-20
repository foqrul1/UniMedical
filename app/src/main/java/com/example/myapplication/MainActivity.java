package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements FragmentControllerInterface{

    BottomNavigationView navView;


    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference2;
    public FirebaseUser currentUser;
    String calledBy = "", user_uID;
    private static final int RC_VIDEO_APP_PERM = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.bottom_nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemReselectedListener);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, new HomeFragment()).commit();


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        requestPermissions();
        if (currentUser != null) {
            user_uID = mAuth.getCurrentUser().getUid();

            userDatabaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(user_uID);

        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }


    }
    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemReselectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()) {

                case R.id.navigation_home:
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new HomeFragment())
                            //.addToBackStack(null)
                            .commit();
                    break;


                case R.id.navigation_call:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new DiscussFragment())
                            .addToBackStack(null)
                            .commit();

                    break;

                case R.id.navigation_status:

                case R.id.navigation_profile:

                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new ProfileFragment())
                            .addToBackStack(null)
                            .commit();
                    break;

            }
            return true;
        }
    };

    @Override
    public void gotoStatusFragment() {

    }

    @Override
    public void gotoAddStatusFragment() {

    }

    @Override
    public void gotoChatFragmnets(String userId) {

    }

    @Override
    public void gotoProfileFragment() {

    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, MainActivity.this);
    }


    @AfterPermissionGranted(RC_VIDEO_APP_PERM)
    private void requestPermissions(){
        String [] perms = {Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

        if (EasyPermissions.hasPermissions(this, perms)){

        }
        else {
            EasyPermissions.requestPermissions(this, "Hey! this app needs Mic and Camera, Please allow.", RC_VIDEO_APP_PERM, perms);

        }
    }

}