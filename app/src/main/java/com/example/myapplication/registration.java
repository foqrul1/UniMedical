package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class registration extends AppCompatActivity {
    TextView backtoLogin;
    Button dctor_signup, ptn_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        backtoLogin = findViewById(R.id.backtoLogin);
        dctor_signup = findViewById(R.id.dctor_signup);
        ptn_signup = findViewById(R.id.patient_signup);
        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        dctor_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, doctor.class);
                startActivity(intent);
            }
        });
        ptn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(registration.this, patient.class);
                startActivity(intent);
            }
        });
    }
}