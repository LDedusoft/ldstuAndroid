package com.ldedusoft.ldstu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;

import java.util.List;

/**
 * Created by wangjianwei on 2016/7/29.
 */
public class SchoolAdapter extends ArrayAdapter<String> {
    private int resourceId;
    private TextView schoolName;
    private String item;

    public SchoolAdapter(Context context, int textViewResourceId, List<String> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        item = getItem(position);
        View view;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }else {
            view = convertView;
        }
        if(item!=null){
            schoolName = (TextView)view.findViewById(R.id.school_item_value);
            schoolName.setText(item);
        }
        return view;
    }
}
