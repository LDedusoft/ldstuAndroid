package com.ldedusoft.ldstu.component.iconfont;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by wangjianwei on 2016/7/15.
 */
public class IconfontView extends TextView {



    public IconfontView(Context context) {
        super(context);
        initFont();
    }

    public IconfontView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFont();
    }

    public IconfontView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFont();
    }

    private void initFont() {
        final Typeface iconfont = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "iconfont/iconfont.ttf");
        setTypeface(iconfont );
    }
}
