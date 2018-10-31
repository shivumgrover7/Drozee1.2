package com.drozee.drozeebvest;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class BooksFragments extends Fragment {


//    @BindView(R.id.editText5)
//    EditText editText5;
//    @BindView(R.id.editText6)
//    EditText editText6;
//    @BindView(R.id.imageButton2)
//    Button imageButton2;
    Unbinder unbinder;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserReference;
    private DatabaseReference mListReference;
    RecyclerView recyclerView;
    List<Books> booklist;
    private EditText editBookName,editAuthorName;
    private Button buttonAdd,buttonShow;
    RecyclerViewAdapter viewAdapter = new RecyclerViewAdapter(getActivity(), booklist);


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bookfragment,container, false);
        unbinder = ButterKnife.bind(this, view);
        buttonShow = (Button) view.findViewById(R.id.imageButton2);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                View mview = getLayoutInflater ().inflate (R.layout.add_dialog,null);

                editBookName = (EditText) mview.findViewById(R.id.edit_book);
                editAuthorName = (EditText) mview.findViewById(R.id.edit_author);
               buttonAdd = (Button) mview.findViewById(R.id.button_add);

               editBookName.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       editBookName.requestFocusFromTouch();
                   }
               });
               editAuthorName.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       editAuthorName.requestFocusFromTouch();
                   }
               });

               buttonAdd.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View view) {
                       Books newbook = new Books(editBookName.getText().toString(),editAuthorName.getText().toString());
                       mListReference.child(editBookName.getText().toString()).setValue(newbook);
                       editBookName.setText("");
                       editAuthorName.setText("");

                   }
               });
                alert.setView (mview);
                AlertDialog dialog = alert.create ();
                dialog.show ();



            }
        });

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        booklist = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mListReference = mFirebaseDatabase.getReference("Books").child(mAuth.getCurrentUser().getUid());
        recyclerView = (RecyclerView) view.findViewById(R.id.prefRV);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setAdapter(viewAdapter);
        String user = mAuth.getCurrentUser().getUid();

//        editText5.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editText5.requestFocusFromTouch();
//            }
//        });
//        editText6.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editText6.requestFocusFromTouch();
//            }
//        });

    }

    @Override
    public void onStart() {
        super.onStart();
        mListReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                booklist.clear();
                for (DataSnapshot applianceSnapshot : dataSnapshot.getChildren()) {

                    Books bookslist = applianceSnapshot.getValue(Books.class);
                    booklist.add(bookslist);
                }
                RecyclerViewAdapter adapter = new RecyclerViewAdapter(getActivity(), booklist);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.imageButton2)
    public void onViewClicked() {
//        Books newbook = new Books(editText5.getText().toString(),editText6.getText().toString());
//        mListReference.child(editText5.getText().toString()).setValue(newbook);
//        editText5.setText("");
//        editText6.setText("");
        Toast.makeText(getActivity(),"hello",Toast.LENGTH_SHORT).show();
    }


//    @OnClick(R.id.imageButton3)
//    public void onViewClicked() {
//        mAuth.signOut();
//    }
}
