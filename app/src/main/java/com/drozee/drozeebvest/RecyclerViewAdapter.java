package com.drozee.drozeebvest;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<Books> booksList;

    public RecyclerViewAdapter(Context c, List<Books> mbooksList) {
        mContext = c;
        booksList = mbooksList;
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView pref;
        TextView author;

        public MyViewHolder(View view) {
            super(view);
            pref = (TextView) view.findViewById(R.id.prefTV);
            author = (TextView)view.findViewById(R.id.authorTV);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.listbooks, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        TextView tv = holder.itemView.findViewById(R.id.textView3);
        TextView auth = holder.itemView.findViewById(R.id.textView4);
        Books books = booksList.get(position);

        tv.setText(books.getBookname());
        auth.setText(books.getAuthor());

    }

    @Override
    public int getItemCount() {
        return booksList.size();
    }
}
