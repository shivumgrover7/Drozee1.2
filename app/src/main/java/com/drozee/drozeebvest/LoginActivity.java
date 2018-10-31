package com.drozee.drozeebvest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText4)
    EditText editText4;
    @BindView(R.id.imageButton)
    Button imageButton;
    @BindView(R.id.imageView)
    ImageView image;
    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mUserReference;
    String firstLogin;
    ProgressBar progressBar;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.textView2)
    TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Madeleina Sans.otf");
        mStorage = FirebaseStorage.getInstance();
        imageButton.setTypeface(typeface);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        image.setAlpha(50);
        editText.setAlpha(0.9f);
        editText4.setAlpha(0.9f);
        imageButton.setAlpha(0.9f);
        progressBar = findViewById(R.id.top_progress_bar);
        progressBar.setVisibility(View.GONE);

    }

    @OnClick(R.id.imageButton)
    public void onViewClicked() {
        //startActivity(new Intent(this, PreferencesActivity.class));
        loginuser();

    }

    private void loginuser() {
        final String emailstring = editText.getText().toString().trim();

        // to get Password from user and store it in variable called Password
        final String PassWord = editText4.getText().toString().trim();
        if (emailstring.isEmpty()) {
            //set an error
            editText.setError("Email is required");
            //and highlight that box
            editText.requestFocus();
            return;
        }
        if (PassWord.isEmpty()) {
            //set an error
            editText4.setError("Password is required");
            //it will focus on password
            editText4.requestFocus();
            return;
        }
        if (PassWord.length() < 6) {
            editText4.setError("Minimum length of password required is 6");
            editText4.requestFocus();
            return;
        }
        if (Patterns.EMAIL_ADDRESS.matcher(emailstring).matches()) {
            progressBar.setVisibility(View.VISIBLE);

            mAuth.signInWithEmailAndPassword(emailstring, PassWord).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(LoginActivity.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        mUserReference = mFirebaseDatabase.getReference("loggedInBefore").child(mAuth.getCurrentUser().getUid());
                        mUserReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    startActivity(new Intent(LoginActivity.this, IDLogin.class));
                                    finish();
                                } else if (dataSnapshot.getValue() != null) {
                                    if (dataSnapshot.getValue(Boolean.class)) {
                                        startActivity(new Intent(LoginActivity.this, PreferencesActivity.class));
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //if login is successful then
//                        Intent intent = new Intent (LoginActivity.this, IDLogin.class);
//                        intent .addFlags (intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity (intent);
//                        Toast.makeText (getApplicationContext (),"log in",Toast.LENGTH_SHORT).show ();

                    } else {
                        progressBar.setVisibility (View.GONE);

                        //else
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @OnClick({R.id.textView, R.id.textView2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.textView:
                startActivity(new Intent(LoginActivity.this,Forgot.class));
                break;
            case R.id.textView2:
                startActivity(new Intent(LoginActivity.this,SignUpMessage.class));

                break;
        }
    }
}
