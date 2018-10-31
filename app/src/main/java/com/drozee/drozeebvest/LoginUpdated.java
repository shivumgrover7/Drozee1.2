package com.drozee.drozeebvest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class LoginUpdated extends AppCompatActivity {

    @BindView(R.id.email)
    EditText editText;
    @BindView(R.id.password)
    EditText editText4;
    @BindView(R.id.imageButton)
    Button imageButton;

    FirebaseAuth mAuth;
    FirebaseStorage mStorage;
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mUserReference,mId,mBk;
    String firstLogin;
    ProgressBar progressBar;
    @BindView(R.id.forgot)
    TextView textView;
    @BindView(R.id.signUp)
    TextView textView2;


    private CheckBox checkbox;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginupdated2);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Madeleina Sans.otf");
        mStorage = FirebaseStorage.getInstance();
//        imageButton.setTypeface(typeface);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        editText.setAlpha(0.9f);
//        editText4.setAlpha(0.9f);
//        imageButton.setAlpha(0.9f);
        progressBar = findViewById(R.id.top_progress_bar);
        progressBar.setVisibility(View.GONE);


//        checkbox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean checked = ((CheckBox) view).isChecked();
//                if(checked)
//                {
//                    Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

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
                        Toast.makeText(LoginUpdated.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                        mUserReference = mFirebaseDatabase.getReference("UserDetails").child(mAuth.getCurrentUser().getUid());
                        mId = mFirebaseDatabase.getReference("loggedInBefore").child(mAuth.getCurrentUser().getUid());
                        mBk = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());

                        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    startActivity(new Intent(LoginUpdated.this, DetailsSignup.class));
                                    finish();
                                } else if (dataSnapshot.getValue() != null) {
                                    mId.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue()!=null){
                                                mBk.addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.getValue()!=null){
                                                            startActivity(new Intent(LoginUpdated.this, MainActivitN.class));
                                                            finish();
                                                        }
                                                        else {
                                                            startActivity(new Intent(LoginUpdated.this, Book.class));
                                                            finish();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });


                                            }
                                            else{
                                                startActivity(new Intent(LoginUpdated.this, IDLoginup.class));
                                                finish();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

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

    @OnClick({R.id.forgot, R.id.signUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forgot:
                startActivity(new Intent(LoginUpdated.this,Forgot.class));
                break;
            case R.id.signUp:
                startActivity(new Intent(LoginUpdated.this,SignUp.class));

                break;
        }
    }

}
