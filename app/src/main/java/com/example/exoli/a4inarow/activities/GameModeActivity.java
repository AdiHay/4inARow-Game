package com.example.exoli.a4inarow.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.exoli.a4inarow.R;
import com.example.exoli.a4inarow.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class GameModeActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private Button btnSamePhoneMode;
    private Button btnWebMode;
    private Button btnVsAIEasy;
    private Button btnVsAINormal;
    private Button btnVsAIHard;
    private Button btnWinTable;
    private Button.OnClickListener ocl;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        bindUI();

        user = (User) getIntent().getSerializableExtra(getString(R.string.user));

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
                    case R.id.btn_1vs_comp_easy:
                        mode1VsAI(0);
                        break;
                    case R.id.btn_1vs_comp_normal:
                        mode1VsAI(1);
                        break;
                    case R.id.btn_1vs_comp_hard:
                        mode1VsAI(2);
                        break;
                    case R.id.btn_winningtable:
                        modeWinningTable();
                }
            }
        };

        btnSamePhoneMode.setOnClickListener(ocl);
        btnWebMode.setOnClickListener(ocl);
        btnVsAIEasy.setOnClickListener(ocl);
        btnVsAINormal.setOnClickListener(ocl);
        btnVsAIHard.setOnClickListener(ocl);
        btnWinTable.setOnClickListener(ocl);
    }

    private void bindUI() {
        btnSamePhoneMode = (Button)findViewById(R.id.btn_2players_same);
        btnWebMode = (Button)findViewById(R.id.btn_2players_web);
        btnVsAIEasy = (Button)findViewById(R.id.btn_1vs_comp_easy);
        btnVsAINormal = (Button)findViewById(R.id.btn_1vs_comp_normal);
        btnVsAIHard = (Button)findViewById(R.id.btn_1vs_comp_hard);
        btnWinTable = (Button)findViewById(R.id.btn_winningtable);
    }

    private void mode1on1SamePhone() {
        Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
        intent.putExtra(getString(R.string.ai_mode), false);
        intent.putExtra(getString(R.string.host), user);
        intent.putExtra(getString(R.string.guest), User.guest());
        startActivity(intent);
    }

    private void mode1on1Web() {
        Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
        intent.putExtra(getString(R.string.ai_mode), false);
        intent.putExtra(getString(R.string.user), user);
        startActivity(intent);
    }

    private void mode1VsAI(int level) {
        Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
        intent.putExtra(getString(R.string.ai_mode), true);
        intent.putExtra(getString(R.string.level), level);
        intent.putExtra(getString(R.string.host), user);
        startActivity(intent);
    }

    private void modeWinningTable() {
        Intent intent = new Intent(GameModeActivity.this, WinningTableActivity.class);
        intent.putExtra(getString(R.string.user), user);
        startActivity(intent);
    }

}
