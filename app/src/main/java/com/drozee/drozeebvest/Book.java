package com.drozee.drozeebvest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Book extends AppCompatActivity {

    @BindView(R.id.editText5)
    EditText editText5;
    @BindView(R.id.editText6)
    EditText editText6;
    @BindView(R.id.imageButton2)
    Button imageButton2;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference,plan;
    int flag = 0,i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        ButterKnife.bind(this);
//        Toast.makeText(getApplicationContext(),"still book",Toast.LENGTH_SHORT).show();

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserReference = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());
        plan = mFirebaseDatabase.getReference("Plan").child(mAuth.getCurrentUser().getUid());

        plan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    i=1;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        changeactivity();
//        if (mUserReference!=null)
//        {
//            startActivity(new Intent(this,MainActivitN.class));
//            finish();
//        }
    }

    @OnClick(R.id.imageButton2)
    public void onViewClicked() {
        String bookname = editText5.getText().toString();
        String author = editText6.getText().toString();
        if(bookname.isEmpty ())
        {
            editText5.setError ("Book name is required");
            editText5.requestFocus ();
            return;
        }
//        mUserRefernces=mUserReference.push();
        mUserReference.child(bookname).child("bookname").setValue(bookname);
        mUserReference.child(bookname).child("author").setValue(author);
        if((i==1)||(i==2)){
        Log.e("Book","*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",null);
        startActivity(new Intent(this,MainActivitN.class));
        finish();}
        else {
            Log.e("Book","*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",null);
            startActivity(new Intent(this,ChoosePlan.class));
            finish();}

    }
    void changeactivity()
    {
        mUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    if(flag==0){
                    startActivity(new Intent(Book.this,MainActivitN.class));
                    finish();}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
