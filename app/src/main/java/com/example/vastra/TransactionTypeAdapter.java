package com.example.vastra;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class TransactionTypeAdapter extends ArrayAdapter<TransactionType> {

    public TransactionTypeAdapter(Context context, List<TransactionType> types) {
        super(context, 0, types);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        TextView textViewTitle = convertView.findViewById(android.R.id.text1);
        TransactionType currentItem = getItem(position);

        if (currentItem != null) {
            textViewTitle.setText(currentItem.getTitle());
        }

        return convertView;
    }
}
