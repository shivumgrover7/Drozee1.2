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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Forgot extends AppCompatActivity {

    @BindView(R.id.editText4)
    EditText editText4;
    @BindView(R.id.imageButton)
    Button imageButton;
    @BindView(R.id.signUp)
    TextView textView10;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgotupdated);
        ButterKnife.bind(this);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Madeleina Sans.otf");

//        imageButton.setTypeface(typeface);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        progressBar.setVisibility(View.GONE);

    }

    @OnClick({R.id.imageButton, R.id.signUp})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                if (editText4.getText().toString().isEmpty()) {
                    //set an error
                    editText4.setError("Email is required");
                    //and highlight that box
                    editText4.requestFocus();
                    return;
                }
                if (Patterns.EMAIL_ADDRESS.matcher(editText4.getText().toString()).matches()) {
                    progressBar.setVisibility(View.VISIBLE);
                FirebaseAuth.getInstance().sendPasswordResetEmail(editText4.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext(),"Email sent with link to reset password",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(Forgot.this,LoginUpdated.class));
                                    Log.d("Status password", "Email sent.");
                                }
                            }
                        });}
                break;
            case R.id.signUp:
                startActivity(new Intent(Forgot.this,SignUp.class));
                break;
        }
    }
}
