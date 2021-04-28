package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "dan";
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button submit;
    private Button toRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.authform);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.authEmail);
        password = findViewById(R.id.authPassword);
        submit = findViewById(R.id.submitAuth);
        toRegistration = findViewById(R.id.toRegistration);

        password.setHint("password");
        email.setHint("email");

        toRegistration.setOnClickListener((v)->{
            Intent intent = new Intent(AuthActivity.this, RegActivity.class);
            startActivity(intent);
        });
        submit.setOnClickListener((o)->{
            signIn(email.getText().toString(),password.getText().toString());
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
}

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser !=  null) {
            Intent intent =  new Intent(AuthActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
    public void signIn(String email,String password) {
        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(AuthActivity.this, "you need fill all fields",
                    Toast.LENGTH_SHORT).show();
        } else {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(AuthActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        }
} }