package com.example.exoli.a4inarow.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.exoli.a4inarow.R;
import com.example.exoli.a4inarow.WinningTableActivity;
import com.example.exoli.a4inarow.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class GameModeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private Button btnSamePhoneMode;
    private Button btnWebMode;
    private Button btnVsComputer;
    private Button.OnClickListener ocl;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        bindUI();

        user = (User) getIntent().getSerializableExtra("user");

        ocl = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                switch (id) {
                    case R.id.btn_2players_same:
                        mode1on1SamePhone();
                        break;
                    case R.id.btn_2players_web:
                        mode1on1Web();
                        break;
                    case R.id.btn_1vscomputer:
                        mode1VsComputer();
                        break;
                    case R.id.btn_winningtable:
                        modeWinningTable();
                }
            }
        };

        btnSamePhoneMode.setOnClickListener(ocl);
        btnWebMode.setOnClickListener(ocl);
        btnVsComputer.setOnClickListener(ocl);
    }

    private void bindUI() {
        btnSamePhoneMode = (Button)findViewById(R.id.btn_2players_same);
        btnWebMode = (Button)findViewById(R.id.btn_2players_web);
        btnVsComputer = (Button)findViewById(R.id.btn_1vscomputer);
    }

    private void mode1on1SamePhone() {
        Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
        intent.putExtra("host", user);
        intent.putExtra("guest", User.guest());
        startActivity(intent);
    }

    private void mode1on1Web() {
        Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    private void mode1VsComputer() {
        Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
        intent.putExtra("host", user);
        startActivity(intent);
    }

    private void modeWinningTable() {
        Intent intent = new Intent(GameModeActivity.this, WinningTableActivity.class);
        startActivity(intent);
    }

}
