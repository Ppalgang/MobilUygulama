package com.example.schoolmobileproject;

import android.content.Context;
import android.widget.ArrayAdapter;

public class CustomArrayAdapter<T> extends ArrayAdapter<T> {

    public CustomArrayAdapter(Context context, int resource, T[] objects){
        super(context, resource, objects);
    }

    public void updateData(T[] newData) {
        clear();
        addAll(newData);
        notifyDataSetChanged();
    }
}
