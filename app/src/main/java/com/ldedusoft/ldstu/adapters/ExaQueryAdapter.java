package com.ldedusoft.ldstu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.model.ExaQuery;

import java.util.List;

/**
 * Created by wangjianwei on 2016/7/29.
 */
public class ExaQueryAdapter extends ArrayAdapter<ExaQuery> {
    private int resourceId;
    private TextView raceName,sjName,exaTime,exaScore,exaZScore;
    private LinearLayout exaTypeLayout;
    private Context mContext;
    private ExaQuery item;

    public ExaQueryAdapter(Context context, int textViewResourceId, List<ExaQuery> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext = context;
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
            raceName = (TextView)view.findViewById(R.id.exa_query_name);
            sjName = (TextView)view.findViewById(R.id.exa_query_sjMingCheng);
            exaTime = (TextView)view.findViewById(R.id.exa_query_time);
            exaScore = (TextView)view.findViewById(R.id.exa_query_score);
            exaZScore = (TextView)view.findViewById(R.id.exa_query_Zscore);
            exaTypeLayout = (LinearLayout)view.findViewById(R.id.exa_type_layout);
            raceName.setText(item.raceName);
            sjName.setText(item.sjName);
            exaTime.setText(item.exaTime);
            exaScore.setText(item.exaScore);
            exaZScore.setText(item.exaZScore);
        }
        return view;
    }
}
