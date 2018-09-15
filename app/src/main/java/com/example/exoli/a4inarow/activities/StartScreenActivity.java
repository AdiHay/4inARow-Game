package com.example.exoli.a4inarow.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exoli.a4inarow.R;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StartScreenActivity extends AppCompatActivity {

    //private static final String TAG = "EmailPassword";
    private TextView txtGameName;
    private TextView txtEmail;
    private EditText edtEmail;
    private TextView txtPassword;
    private EditText edtUserPassword;
    private Button btnSignIn;
    private Button btnSignUp;
    private FirebaseAuth mAuth;
    private DatabaseReference db;
    private Intent intent;
    private final static int MINIMUM_PASSWORD_LENGTH = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        bindUI();

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference(getString(R.string.users));
        intent = new Intent(StartScreenActivity.this, GameModeActivity.class);
    }

    private void bindUI() {
        txtGameName = (TextView)findViewById(R.id.txt_game_name);
        txtEmail = (TextView)findViewById(R.id.txt_email_signin);
        edtEmail = (EditText)findViewById(R.id.edt_email);
        txtPassword = (TextView)findViewById(R.id.txt_pass_signin);
        edtUserPassword = (EditText)findViewById(R.id.edt_pass);
        btnSignIn = (Button)findViewById(R.id.btn_signin);
        btnSignUp = (Button)findViewById(R.id.btn_signup);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = edtEmail.getText().toString().trim();
                final String password = edtUserPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    edtEmail.setError(getString(R.string.email_length_error));
                    return;
                }

                if (password.length() < MINIMUM_PASSWORD_LENGTH) {
                    edtUserPassword.setError(getString(R.string.pass_min_msg));
                    return;
                }

                findUser(email, password);
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreenActivity.this, SignUpActivity.class));
            }
        });
    }

    private void findUser(final String email, final String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(StartScreenActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            if (password.length() < MINIMUM_PASSWORD_LENGTH) {
                                edtUserPassword.setError(getString(R.string.pass_min_msg));
                            } else {
                                Toast.makeText(StartScreenActivity.this, getString(R.string.login_signup_error), Toast.LENGTH_LONG).show();
                            }
                        } else {

                            String transMail = email.replace(getString(R.string.dot), getString(R.string.underline));
                            db.child(transMail).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User theUser = dataSnapshot.getValue(User.class);

                                    intent.putExtra(getString(R.string.user), theUser);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

}
