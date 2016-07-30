package com.ldedusoft.ldstu.interfaces;

import org.json.JSONObject;

/**
 * Created by wangjianwei on 2016/7/4.
 */
public interface CommonQueryBarListener {
    public abstract void OnSelectClick(JSONObject paramObj);
    public abstract void OnSalesmanSelect();
    public abstract void OnClientSelect();
    public abstract void OnRestockTypeSelect();
}
