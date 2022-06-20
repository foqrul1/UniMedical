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
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class patient extends AppCompatActivity {

    TextView backtoLoginfrompatnRegistartion;
    TextInputEditText patientRegistrationName, patientRegistrationNumber, patientRegistrationEmail, patientRegistrationMobile, patientRegistrationPass;
    Button patientReg;
    Spinner Department;
    CircleImageView profileImage;
    Uri resultUri;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private ProgressDialog loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);

        backtoLoginfrompatnRegistartion = findViewById(R.id.backtoLoginfrompatnRegistartion);

        patientRegistrationName = findViewById(R.id.patientRegistrationName);
        patientRegistrationNumber = findViewById(R.id.patientRegistrationNumber);
        patientRegistrationEmail = findViewById(R.id.patientRegistrationEmail);
        patientRegistrationMobile = findViewById(R.id.patientRegistrationMobile);
        patientRegistrationPass = findViewById(R.id.patientRegistrationPass);
        patientReg = findViewById(R.id.patientReg);
        Department = findViewById(R.id.Department);
        profileImage = findViewById(R.id.profileImage);
        loader = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        backtoLoginfrompatnRegistartion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(patient.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(intent, 1);
            }
        });
        patientReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Name = patientRegistrationName.getText().toString().trim();
                final String RegNum = patientRegistrationNumber.getText().toString().trim();
                final String Email = patientRegistrationEmail.getText().toString().trim();
                final String Mobile = patientRegistrationMobile.getText().toString().trim();
                final String Pass = patientRegistrationPass.getText().toString().trim();
                final String Dept = Department.getSelectedItem().toString();

                if (TextUtils.isEmpty(Name)){
                    patientRegistrationName.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(RegNum)){
                    patientRegistrationNumber.setError("Reg Num is Required");
                    return;
                }
                if (TextUtils.isEmpty(Email)){
                    patientRegistrationEmail.setError("Email Address is required");
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()){
                    patientRegistrationEmail.setError("Enter Valid Email Address");
                    return;
                }
                if (TextUtils.isEmpty(Mobile)){
                    patientRegistrationMobile.setError("Mobile Number is Required");
                    return;
                }
                if (TextUtils.isEmpty(Pass)){
                    patientRegistrationEmail.setError("Password is Required");
                    return;
                }
                if (Department.getSelectedItemPosition() < 0){
                    Toast.makeText(patient.this, "Department is required", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(resultUri==null){
                    Toast.makeText(patient.this, "Profile Image is required", Toast.LENGTH_SHORT).show();
                }else{
                    loader.setMessage("Registration on Progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                String Error = task.getException().toString();
                                Toast.makeText(patient.this, "Error Occured "+Error, Toast.LENGTH_SHORT).show();

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
                                userInfo.put("Dept", Dept);
                                userInfo.put("type", "patient");

                                databaseReference.updateChildren(userInfo).
                                        addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(patient.this, "Details Add Successfully", Toast.LENGTH_LONG).show();
                                                }else{
                                                    Toast.makeText(patient.this, task.getException().toString(), Toast.LENGTH_LONG).show();
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
                                                                    Toast.makeText(patient.this,
                                                                            "Regestration Success", Toast.LENGTH_SHORT).show();
                                                                }else {
                                                                    Toast.makeText(patient.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                        finish();
                                                    }
                                                });
                                            }
                                        }
                                    });
                                    Intent intent = new Intent(patient.this, MainActivity.class);
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