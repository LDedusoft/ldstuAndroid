package com.ldedusoft.ldstu.adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.model.ExeTypeModel;

import java.util.List;

/**
 * Created by wangjianwei on 2016/6/29.
 */
public class ExeTypeAdapter extends ArrayAdapter<ExeTypeModel> {
    private final String EXE_TYPE_NORMAL = "normal";
    private int resourceId;
    private TextView icon1Text;
    private TextView icon2Text;
    private TextView title1Text;
    private TextView title2Text;
    private ExeTypeModel item;
    private Context mContext;
    //用来存放EditText的值，Key是position
    SparseArray<String> textHashMap1 = new SparseArray<String>();
    SparseArray<String> textHashMap2 = new SparseArray<String>();

    public ExeTypeAdapter(Context context, int textViewResourceId, List<ExeTypeModel> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        item = getItem(position);
        View view;
        if(convertView==null){
        view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }else {
            view = convertView;
        }

        if(item!=null){
            textHashMap1.put(position,item.value1);
            textHashMap2.put(position,item.value2);
            icon1Text = (TextView)view.findViewById(R.id.exe_type_item_icon1);
            icon2Text = (TextView)view.findViewById(R.id.exe_type_item_icon2);
            title1Text = (TextView)view.findViewById(R.id.exe_type_item_title1);
            title2Text = (TextView)view.findViewById(R.id.exe_type_item_title2);
            try {
                Class<com.ldedusoft.ldstu.R.drawable> cls = R.drawable.class;  //图片类型
                int iconId1 = cls.getDeclaredField(item.icon1).getInt(null);
                icon1Text.setBackgroundDrawable(mContext.getResources().getDrawable(iconId1));
                int iconId2 = cls.getDeclaredField(item.icon2).getInt(null);
                icon2Text.setBackgroundDrawable(mContext.getResources().getDrawable(iconId2));
                title1Text.setText(item.value1);
                title2Text.setText(item.value2);

                if(TextUtils.isEmpty(item.value2)){
                    icon2Text.setVisibility(View.INVISIBLE);
                    title2Text.setVisibility(View.INVISIBLE);
                }

                icon1Text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("activity.answer.AnswerActivity");
                        intent.putExtra("title", textHashMap1.get(position));
                        intent.putExtra("param", EXE_TYPE_NORMAL);//normal
                        intent.putExtra("category", textHashMap1.get(position));
                        getContext().startActivity(intent);
                    }
                });
                title1Text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("activity.answer.AnswerActivity");
                        intent.putExtra("title", textHashMap1.get(position));
                        intent.putExtra("param", EXE_TYPE_NORMAL);//normal
                        intent.putExtra("category", textHashMap1.get(position));
                        getContext().startActivity(intent);
                    }
                });

                icon2Text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("activity.answer.AnswerActivity");
                        intent.putExtra("title",textHashMap2.get(position));
                        intent.putExtra("param",EXE_TYPE_NORMAL);//normal
                        intent.putExtra("category",textHashMap2.get(position));
                        getContext().startActivity(intent);
                    }
                });
                title2Text.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("activity.answer.AnswerActivity");
                        intent.putExtra("title", textHashMap2.get(position));
                        intent.putExtra("param", EXE_TYPE_NORMAL);//normal
                        intent.putExtra("category", textHashMap2.get(position));
                        getContext().startActivity(intent);
                    }
                });
            }catch (Exception e){e.printStackTrace();}
        }
        return view;
    }
}
