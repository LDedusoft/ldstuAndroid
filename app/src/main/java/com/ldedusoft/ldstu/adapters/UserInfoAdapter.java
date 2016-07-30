package com.ldedusoft.ldstu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.model.InputItem;

import java.util.List;

/**
 * Created by wangjianwei on 2016/6/29.
 */
public class UserInfoAdapter extends ArrayAdapter<InputItem> {
    private int resourceId;
    private TextView titleText;
    private TextView valueText;
    private InputItem item;

    public UserInfoAdapter(Context context, int textViewResourceId, List<InputItem> objects) {
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
            titleText = (TextView)view.findViewById(R.id.userinfo_item_title);
            valueText = (TextView)view.findViewById(R.id.userinfo_item_value);
            titleText.setText(item.title);
            valueText.setText(item.value);
        }
        return view;
    }
}
