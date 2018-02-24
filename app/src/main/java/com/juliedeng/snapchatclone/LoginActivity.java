package com.juliedeng.snapchatclone;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText login_email, login_password;
    private TextView forgot_password, signup;
    private Button login_button;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private final static String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_button = findViewById(R.id.login_button);
        forgot_password = findViewById(R.id.forgot_password_link);
        signup = findViewById(R.id.signup_link);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, user.toString());
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }
            }
        };

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (login_email.getText().length() == 0) {
                    Toast.makeText(LoginActivity.this, "Input an email.",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.sendPasswordResetEmail(login_email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this, "Email sent.",
                                            Toast.LENGTH_SHORT).show();
                                } else {
//                                    need better error message
                                    Toast.makeText(LoginActivity.this, "Failed to send. Make sure you have inputted a valid email.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignup();
            }
        });
        ConstraintLayout layout=(ConstraintLayout)findViewById(R.id.background);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                login_email.clearFocus();
                login_password.clearFocus();
                findViewById(R.id.background).requestFocus();
            }
        });
        login_email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
        login_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    private void attemptLogin() {
        String email = login_email.getText().toString();
        String password = login_password.getText().toString();
        Log.v(TAG, email + password);
        if (!email.equals("") && !password.equals("")) {
            Log.v(TAG, "ENTERED");
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.d(TAG, "attempt sign in");
                            if (task.isSuccessful()) {
                                Log.v(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                            } else {
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "noo");
                        }
                    });
        } else {
            Toast.makeText(LoginActivity.this, "All fields must be filled in.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void attemptSignup() {
        Log.d(TAG, "enter  func");
        if (login_email.getText().length() == 0) {
            Toast.makeText(LoginActivity.this, "Input an email.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (login_password.getText().length() == 0) {
            Toast.makeText(LoginActivity.this, "Input a password.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        String email = login_email.getText().toString();
        String password = login_password.getText().toString();
        if (password.length() < 6) {
            Log.d(TAG, "enter  bad pw check");
            Toast.makeText(LoginActivity.this, "Passwords must be longer than 6 characters.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d(TAG, "pass bad pw check");
        if (!email.equals("") && !password.equals("")) {

            Log.d(TAG, "enter  if statement");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                            if (!task.isSuccessful()) {
//                                check toast message
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(LoginActivity.this, "Successfully signed up! Log in.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            mAuth.addAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(LoginActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
