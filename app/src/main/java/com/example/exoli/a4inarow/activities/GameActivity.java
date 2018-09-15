package com.example.exoli.a4inarow.activities;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.exoli.a4inarow.BoardView;
import com.example.exoli.a4inarow.GameRules;
import com.example.exoli.a4inarow.GameplayControl;
import com.example.exoli.a4inarow.R;
import com.example.exoli.a4inarow.classes.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class GameActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private User user;
    private GameplayControl gameplayControl;
    private final GameRules gameRules = new GameRules();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        user = (User) getIntent().getSerializableExtra(getString(R.string.user));

        BoardView boardView = (BoardView) findViewById(R.id.gameView);
        gameRules.importFrom(getIntent().getExtras());
        gameplayControl = new GameplayControl(this, boardView, gameRules);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_action_close);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                showAlert(R.string.back);
                break;
            case R.id.restart:
                showAlert(R.string.reset_game);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(final int msgId) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(msgId)
                .setCancelable(false)
                .setNegativeButton(R.string.no, null)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (msgId == R.string.back) {
                            gameplayControl.exitGame();
                        } else {
                            gameplayControl.restartGame();
                        }
                    }
                }).show();
    }

    @Override
    public void onBackPressed() {
        showAlert(R.string.back);
    }
}
