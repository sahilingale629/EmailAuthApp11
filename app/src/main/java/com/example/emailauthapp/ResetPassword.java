package com.example.emailauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPassword extends AppCompatActivity {
    EditText userpassword, userconfmpassword;
    Button savepasswordbtn;
    FirebaseUser  user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        userpassword = findViewById(R.id.newUserPassword);
        userconfmpassword = findViewById(R.id.confirmpassword);
        user = FirebaseAuth.getInstance().getCurrentUser();
        savepasswordbtn = findViewById(R.id.resetpassbtn);
        savepasswordbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userpassword.getText().toString().isEmpty()){
                    userpassword.setError("Required Field");
                    return;

                }
                if(userconfmpassword.getText().toString().isEmpty()){
                    userconfmpassword.setError("Required Field");
                    return;
                }

                if(!userpassword.getText().toString().equals(userconfmpassword.getText().toString())){
                    userconfmpassword.setError("The Password does not match");
                    return;

                }
                user.updatePassword(userpassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPassword.this, "Password Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPassword.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

}