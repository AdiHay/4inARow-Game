package com.example.exoli.a4inarow.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.exoli.a4inarow.GameFragment;
import com.example.exoli.a4inarow.R;
import com.example.exoli.a4inarow.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WinningTableActivity extends AppCompatActivity {

    private TextView winTable;
    private RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private User user;
    //private FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winning_table);

        winTable = (TextView)findViewById(R.id.txt_win_table);
        recyclerView = (RecyclerView)findViewById(R.id.rv_game);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("games");
        user = (User)getIntent().getSerializableExtra("user");
        displayGames();
    }

    private void displayGames() {
        GameFragment gf = new GameFragment();
    }
}
