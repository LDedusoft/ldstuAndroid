package com.ldedusoft.ldstu.component.myAdapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ldedusoft.ldstu.interfaces.CommonNormalDetailListener;

/**
 * Created by wangjianwei on 2016/7/16.
 */
public class ViewHolder {
        private final SparseArray<View> mViews;
        private View mConvertView;

        private ViewHolder(Context context, ViewGroup parent, int layoutId,
                           int position)
        {
            this.mViews = new SparseArray<View>();
            mConvertView = LayoutInflater.from(context).inflate(layoutId, parent,
                    false);
            //setTag
            mConvertView.setTag(this);


        }

        /**
         * 拿到一个ViewHolder对象
         * @param context
         * @param convertView
         * @param parent
         * @param layoutId
         * @param position
         * @return
         */
    public static ViewHolder get(Context context, View convertView,
                                 ViewGroup parent, int layoutId, int position)
    {

        if (convertView == null)
        {
            return new ViewHolder(context, parent, layoutId, position);
        }
        return (ViewHolder) convertView.getTag();
    }


    /**
     * 通过控件的Id获取对于的控件，如果没有则加入views
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {

        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 为TextView设置字符串
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, String text)
    {
        TextView view = getView(viewId);
        view.setText(text);
        return this;
    }

    /**
     * 为TextView设置字符串(int型转换)
     *
     * @param viewId
     * @param text
     * @return
     */
    public ViewHolder setText(int viewId, int text)
    {
        TextView view = getView(viewId);
        view.setText(String.valueOf(text));
        return this;
    }

    /**
     * 隐藏组件
     * @param viewId
     * @param visType
     * @return
     */
    public ViewHolder hiddenLayout(int viewId,int visType){
        LinearLayout layout = getView(viewId);
        layout.setVisibility(visType);
        return this;
    }

    public ViewHolder setLayoutClickAction(int viewId, final String datasource,final String title, final CommonNormalDetailListener listener){
        LinearLayout layout = getView(viewId);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnDetailClick(datasource,title);
            }
        });
        return this;
    }

    public View getConvertView()
    {
        return mConvertView;
    }



}