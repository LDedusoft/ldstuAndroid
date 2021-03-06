package com.ldedusoft.ldstu.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.model.RaceQuery;

import java.util.List;

/**
 * Created by wangjianwei on 2016/7/29.
 */
public class RaceQueryAdapter extends ArrayAdapter<RaceQuery> {
    private int resourceId;
    private TextView raceName,raceTime,raceXianshi,raceScore,raceRounds;
    private LinearLayout raceTypeLayout;
    private Context mContext;
    private RaceQuery item;

    public RaceQueryAdapter(Context context, int textViewResourceId, List<RaceQuery> objects) {
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
            raceName = (TextView)view.findViewById(R.id.race_query_name);
            raceTime = (TextView)view.findViewById(R.id.race_query_time);
            raceXianshi = (TextView)view.findViewById(R.id.race_query_xianshi);
            raceScore = (TextView)view.findViewById(R.id.race_query_score);
            raceRounds = (TextView)view.findViewById(R.id.race_query_rounds);
            raceName.setText(item.Name);
            raceTime.setText(item.StartTime);
            raceXianshi.setText(item.LimitTime);
            raceScore.setText(item.Qzfen);
            raceRounds.setText(item.TestId);
        }
        return view;
    }
}
