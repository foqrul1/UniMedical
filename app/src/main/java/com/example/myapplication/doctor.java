package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class doctor extends AppCompatActivity {

    TextView backtoLoginfromDctrRegistartion;
    CircleImageView profileImage;
    TextInputEditText DoctorRegistrationName, DoctorRegistrationNumber, DoctorRegistrationEmail, DoctorRegistrationMobile, DoctorRegistrationPass;
    Button DoctorReg;
    Spinner Availability, Speciality;

    Uri resultUri;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        backtoLoginfromDctrRegistartion = findViewById(R.id.backtoLoginfromDctrRegistartion);
        DoctorRegistrationName = findViewById(R.id.DoctorRegistrationName);
        DoctorRegistrationNumber = findViewById(R.id.DoctorRegistrationNumber);
        DoctorRegistrationEmail = findViewById(R.id.DoctorRegistrationEmail);
        DoctorRegistrationMobile = findViewById(R.id.DoctorRegistrationMobile);
        DoctorRegistrationPass = findViewById(R.id.DoctorRegistrationPass);
        DoctorReg = findViewById(R.id.DoctorReg);
        Availability = findViewById(R.id.Availability);
        Speciality = findViewById(R.id.Speciality);
        profileImage = findViewById(R.id.DoctorProfileImage);


        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 1);
            }
        });


        backtoLoginfromDctrRegistartion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(doctor.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        DoctorReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = DoctorRegistrationName.getText().toString().trim();
                final String RegNum = DoctorRegistrationNumber.getText().toString().trim();
                final String Email = DoctorRegistrationEmail.getText().toString().trim();
                final String Mobile = DoctorRegistrationMobile.getText().toString().trim();
                final String Pass = DoctorRegistrationPass.getText().toString().trim();
                final String available = Availability.getSelectedItem().toString();
                final String speciality = Speciality.getSelectedItem().toString();

                if (TextUtils.isEmpty(Name)){
                    DoctorRegistrationName.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(RegNum)){
                    DoctorRegistrationNumber.setError("Reg Num is Required");
                    return;
                }
                if (TextUtils.isEmpty(Email)){
                    DoctorRegistrationEmail.setError("Email Address is required");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    DoctorRegistrationEmail.setError("Enter Valid Email Address");
                    return;
                }
                if (TextUtils.isEmpty(Mobile)){
                    DoctorRegistrationMobile.setError("Mobile Number is Required");
                    return;
                }
                if (TextUtils.isEmpty(Pass)){
                    DoctorRegistrationEmail.setError("Password is Required");
                    return;
                }
                if (Availability.getSelectedItemPosition() < 0){
                    Toast.makeText(doctor.this, "Department is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(resultUri==null){
                    Toast.makeText(doctor.this, "Profile Image is required", Toast.LENGTH_SHORT).show();
                }else{
                    loader.setMessage("Registration on Progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                String Error = task.getException().toString();
                                Toast.makeText(doctor.this, "Error Occured "+Error, Toast.LENGTH_SHORT).show();

                            }else {
                                String currentUser = mAuth.getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference().
                                        child("users").child(currentUser);
                                HashMap userInfo = new HashMap();
                                userInfo.put("id", currentUser);
                                userInfo.put("Name", Name);
                                userInfo.put("Reg", RegNum);
                                userInfo.put("Email", Email);
                                userInfo.put("Phone", Mobile);
                                userInfo.put("Available", available);
                                userInfo.put("Special", speciality);
                                userInfo.put("type", "Doctor");

                                databaseReference.updateChildren(userInfo).
                                        addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(doctor.this, "Details Add Successfully", Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(doctor.this, task.getException().toString(), Toast.LENGTH_LONG).show();
                                                }
                                                finish();
                                                loader.dismiss();
                                            }
                                        });
                                if(resultUri != null){
                                    final StorageReference filepath =
                                            FirebaseStorage.getInstance().getReference().child("Profile Picture")
                                                    .child(currentUser);
                                    Bitmap bitmap = null;
                                    try {
                                        bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver()
                                                , resultUri);
                                    }catch (IOException e){
                                        e.printStackTrace();
                                    }
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                    byte[] data = byteArrayOutputStream.toByteArray();

                                    UploadTask uploadTask = filepath.putBytes(data);
                                    uploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            finish();
                                            return;
                                        }
                                    });
                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if(taskSnapshot.getMetadata() != null){
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        String ImageUrl = uri.toString();
                                                        Map newImageMap = new HashMap();
                                                        newImageMap.put("profileImageUrl", ImageUrl);

                                                        databaseReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                            @Override
                                                            public void onComplete(@NonNull Task task) {
                                                                if(task.isSuccessful()){
                                                                    Toast.makeText(doctor.this,
                                                                            "Regestration Success", Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(doctor.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(doctor.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                    loader.dismiss();

                                }

                            }
                        }
                    });
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode== Activity.RESULT_OK && data !=null){
            resultUri = data.getData();
            profileImage.setImageURI(resultUri);
        }
    }
}