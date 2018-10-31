package com.drozee.drozeebvest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsSignup extends AppCompatActivity {
    public Bitmap cropImg;
    @BindView(R.id.editText)
    EditText name;
    @BindView(R.id.editText2)
    EditText college;
    @BindView(R.id.editText3)
    EditText year;
    @BindView(R.id.imageButton)
    Button imageButton;
    @BindView(R.id.imageView3)
    ImageView prof;
    public int flag = 0, Main = 0;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.editText7)
    EditText editText7;

    private Uri filefront, fileback, file, file1;
    private StorageReference mStorage;
    public String useriD;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase, dbtest;
    private DatabaseReference mUserReference, mProfile, drtest, mBook, mEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_signup);
        ButterKnife.bind(this);
//        Toast.makeText(getApplicationContext(),"still Details",Toast.LENGTH_SHORT).show();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        allowpermissioncamera();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//        buttonSkip = (Button) findViewById(R.id.button_Skip);
//        nextActivityGo = findViewById(R.id.textView5);
        if (mAuth.getCurrentUser() != null) {
            useriD = mAuth.getCurrentUser().getUid();
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mUserReference = mFirebaseDatabase.getReference("UserDetails").child(mAuth.getCurrentUser().getUid());
            mProfile = mFirebaseDatabase.getReference("profilePic").child(mAuth.getCurrentUser().getUid());
            mBook = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());
            mEmail = mFirebaseDatabase.getReference("emailanduid").child(mAuth.getCurrentUser().getUid());

            dbtest = FirebaseDatabase.getInstance();
            drtest = dbtest.getReference("loggedInBefore").child(mAuth.getCurrentUser().getUid());
        } else {
            startActivity(new Intent(this, SignUp.class));
        }

//        mUserReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if(dataSnapshot!=null){
//                    Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_LONG).show();
//                    startActivity(new Intent(DetailsSignup.this,IDLoginup.class));
//                    finish();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

//        if(mFirebaseDatabase!=null){
//            Toast.makeText(getApplicationContext(),"not null",Toast.LENGTH_LONG).show();
//            startActivity(new Intent(this,IDLoginup.class));
//            finish();
//        }
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ContextCompat.checkSelfPermission(DetailsSignup.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(DetailsSignup.this, "Please allow permission for storage", Toast.LENGTH_LONG).show();
                    allowpermissionstorage();
                } else if (ContextCompat.checkSelfPermission(DetailsSignup.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(DetailsSignup.this, "Please allow permission for storage", Toast.LENGTH_LONG).show();
                    allowpermissionstorage();

                } else {
                    showFileChooser();
                }

            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = name.getText().toString();
                String c = college.getText().toString();
                String y = year.getText().toString();
                String p = editText7.getText().toString();
                if (n.isEmpty()) {
                    name.setError("Name is required");
                    name.requestFocus();
                    return;
                }
                if (c.isEmpty()) {
                    college.setError("College's Name is required");
                    college.requestFocus();
                    return;
                }
                if (y.isEmpty()) {
                    year.setError("College Year is required");
                    year.requestFocus();
                    return;
                }
                if (p.isEmpty()) {
                    editText7.setError("College Year is required");
                    editText7.requestFocus();
                    return;
                }
//                mUserReference.child("name").setValue(n);
//                mUserReference.child("college").setValue(c);
//                mUserReference.child("college year").setValue(y);

                profileclass profileclass = new profileclass(n, c, y, p);
                mUserReference.setValue(profileclass);
                mEmail.child("email").setValue(mAuth.getCurrentUser().getEmail());
                mEmail.child("uid").setValue(mAuth.getCurrentUser().getUid());
                mEmail.child("name").setValue(n);
                mEmail.child("college").setValue(c);
                mEmail.child("year").setValue(y);
                mEmail.child("status").setValue("not paid");



                uploadFile();

            }
        });
    }

    private void allowpermissionstorage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }

    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/").putExtra(Intent.EXTRA_LOCAL_ONLY,true);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, 100);
    }

    private void allowpermissioncamera() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                prof.setEnabled(true);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {


            file = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), file);

                prof.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            prof.setImageURI(file);
        }

    }

    private void uploadFile() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Picture....");
        progressDialog.setCancelable(false);
        StorageReference riversRef = mStorage.child(mAuth.getCurrentUser().getUid()).child("profile");


        if (file == null) {
//            Toast.makeText(this,"no pic",Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(DetailsSignup.this,IDLoginup.class));
//            finish();
            flag = 0;
            mProfile.setValue(flag);
            changeactivity();
        }
        if (file != null) {
            progressDialog.show();
            riversRef.putFile(file)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();
                            flag = 1;
                            mProfile.setValue(flag);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% Uploaded.. ");
                    if (progress == 100) {

                        //checking if idcard exists
                        changeactivity();
//                        startActivity(new Intent(DetailsSignup.this,IDLoginup.class));
//                        finish();
                    }
                }
            });

        }
    }


    void changeactivity() {
        drtest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    mBook.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                if (Main == 0) {
                                    Log.e("Details", "*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%", null);
                                    Main = 1;
                                    startActivity(new Intent(DetailsSignup.this, MainActivitN.class));
                                    finish();
                                }
                            } else {
                                if (Main == 0) {
                                    startActivity(new Intent(DetailsSignup.this, Book.class));
                                    finish();
                                    Main = 2;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    startActivity(new Intent(DetailsSignup.this, IDLoginup.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
