package com.ldedusoft.ldstu.component.customComp;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.interfaces.FormToolBarListener;

/**
 * Created by wangjianwei on 2016/6/24.
 */
public class TopBar extends LinearLayout {
    private TextView back;
    private TextView backText;
    private LinearLayout backLayout;
    private TextView topBarTitle;
    private FormToolBarListener formToolBarListener;
    public TopBar(Context context, AttributeSet attrs){
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.ldstu_top_bar, this);
         topBarTitle = (TextView)findViewById(R.id.top_bar_title);
        backLayout = (LinearLayout)findViewById(R.id.form_tool_bar_back_layout);
        back  = (TextView)findViewById(R.id.form_tool_bar_back);
        backText = (TextView)findViewById(R.id.form_tool_bar_text);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formToolBarListener!=null){
                    formToolBarListener.OnBackClick();
                }
            }
        });

        backLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formToolBarListener!=null){
                    formToolBarListener.OnBackClick();
                }
            }
        });

        backText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formToolBarListener!=null){
                    formToolBarListener.OnBackClick();
                }
            }
        });
    }

    public void setFormToolBarListener(FormToolBarListener formToolBarListener) {
        this.formToolBarListener = formToolBarListener;
    }

    public void showBackBtn(){
        backLayout.setVisibility(VISIBLE);
    }

    public void setTitle(String title){
        topBarTitle.setText(title);
    }

}
