package com.drozee.drozeebvest;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BooksListAdapter extends ArrayAdapter<Books>{
    private Activity context;
    private List<Books> booksList;

    public BooksListAdapter(@NonNull Activity context, List<Books> booksList) {
        super(context, R.layout.listbooks, booksList);
        this.context = context;
        this.booksList = booksList;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater ();
        View listViewItem = inflater.inflate (R.layout.listbooks,null,true);

//        Button butt =listViewItem.findViewById(R.id.button2);

        TextView textViewItemName = (TextView) listViewItem.findViewById (R.id.textView3);
//        TextView textViewPlaceName = (TextView) listViewItem.findViewById (R.id.text_Place_Name);
        TextView textViewStatus = (TextView) listViewItem.findViewById (R.id.textView4);
//        TextView textView =listViewItem.findViewById(R.id.schedule2);
//        TextView ab =listViewItem.findViewById(R.id.text_status3);

//        Appliances appliances = appliancesList.get (position);

        Books books = booksList.get(position);
        textViewItemName.setText (books.getBookname());
//        textViewPlaceName.setText (appliances.getPlaceName ());
        textViewStatus.setText (books.getAuthor ());

//        ab.setText(appliances.getType());

//        textView.setText(appliances.getSchedule());

//        butt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                View parentRow = (View) view.getParent();
//                ListView listView= (ListView) parentRow.getParent();
//                final int position = listView.getPositionForView(parentRow);
//                Main3Activity m= new Main3Activity();
//
//                m.aaa(position);
//            }
//        });

        return listViewItem;
    }
}
