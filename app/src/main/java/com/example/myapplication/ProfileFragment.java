package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {
    CircleImageView imageView;
    Toolbar toolbar;
    FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    DatabaseReference reference;
    TextInputEditText UserProfileName, UserProfileEmail, UserProfileID, UserProfileType, UserProfileNumber;
    Button signOut;


    public ProfileFragment() {
        // Required empty public constructor

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.propfileImageIV);
        toolbar = view.findViewById(R.id.profile_toolbar2);
        UserProfileName = view.findViewById(R.id.UserProfileName);
        UserProfileEmail = view.findViewById(R.id.UserProfileEmail);
        UserProfileID = view.findViewById(R.id.UserProfileID);
        UserProfileType = view.findViewById(R.id.UserProfileDept);
        UserProfileNumber = view.findViewById(R.id.UserProfileNumber);
        signOut = view.findViewById(R.id.profileSingOutBTN);

       /* if (toolbar != null) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }*/
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertsignout();
            }
        });

        loadDataFromFirebase();




    }
    private void alertsignout() {
        AlertDialog.Builder alertDialog2 = new
                AlertDialog.Builder(
                getActivity());

        // Setting Dialog Title
        alertDialog2.setTitle("Confirm SignOut");

        // Setting Dialog Message
        alertDialog2.setMessage("Are you sure you want to Signout?");

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        mAuth.getInstance().signOut();
                        Intent i = new Intent(getActivity(),
                                LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                                Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                    }
                });

        // Setting Negative "NO" Btn
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dialog.cancel();
                    }
                });

        // Showing Alert Dialog
        alertDialog2.show();


    }

    private void loadDataFromFirebase() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mAuth = FirebaseAuth.getInstance();
        String current_userID = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference().child("users").child(current_userID);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("Name").getValue().toString();
                String Email = snapshot.child("Email").getValue().toString();
                String ID = snapshot.child("Reg").getValue().toString();
                String TYPE = snapshot.child("type").getValue().toString();
                String MobileNumber = snapshot.child("Phone").getValue().toString();

                UserProfileName.setText(Name);
                UserProfileType.setText(TYPE);
                UserProfileEmail.setText(Email);
                UserProfileID.setText(ID);
                UserProfileNumber.setText(MobileNumber);
                Glide.with(getContext())
                        .load(snapshot.child("profileImageUrl").getValue())
                        .placeholder(R.drawable.profile_pic)
                        .into(imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}