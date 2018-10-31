package com.drozee.drozeebvest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.ImageView;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDLoginup extends AppCompatActivity implements View.OnClickListener{

    private static final String INTENT_EXTRA_IMAGES = "10000";
    public int i=0;
    private Button buttonGallery, buttonCamera,buttonUpdate;
    private ImageView imageView,imageView3;
    private Uri filefront,fileback,file;
    private StorageReference mStorage; public String useriD;
    private FirebaseAuth mAuth;
    int flag = 0;
//    private TextView nextActivityGo;
//    private Button buttonSkip;
    private FirebaseDatabase mFirebaseDatabase,dbtest;
    private DatabaseReference mUserReference,drtest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idloginup);
        Toast.makeText(getApplicationContext(),"still ID",Toast.LENGTH_SHORT).show();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


//        buttonCamera = (Button) findViewById(R.id.button_photo);
//        buttonGallery = (Button) findViewById(R.id.button_Gallery);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView3 = (ImageView) findViewById(R.id.imageView3);

        buttonUpdate = (Button) findViewById(R.id.imageButton);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//        buttonSkip = (Button) findViewById(R.id.button_Skip);
//        nextActivityGo = findViewById(R.id.textView5);
        useriD = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserReference = mFirebaseDatabase.getReference("loggedInBefore").child(mAuth.getCurrentUser().getUid());
        drtest = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());

//        changeactivity();

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Madeleina Sans.otf");
//        buttonCamera.setTypeface(typeface);
//        buttonGallery.setTypeface(typeface);
//        buttonUpdate.setTypeface(typeface);
//        if(mUserReference!=null){
//            startActivity(new Intent(this,Book.class));
//            finish();
//        }



//        buttonGallery.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/").putExtra(Intent.EXTRA_LOCAL_ONLY,true);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), TRUE_PHOTO_PICKER);
//            }
//        });
//
//
//        buttonCamera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                file = Uri.fromFile(getOutputMediaFile());
//
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
//
//                startActivityForResult(intent,100);
//            }
//        });
        allowpermissioncamera();

//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                == PackageManager.PERMISSION_DENIED){
//                Toast.makeText(getApplicationContext(),"denied camera",Toast.LENGTH_LONG).show();
//                allowpermissioncamera();
//        }
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_DENIED){
//                allowpermissionstorage();
//            Toast.makeText(getApplicationContext(),"denied storage",Toast.LENGTH_LONG).show();
//
//
//        }


//
//        buttonGallery.setOnClickListener(this);
        imageView.setOnClickListener(this);
        imageView3.setOnClickListener(this);

        buttonUpdate.setOnClickListener(this);
    }

//    private void allowpermissionstorageread() {
//        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
//
//    }
    private void allowpermissionstorage() {
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);

    }

    private void allowpermissioncamera() {

        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                imageView.setEnabled(true);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null && data.getData() != null) {

            if(i==1)
            filefront = data.getData();
            else fileback = data.getData();
            try {

                    Bitmap bitmap1 = MediaStore.Images.Media.getBitmap(getContentResolver(), filefront);
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), fileback);

                    imageView.setImageBitmap(bitmap1);


                    imageView3.setImageBitmap(bitmap2);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            if(i==1){
            imageView.setImageURI(filefront);}
            else{imageView3.setImageURI(fileback);}
        }

    }
    //                StorageReference filePath = mStorage.child("photos");
//                filePath.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(MainActivity.this, "UPLOADING IMAGE .... ", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//                imageView.setImageURI(file);

//                Uri selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//                    // Get the path from the Uri
//                    String path = getPathFromURI(selectedImageUri);
//                    //Log.i(TAG, "Image Path : " + path);
//                    // Set the image in ImageView
//                    ((ImageView) findViewById(R.id.imageview)).setImageURI(selectedImageUri);
//                    StorageReference filePath1 = mStorage.child("photos");
//                    filePath1.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                            Toast.makeText(MainActivity.this, "UPLOADING IMAGE .... ", Toast.LENGTH_LONG).show();
//                        }
//                    });
//                    imageView.setImageURI(selectedImageUri);








    private static File getOutputMediaFile () {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
    }
    private void showFileChooser(){
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT).setType("image/").putExtra(Intent.EXTRA_LOCAL_ONLY,true);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent , 100);
    }
    private void takePhoto(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(i==1) {
            filefront = Uri.fromFile(getOutputMediaFile());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, filefront);
        }
        else{
            fileback = Uri.fromFile(getOutputMediaFile());

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileback);
        }
        startActivityForResult(intent,100);
    }

    private void uploadFile(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading front image....");
        progressDialog.setCancelable(false);
        StorageReference riversRef = mStorage.child(useriD).child("ICARDS");

        if(filefront!=null && fileback!=null){
            progressDialog.show();

            if(filefront!=null) {
            riversRef.child("front").putFile(filefront)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();

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
                    progressDialog.setMessage("front image " + ((int) progress) + "% Uploaded.. ");
                    if(progress==100){
//                        startActivity(new Intent(IDLoginup.this,PreferencesActivity.class));
                    }
                }
            });

        }

        progressDialog.setTitle("Uploading back image....");
            progressDialog.setCancelable(false);
            progressDialog.show();

        if(fileback!=null) {
            riversRef.child("back").putFile(fileback)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "File Uploaded", Toast.LENGTH_SHORT).show();

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
                    progressDialog.setMessage("back image"+((int) progress) + "% Uploaded.. ");
                    if(progress==100){
                        mUserReference.child("value").setValue(true);
                        changeactivity();
                        finish();
                    }
                }
            });

        }
        }
        else Toast.makeText(getApplicationContext(),"please upload both pictures",Toast.LENGTH_SHORT).show();

    }


    @Override
    public void onClick(View v) {
        if(v == imageView){
            i = 1;
            //choose camera
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Please allow permssion for camera",Toast.LENGTH_LONG).show();
                allowpermissioncamera();

            }
            else {
            takePhoto();}


        }
        if(v == imageView3){
            i = 0;
            //choose camera
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Please allow permssion for camera",Toast.LENGTH_LONG).show();
                allowpermissioncamera();

            }
            else {
                takePhoto();}


        }
//        else if(v == buttonGallery)
//        {
//            //choose gallery
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_DENIED){
//                Toast.makeText(this,"Please allow permssion for storage",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(this, LoginActivity.class));
//                allowpermissionstorage();
//            }
//
//            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_DENIED){
//                Toast.makeText(this,"Please allow permssion for storage",Toast.LENGTH_LONG).show();
//                startActivity(new Intent(this, LoginActivity.class));
//                allowpermissionstorage();
//
//            }
//
//            else{
//            showFileChooser();
//        }
//        }
        else if(v== buttonUpdate){
           // startActivity(new Intent(this,PreferencesActivity.class));

        uploadFile();
    //            startActivity(new Intent(this,PreferencesActivity.class));
        }
//        else if(v==nextActivityGo)
//        {
//            Intent intent = new Intent(IDLogin.this,PreferencesActivity.class);
//            startActivity(intent);
//        }
    }
    void changeactivity()
    {
        drtest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    if (flag == 0){
                    Log.e("ID","*%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%",null);
                    flag =1;
                    startActivity(new Intent(IDLoginup.this,MainActivitN.class));
                    finish();}
                }else {
                    if(flag==0){
                        flag=2;
                        Log.e("ID","&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&",null);

                        startActivity(new Intent(IDLoginup.this, Book.class));
                    finish();}
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
