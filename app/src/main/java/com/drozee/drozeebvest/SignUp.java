package com.drozee.drozeebvest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.utils.L;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUp extends AppCompatActivity {

    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.imageButton)
    Button imageButton;
    @BindView(R.id.already)
    TextView already;
    @BindView(R.id.forgot)
    TextView forgot;
    private String url = "http://drozee.com/terms&conditions";
    private Button butAgree,butdisagree;
    private CheckBox checkBox;
    private FirebaseAuth mAuth;
    public String email, password, confirm, sessionID;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference, mID, mBook,plan;
    public int flag = 0;

    //    public FirebaseUser user;
    public DatabaseReference databaseReference;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginupdated);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        Toast.makeText(getApplicationContext(),"still sign up",Toast.LENGTH_SHORT).show();

//        checkBox = (CheckBox) findViewById(R.id.check);
//
//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(checkBox.isChecked()){
//                    Intent intent = new Intent(Intent.ACTION_VIEW);
//                    intent.setData(Uri.parse(url));
//                    startActivity(intent);
//
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Please click on terms and conditions",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
        ButterKnife.bind(this);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (user != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Checking for existing user");
            progressDialog.setMessage("Please wait");
            progressDialog.setCancelable(false);
            progressDialog.show();

            Log.e("emailnot", "sendEmailVerification", null);

            mFirebaseDatabase = FirebaseDatabase.getInstance();

            mUserReference = mFirebaseDatabase.getReference("UserDetails").child(user.getUid());
            mID = mFirebaseDatabase.getReference("loggedInBefore").child(mAuth.getCurrentUser().getUid());
            mBook = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());
            plan = FirebaseDatabase.getInstance().getReference("Plan").child(mAuth.getCurrentUser().getUid());

            mUserReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                        mID.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {

                                    mBook.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.getValue() != null) {
                                                { if(flag==0){
                                                    plan.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.getValue()!=null)
                                                            {
                                                                {Log.e("SignUp",flag+"***********************************************************",null);
                                                                    flag = 1;
                                                                    startActivity(new Intent(SignUp.this, MainActivitN.class));
                                                                    finish();}
                                                            }
                                                            else{
                                                                {Log.e("SignUp",flag+"@@@@@@@@@@@@@",null);
                                                                    flag = 1;
                                                                    startActivity(new Intent(SignUp.this, ChoosePlan.class));
                                                                    finish();}
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                    }
                                                }
                                            } else {
                                                if(flag==0){

                                                    Log.e("SignUp","%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",null);
                                                flag = 2;
                                                startActivity(new Intent(SignUp.this, Book.class));
                                                finish();}
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                } else {
                                    if(flag==0){

                                        Log.e("SignUp","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",null);
                                    flag = 4;
                                    startActivity(new Intent(SignUp.this, IDLoginup.class));
                                    finish();}
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    } else {
                            if(flag==0){

                                Log.e("SignUp","ASSSSSSSSSSDSDJASDJASJDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD",null);
                        flag = 3;
                        startActivity(new Intent(SignUp.this, DetailsSignup.class));
                        finish();}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            Toast.makeText(getApplicationContext(), "user signed in", Toast.LENGTH_SHORT).show();


        }
    forgot.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SignUp.this,Forgot.class));

        }
    });

    already.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(SignUp.this,LoginUpdated.class));
        }
    });
    }

    @OnClick(R.id.imageButton)
    public void onViewClicked() {
        email = editText.getText().toString();
        password = editText2.getText().toString();
        confirm = editText3.getText().toString();


        if (email.isEmpty()) {
            editText.setError("Email is required");
            editText.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            editText2.setError("Password is required");
            editText2.requestFocus();
            return;
        }
        if (password.length() < 6) {
            editText2.setError("Minimum length of password required is 6");
            editText2.requestFocus();
            return;
        }
        if (!(password.equals(confirm))) {
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder (SignUp.this);
        View mview = getLayoutInflater ().inflate (R.layout.termdialog,null);

       // checkBox = (CheckBox) mview.findViewById(R.id.check);

//        checkBox.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean checked = ((CheckBox) view).isChecked();
//                if (checked)
//                {
////                    Intent intent = new Intent(LoginUpdated.this, DetailsSignup.class);
////                    startActivity(intent);
//                    setSignupa();
//
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(),"Please agree terms and conditions", Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
        butAgree = (Button) mview.findViewById(R.id.buttonAgree);
        butdisagree = (Button) mview.findViewById(R.id.buttonReject);
        butAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSignupa();
            }
        });
        butdisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Please agree terms and conditions", Toast.LENGTH_SHORT).show();

                }
        });
        builder.setView (mview);
        AlertDialog dialog = builder.create ();
        dialog.show ();
//        boolean checked = checkBox.isChecked();
//        if(checked)
//        {
//            setSignupa();
//        }
//        else
//        {
//            Toast.makeText(getApplicationContext(),"Please select terms and conditions",Toast.LENGTH_SHORT).show();
//        }



    }

    private void setSignupa() {
        email = editText.getText().toString();
        password = editText2.getText().toString();
        confirm = editText3.getText().toString();


//        if (email.isEmpty()) {
//            editText.setError("Email is required");
//            editText.requestFocus();
//            return;
//        }
//        if (password.isEmpty()) {
//            editText2.setError("Password is required");
//            editText2.requestFocus();
//            return;
//        }
//        if (password.length() < 6) {
//            editText2.setError("Minimum length of password required is 6");
//            editText2.requestFocus();
//            return;
//        }
//        if (!(password.equals(confirm))) {
//            Toast.makeText(this, "Password does match can't", Toast.LENGTH_SHORT).show();
//            return;
//        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                progressBar.setVisibility (View.GONE);
                if (task.isSuccessful()) {
                    String UserID = mAuth.getCurrentUser().getUid();
                    Toast.makeText(getApplicationContext(), "You are registered", Toast.LENGTH_SHORT).show();

                    user = mAuth.getCurrentUser();
                    user.sendEmailVerification()
                            .addOnCompleteListener(SignUp.this, new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    // Re-enable button
//                                    findViewById(R.id.verify_email_button).setEnabled(true);

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUp.this,
                                                "Verification email sent to " + user.getEmail(),
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.e("emailnot", "sendEmailVerification", task.getException());
                                        Toast.makeText(SignUp.this,
                                                "Failed to send verification email.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

//                    Users users = new Users(email,false,UserID,false);
//                    databaseReference.child(UserID).setValue (users);
//

                    startActivity(new Intent(SignUp.this, DetailsSignup.class));
                    finish();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });


    }
}
