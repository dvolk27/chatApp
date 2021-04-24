package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private Button logout;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        logout.setOnClickListener((o)->{
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this,AuthActivity.class);
            startActivity(intent);
        });
        recyclerView = findViewById(R.id.recyclerView);
        ChatAdapter adapter = new ChatAdapter();
    }
}
class ChatAdapter extends RecyclerView.Adapter<MessageHolder> {

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class MessageHolder extends RecyclerView.ViewHolder{
    private TextView username;
    private TextView message;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.username);
        message = itemView.findViewById(R.id.)
    }
}