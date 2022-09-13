package com.example.emailauthapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    AlertDialog.Builder reset_alert;
    LayoutInflater inflater;
    FirebaseAuth firebaseAuth;
    TextView verifymessage;
    Button verifyemailbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseAuth.getInstance();
        Button logout = findViewById(R.id.logoutBtn);
        verifyemailbtn = findViewById(R.id.verifyemailbtn);
        verifymessage = findViewById(R.id.verifyemailmsg);

        if(!firebaseAuth.getCurrentUser().isEmailVerified()){
            verifyemailbtn.setVisibility(View.VISIBLE);
            verifymessage.setVisibility(View.VISIBLE);
        }

        verifyemailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send verification email
                firebaseAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Verification Email is Sent", Toast.LENGTH_SHORT).show();
                        verifyemailbtn.setVisibility(View.GONE);
                        verifymessage.setVisibility(View.GONE);
                    }
                });
            }

        });


        reset_alert = new AlertDialog.Builder(this);
        inflater = this.getLayoutInflater();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.resetUserPassword) {
            startActivity(new Intent(getApplicationContext(), ResetPassword.class));
        }


            if(item.getItemId() == R.id.updateemailmenu){
                final View view = inflater.inflate(R.layout.reset_pop, null);
                reset_alert.setTitle("Update Email")
                        .setMessage("Enter New Email Address")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //validate the email address

                                EditText email = view.findViewById(R.id.resetemailpop);
                                if(email.getText().toString().isEmpty()){
                                    email.setError("Requied Field");
                                    return;
                                }

                                //send reset link
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                user.updateEmail(email.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(MainActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                        }).setNegativeButton("Cancel", null)
                        .setView(view)
                        .create().show();
            }


        return super.onOptionsItemSelected(item);
    }

}