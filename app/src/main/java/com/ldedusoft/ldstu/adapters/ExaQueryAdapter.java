package com.ldedusoft.ldstu.adapters;

import android.content.Context;
import android.text.TextUtils;
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
    private TextView exaName,exaTime,exaType,exaScore;
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
            exaName = (TextView)view.findViewById(R.id.exa_query_name);
            exaTime = (TextView)view.findViewById(R.id.exa_query_time);
            exaType = (TextView)view.findViewById(R.id.exa_query_type);
            exaScore = (TextView)view.findViewById(R.id.exa_query_score);
            exaTypeLayout = (LinearLayout)view.findViewById(R.id.exa_type_layout);
            if(TextUtils.isEmpty(item.type)){
                exaTypeLayout.setVisibility(View.INVISIBLE);
            }
            exaName.setText(item.name);
            exaTime.setText(item.time);
            exaType.setText(item.type);
            exaScore.setText(item.score);
        }
        return view;
    }
}
