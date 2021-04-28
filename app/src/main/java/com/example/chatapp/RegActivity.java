package com.example.chatapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_reg);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        submit = findViewById(R.id.submitReg);
        toAuth = findViewById(R.id.toAuth);
        usernameInput = findViewById(R.id.usernameInput);

        usernameInput.setHint("username");
        password.setHint("password");
        email.setHint("email");

        database = new Database();
        database.connect(0);

        toAuth.setOnClickListener((v)->{
            Intent intent = new Intent(RegActivity.this, AuthActivity.class);
            startActivity(intent);
        });
        submit.setOnClickListener((o)->{
            username = usernameInput.getText().toString();
            createAccount(email.getText().toString(),password.getText().toString());
        });

    }
    private void createAccount(String email, String password) {
        if(email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            Toast.makeText(RegActivity.this, "you need fill all fields",
                    Toast.LENGTH_SHORT).show();
        } else {
            if(username.length() < 11) {
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
                } else {
                Toast.makeText(RegActivity.this,"username length must be less then 11",Toast.LENGTH_SHORT).show();
            }

        }
    }
    private void updateUI(FirebaseUser currentUser) {
        if(currentUser !=  null) {
            Intent intent =  new Intent(RegActivity.this,MainActivity.class);
            startActivity(intent);
        }
    }
}
