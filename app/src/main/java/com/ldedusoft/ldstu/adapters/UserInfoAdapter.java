package com.ldedusoft.ldstu.adapters;

import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.model.InputItem;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wangjianwei on 2016/6/29.
 */
public class UserInfoAdapter extends ArrayAdapter<InputItem> {
    private int resourceId;
    private TextView titleText;
    private TextView valueText;
    private TextView checkText;
    private RadioGroup radioGroup;
    private RadioButton maleRadio,femaleRadio;
    private EditText editText;
    private InputItem item;
    private Activity mContext;
    private EditText password;
    private int touchedPosition=-1;//缓存当前位置
    //用来存放EditText的值，Key是position
    SparseArray<String> textHashMap = new SparseArray<String>();
    //用来存放数据对象，Key是position
    HashMap<Integer, InputItem> inputItemMap = new HashMap<Integer, InputItem>();

    public UserInfoAdapter(Activity context, int textViewResourceId, List<InputItem> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
        mContext = context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        item = getItem(position);
        inputItemMap.put(position,item);
        View view;
        if(convertView==null){
        view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        }else {
            view = convertView;
        }

        if(item!=null){
            titleText = (TextView)view.findViewById(R.id.userinfo_item_title);
            valueText = (TextView)view.findViewById(R.id.userinfo_item_value);
            checkText = (TextView)view.findViewById(R.id.textClick_user_item);
            radioGroup = (RadioGroup)view.findViewById(R.id.raidoGroup_user_item);
            maleRadio = (RadioButton)view.findViewById(R.id.radioButton_user_male);
            femaleRadio = (RadioButton)view.findViewById(R.id.radioButton_user_female);
            editText = (EditText)view.findViewById(R.id.edit_user_item);
            password = (EditText)view.findViewById(R.id.password_user_item);

            titleText.setText(item.title);
            if("用户".equals(item.title)){
                valueText.setVisibility(View.VISIBLE);
                valueText.setText(item.value);
            }else if("考生姓名".equals(item.title)) {
                editText.setVisibility(View.VISIBLE);
                editText.setText(item.value);
            }else if("密码".equals(item.title)){
                password.setVisibility(View.VISIBLE);
                password.setText(item.value);
            }else if("确认密码".equals(item.title)){
                password.setVisibility(View.VISIBLE);
                password.setText(item.value);
            }else  if("学校".equals(item.title)){
                checkText.setVisibility(View.VISIBLE);
                checkText.setText(item.value);
                checkText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("activity.SchoolSelectActivity");
                        intent.putExtra("needBack", "true");
                        intent.putExtra("position", position);
                        mContext.startActivityForResult(intent, 2);
                    }
                });
            }else if("性别".equals(item.title)){
                radioGroup.setVisibility(View.VISIBLE);
                if("男".equals(item.value)){
                    maleRadio.setChecked(true);
                    femaleRadio.setChecked(false);
                }else if("女".equals(item.value)){
                    maleRadio.setChecked(false);
                    femaleRadio.setChecked(true);
                }
            }

            //输入文字改变后保存到map中，防止滚动后错乱
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    //将editText中改变的值设置的HashMap中
                    textHashMap.put(position, s.toString());
                    InputItem tempItem = inputItemMap.get(position);
                    tempItem.value = s.toString();
                }
            });
            //输入文字改变后保存到map中，防止滚动后错乱
            password.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    //将editText中改变的值设置的HashMap中
                    textHashMap.put(position, s.toString());
                    InputItem tempItem = inputItemMap.get(position);
                    tempItem.value = s.toString();
                }
            });
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        touchedPosition = position;
                    }
                    return false;
                }
            });
            password.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        touchedPosition = position;
                    }
                    return false;
                }
            });

        }
        //如果hashMap不为空，就设置editText
        if(textHashMap.get(position) != null){
            editText.setText(textHashMap.get(position));
            password.setText(textHashMap.get(position));
            item.value=textHashMap.get(position);
        }
        if (touchedPosition!=-1 && touchedPosition == position) {
            // 如果当前的行下标和点击事件中保存的index一致，手动为EditText设置焦点。
            editText.requestFocus();
            editText.setSelection(editText.getText().length());
            password.requestFocus();
            password.setSelection(password.getText().length());
        }else {
            editText.clearFocus();
            password.clearFocus();

        }
        return view;
    }
}
