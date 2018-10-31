package com.drozee.drozeebvest;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mListReference;
    FirebaseAuth mAuth;
    String user;
    RecyclerView recyclerView;
    List<Books> booklist;
    RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(this, booklist);
    public EditText prefET;

    public EditText authET;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);
        mAuth =  FirebaseAuth.getInstance();
        booklist = new ArrayList<> ();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListReference = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());
         recyclerView = (RecyclerView) findViewById(R.id.prefRV);
         RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
         recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(viewAdapter);
        user = mAuth.getUid();
        final Button add = (Button)findViewById(R.id.addBTN);
        final Button submit = (Button)findViewById(R.id.addSub);

//        final Button submit = (Button)findViewById(R.id.submitBTN);
          prefET = (EditText)findViewById(R.id.prefET);
        authET = (EditText)findViewById(R.id.authorET);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booklist.size()>3){
                startActivity(new Intent(PreferencesActivity.this, Thanks.class));}
                else Toast.makeText(getApplicationContext(),"Please add atleast 5 books",Toast.LENGTH_LONG).show();
            }
        });
//        submit.setEnabled(false);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addbook();
//                if(authET.getText().toString()!= "" && prefET.getText().toString()!= ""){
//                pref.add(prefET.getText().toString() + "," + authET.getText());
//                prefET.setText("");
//                authET.setText("");
//                viewAdapter.notifyDataSetChanged();
//                if(viewAdapter.getItemCount() >= 5)
//                {
////                    submit.setBackgroundResource(R.drawable.edittext);
//                    submit.setEnabled(true);
//                if (prefET.getText().toString().equals("") || authET.getText().toString().equals("")) {
//                    Toast.makeText(PreferencesActivity.this, "Either field cannot be blank", Toast.LENGTH_SHORT).show();
//                }
//                else
//                {
//                    pref.add(prefET.getText().toString() + "," + authET.getText().toString());
//                    prefET.setText("");
//                    authET.setText("");
//                    viewAdapter.notifyDataSetChanged();
//                    if (viewAdapter.getItemCount() >= 5)
//                    {
//                        submit.setBackgroundResource(R.drawable.edittext);
//                    }
//                }
//            }}
        }});
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(viewAdapter.getItemCount() >= 5)
//                {
//              if(viewAdapter.getItemCount() == 1)
//                        mListReference.child("Books").child(user).push().setValue(pref);
//                    else
//                        mListReference.child("Books").child(user).setValue(pref);
//                    Toast.makeText(PreferencesActivity.this, "Uploading Data", Toast.LENGTH_SHORT).show();
//
//                }
//                else
//                {
//                    Toast.makeText(PreferencesActivity.this, "Please enter at least 5 books", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });





        //Set Custom Font
        Typeface myCustomFont = Typeface.createFromAsset(getAssets(), "CaviarDreams_Bold.ttf");
        add.setTypeface(myCustomFont);
        submit.setTypeface(myCustomFont);
    }


    @Override
    protected void onStart() {
        super.onStart ( );
        mListReference.addValueEventListener (new ValueEventListener( ) {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                booklist.clear ();
                for (DataSnapshot applianceSnapshot : dataSnapshot.getChildren ()){

                    Books bookslista = applianceSnapshot.getValue (Books.class);
                    booklist.add (bookslista);
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter (PreferencesActivity.this, booklist);
                recyclerView.setAdapter (adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void addbook() {
        String bookname = prefET.getText().toString();
        String author = authET.getText().toString();
        if(prefET.getText().toString().equals("") || authET.getText().toString().equals("")) {
            Toast.makeText(PreferencesActivity.this, "Book or Author name cannot be empty", Toast.LENGTH_SHORT).show();
        }
        else
        {
            prefET.setText("");
            authET.setText("");
            Books books = new Books(bookname, author);
            mListReference.child(bookname).setValue(books);
            Toast.makeText(this, "Book added", Toast.LENGTH_LONG).show();

        }
    }
}
