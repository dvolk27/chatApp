package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Button logout;

    private FirebaseAuth myAuth;
    private Database database;
    private RecyclerView recyclerView;
    private EditText input;
    private Button send;
    private String username;
    private static ChatAdapter adapter;
    private Thread thread1;
    private Thread thread2;
    public MainActivity(){}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        myAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        send = findViewById(R.id.send);
        input = findViewById(R.id.input);
        recyclerView = findViewById(R.id.recyclerView);

        database = new Database();

        database.connect(1);


        username = "";
        adapter = new ChatAdapter();

        recyclerView.setAdapter(adapter);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(layoutManager);
        thread2 = new Thread(() -> {
            int count = 0;
            while(true) {
                if(count!=recyclerView.getAdapter().getItemCount()) {
                    count = recyclerView.getAdapter().getItemCount();
                    recyclerView.smoothScrollToPosition(count-1);
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread2.start();
        if(username.equals("")) {
            thread1 = new Thread(() -> {
                while(username.equals("")) {
                    try {
                        username = database.getUsername();
                        Log.d("dan",username);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            thread1.start();
        }

        logout.setOnClickListener((o)->{
            myAuth.signOut();
            Intent intent = new Intent(MainActivity.this,AuthActivity.class);
            startActivity(intent);
        });

        send.setOnClickListener((o)->{
            if(!input.getText().toString().equals("")) {
                Message message = new Message(username,input.getText().toString());
                database.sendMessage(message);
                input.setText("");
                adapter.update(database.getMessages());
            }
        });

    }

    public static ChatAdapter getAdapter() {
        return adapter;
    }

    @Override
    protected void onStop() {
        super.onStop();
        while (thread1.isAlive() && thread2.isAlive()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class ChatAdapter extends RecyclerView.Adapter<MessageHolder> {
    private ArrayList<Message> messages;
    public ChatAdapter(){
        messages = new ArrayList<>();
    }
    public ChatAdapter(ArrayList<Message> messages) {
        this.messages = messages;
    }
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message,parent,false);
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.setMessage(messages.get(position).getMessage());
        holder.setUsername(messages.get(position).getUsername());
    }

    @Override
    public int getItemCount() {
        if (messages != null) {return messages.size();}
        else  {
            return 0;
        }

    }
    public  void update(ArrayList<Message> messages){
        this.messages.clear();
        this.messages.addAll(messages);
        notifyDataSetChanged();
    }

}
class MessageHolder extends RecyclerView.ViewHolder{
    private TextView username;
    private TextView message;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
        username = itemView.findViewById(R.id.username);
        message = itemView.findViewById(R.id.input);
    }

    public void setUsername(String username) {
        this.username.setText(username);
    }

    public void setMessage(String message) {
        this.message.setText(message);
    }
}