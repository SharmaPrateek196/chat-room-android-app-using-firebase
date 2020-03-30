package com.example.prateek_sharma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.common.ConnectionResult;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class ChatRoom extends AppCompatActivity {

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    EditText et_msg;
    Button btn_send;
    RecyclerView recyclerView;
    ArrayList<MessagePojo> list;
    private ChatAdapter mAdapter;
    private String message;

    private DatabaseReference mRef;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        gso =  new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(ChatRoom.this, "Connection failed", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        progressDialog = new ProgressDialog(ChatRoom.this);
        progressDialog.setMessage("Loading ongoing chats");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
        et_msg = (EditText) findViewById(R.id.et_msg);
        btn_send = (Button) findViewById(R.id.btn_send);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        list = new ArrayList<>();

        mRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ChatAdapter(ChatRoom.this, list);
        recyclerView.setAdapter(mAdapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message = et_msg.getText().toString();
                if (message.equals(""))
                    Toast.makeText(ChatRoom.this, "Please type some message", Toast.LENGTH_SHORT).show();
                else
                    sendMessage(message);
            }
        });

        mRef.child("chat").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                        progressDialog.dismiss();
                        MessagePojo messagePojo = dataSnapshot.getValue(MessagePojo.class);

                        list.add(messagePojo);
                        mAdapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(list.size() - 1);
                    } catch (Exception ex) {
                        Toast.makeText(ChatRoom.this, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ChatRoom.this, "Error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void sendMessage(String message) {
        if (mRef != null && mAuth.getCurrentUser() != null) {
            String sender = mAuth.getCurrentUser().getDisplayName();
            MessagePojo messagePojo = new MessagePojo(sender, message, new Date().getTime());
            mRef.child("chat").push().setValue(messagePojo);
            Toast.makeText(this, "Sent", Toast.LENGTH_SHORT).show();
            et_msg.setText("");
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out) {
            FirebaseAuth.getInstance().signOut();
            GoogleSignIn.getClient(ChatRoom.this,gso).revokeAccess();
            Intent intent=new Intent(ChatRoom.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
}