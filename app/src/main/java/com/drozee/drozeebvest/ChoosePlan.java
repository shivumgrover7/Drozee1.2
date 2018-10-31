package com.drozee.drozeebvest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChoosePlan extends AppCompatActivity {

    @BindView(R.id.imageButton)
    Button imageButton;
    @BindView(R.id.imageButton3)
    Button imageButton3;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_plan);
        ButterKnife.bind(this);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Plan").child(mAuth.getCurrentUser().getUid());
    }

    @OnClick({R.id.imageButton, R.id.imageButton3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageButton:
                databaseReference.setValue(2);
                Toast.makeText(getApplicationContext(),"Thank you!! We will be contacting you regarding this soon",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,MainActivitN.class));
                finish();
                break;
            case R.id.imageButton3:
                databaseReference.setValue(1);
                Toast.makeText(getApplicationContext(),"Thank you!! We will be contacting you regarding this soon",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this,MainActivitN.class));
                finish();
                break;
        }
    }
}
