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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    EditText registerFullName, registerEmail, registerPassword, registerCnfrmPassword;
    Button registerBtn, gotologin;
    FirebaseAuth fireAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerFullName = findViewById(R.id.registerfullname);
        registerEmail = findViewById(R.id.registeremail);
        registerPassword = findViewById(R.id.registerpassword);
        registerCnfrmPassword = findViewById(R.id.confirmpassword);
        registerBtn = findViewById(R.id.registerbutton);
        gotologin = findViewById(R.id.gotoLogin);

        fireAuth = FirebaseAuth.getInstance();
        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //extract the data from the form

                String fullName = registerFullName.getText().toString();
                String email = registerEmail.getText().toString();
                String password = registerPassword.getText().toString();
                String cnfrmpassword = registerCnfrmPassword.getText().toString();
                if(fullName.isEmpty())
                {
                    registerFullName.setError("Required Full Name");
                    return;
                }
                if(email.isEmpty())
                {
                    registerEmail.setError("Required E-Mail");
                    return;
                }
                if(password.isEmpty())
                {
                    registerPassword.setError("Enter Password");
                    return;
                }
                if(cnfrmpassword.isEmpty())
                {
                    registerCnfrmPassword.setError("Re-Enter Password");
                    return;
                }
                if (!password.equals(cnfrmpassword)){
                    registerCnfrmPassword.setError("Password Does Not Match");
                    return;
                }
                //data is validated
                Toast.makeText(Register.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                fireAuth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //send user to next page
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}