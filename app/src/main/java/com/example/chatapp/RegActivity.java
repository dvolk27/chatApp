package com.example.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegActivity extends AppCompatActivity {
    private static final String TAG = "dan";
    private FirebaseAuth mAuth;
    private EditText email;
    private EditText password;
    private Button submit;
    private Button toAuth;
    private EditText usernameInput;
    private Database database;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        submit = findViewById(R.id.submitReg);
        toAuth = findViewById(R.id.toAuth);
        database = new Database();

        database.connect();

        usernameInput = findViewById(R.id.usernameInput);
        toAuth.setOnClickListener((v)->{
            Intent intent = new Intent(RegActivity.this, AuthActivity.class);
            startActivity(intent);
        });
        submit.setOnClickListener((o)->{
            createAccount(email.getText().toString(),password.getText().toString());
            username = usernameInput.getText().toString();
        });

    }
    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            database.addUser(username);
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser !=  null) {
            Intent intent =  new Intent(RegActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
