package com.drozee.drozeebvest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ProfileFragment extends Fragment {
    @BindView(R.id.imageView3)
    ImageView imageView3;
    @BindView(R.id.editText)
    EditText editText;
    @BindView(R.id.editText2)
    EditText editText2;
    @BindView(R.id.editText3)
    EditText editText3;
    @BindView(R.id.imageButton)
    Button imageButton;
    Unbinder unbinder;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.editText7)
    EditText editText7;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference, mProfile;
    private StorageReference mStorage;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragent, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toast.makeText(getActivity(), "HEy", Toast.LENGTH_SHORT).show();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();
        mUserReference = mFirebaseDatabase.getReference("UserDetails").child(mAuth.getCurrentUser().getUid());
        mProfile = mFirebaseDatabase.getReference("profilePic").child(mAuth.getCurrentUser().getUid());

//        mUserReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//////
//////                    editText.setText(ds.child("name").toString());
//////                    Toast.makeText(getActivity(),ds.child("name").getValue().toString(),Toast.LENGTH_SHORT).show();
////
////
////
////                }
//                profileclass profileclas = dataSnapshot.getValue(profileclass.class);
//                editText.setText(profileclas.getName());
//                editText2.setText(profileclas.getCollege());
//                editText3.setText(profileclas.getYear());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
        mUserReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                profileclass profileclas = dataSnapshot.getValue(profileclass.class);
                editText.setText(profileclas.getName());
                editText2.setText(profileclas.getCollege());
                editText3.setText(profileclas.getYear());
                editText7.setText(profileclas.getPhone());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mProfile.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String flag = dataSnapshot.getValue().toString();
                if (flag.equals(1) || flag.equals("1"))
                    getfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.imageView3, R.id.imageButton})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.imageView3:

                break;
            case R.id.imageButton:

                profileclass profileclass = new profileclass(editText.getText().toString(), editText2.getText().toString(), editText3.getText().toString(),editText7.getText().toString());
                mUserReference.setValue(profileclass);
                Toast.makeText(getActivity(), "updated", Toast.LENGTH_SHORT).show();

                break;
        }
    }

    //    @OnClick(R.id.imageButton3)
//    public void onViewClicked() {
//        mAuth.signOut();
//    }
    void getfile() {
        try {
            final File localFile = File.createTempFile("images", "jpg");
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Getting picture....");
            progressDialog.setCancelable(false);

            progressDialog.show();
            mStorage.child(mAuth.getCurrentUser().getUid()).child("profile").getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    if (bitmap != null) {
                        Log.e("pic", "downloaded", null);
                        imageView3.setImageBitmap(bitmap);
                    }
                    Toast.makeText(getActivity(), "done", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    progressDialog.setMessage(((int) progress) + "% Downloaded.. ");

                }
            });
        } catch (IOException e) {
        }
    }
}
