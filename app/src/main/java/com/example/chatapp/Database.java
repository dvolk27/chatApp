package com.example.chatapp;

import android.util.Log;
import android.util.Pair;
import com.example.chatapp.MainActivity.*;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import static android.content.ContentValues.TAG;

public class Database {
    private FirebaseDatabase database;
    private DatabaseReference messageRef;
    private DatabaseReference usersRef;
    private FirebaseAuth mAuth;
    private String username;
    private ArrayList<Message> messages;

    Database(){}
    public void connect(int connectionType) {
        if(connectionType == 0 || mAuth == null) {
            database = FirebaseDatabase.getInstance("https://authapp-1da6a-default-rtdb.europe-west1.firebasedatabase.app");
            mAuth = FirebaseAuth.getInstance();
            messageRef = database.getReference("Messages");
            usersRef = database.getReference("Users");
            username = "";
            messages = new ArrayList<>();
        }
        if(connectionType == 1 ) {
            messageRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    messages = new ArrayList<>();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
                            String message = iter.next().getValue().toString();
                            String username = iter.next().getValue().toString();
                            messages.add(new Message(username, message));
                        }

                    }
                    MainActivity.getAdapter().update(messages);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", databaseError.toException());
                }
            });

            usersRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {
                    GenericTypeIndicator<HashMap<String, String>> t = new GenericTypeIndicator<HashMap<String, String>>() {
                    };
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        for (DataSnapshot childChild : child.getChildren()) {
                            if (childChild.getValue().toString().equals(mAuth.getUid())) {
                                Iterator<DataSnapshot> iter = child.getChildren().iterator();
                                iter.next();
                                username = iter.next().getValue().toString();
                            }
                        }
                    }
                }
            });
        }
    }
    public  void sendMessage(Message message){
        messageRef.push().setValue(message);
    }
    public void addUser(String username) {
        usersRef.push().setValue(new Pair<String,String>(mAuth.getCurrentUser().getUid(),username));
    }
    public String getUsername()
    {
        return username;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }
    /*
    public ArrayList<Message> read() {
        ArrayList<Message> messages = new ArrayList<>();
        *//*
        for (Message message : ) {
        adapter.update(message);
        }*//*
        return messages;
    }*/

}
