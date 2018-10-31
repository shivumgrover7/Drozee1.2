package com.drozee.drozeebvest;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class IDLogin extends AppCompatActivity implements View.OnClickListener{

    private static final String INTENT_EXTRA_IMAGES = "10000";

    private Button buttonGallery, buttonCamera,buttonUpdate;
    private ImageView imageView;
    private Uri file;
    private StorageReference mStorage; public String useriD;
    private FirebaseAuth mAuth;
//    private TextView nextActivityGo;
//    private Button buttonSkip;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idlogin);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        buttonCamera = (Button) findViewById(R.id.button_photo);
        buttonGallery = (Button) findViewById(R.id.button_Gallery);
        imageView = (ImageView) findViewById(R.id.imageview);
        buttonUpdate = (Button) findViewById(R.id.button_update);
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//        buttonSkip = (Button) findViewById(R.id.button_Skip);
//        nextActivityGo = findViewById(R.id.textView5);
        useriD = mAuth.getCurrentUser().getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUserReference = mFirebaseDatabase.getReference("loggedInBefore").child(mAuth.getCurrentUser().getUid());
        if(mUserReference!=null){
            startActivity(new Intent(this,PreferencesActivity.class));
            finish();
        }
        Typeface typeface = Typeface.createFromAsset(getAssets(), "Madeleina Sans.otf");
        buttonCamera.setTypeface(typeface);
        buttonGallery.setTypeface(typeface);
        buttonUpdate.setTypeface(typeface);




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



        buttonGallery.setOnClickListener(this);
        buttonCamera.setOnClickListener(this);
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

        ActivityCompat.requestPermissions(this, new String[] {android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
        }, 100);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                buttonCamera.setEnabled(true);
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
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            imageView.setImageURI(file);
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

        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent , 100);
    }
    private void takePhoto(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = Uri.fromFile(getOutputMediaFile());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);

        startActivityForResult(intent,100);
    }

    private void uploadFile(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading....");
        StorageReference riversRef = mStorage.child(useriD).child("ICARDS");
        progressDialog.show();


        if(file!=null) {
            riversRef.putFile(file)
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
                    progressDialog.setMessage(((int) progress) + "% Uploaded.. ");
                    if(progress==100){
                        mUserReference.setValue(true);

                        startActivity(new Intent(IDLogin.this,PreferencesActivity.class));
                    }
                }
            });

        }
        }


    @Override
    public void onClick(View v) {
        if(v == buttonCamera){
            //choose camera
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Please allow permssion for camera",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                allowpermissioncamera();

            }
            else {
            takePhoto();}


        }
        else if(v == buttonGallery)
        {
            //choose gallery

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Please allow permssion for storage",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                allowpermissionstorage();
            }

            else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                Toast.makeText(this,"Please allow permssion for storage",Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, LoginActivity.class));
                allowpermissionstorage();

            }

            else{
            showFileChooser();
        }
        }
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
}
