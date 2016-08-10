package com.ldedusoft.ldstu.model;

import java.io.Serializable;

/**
 * Created by wangjianwei on 2016/7/29.
 * {"Name":"名称","TestId":"编号","Qzfen:"总分","StartTime":"开始时间","LimitTime":"限时"}
 */
public class RaceQuery implements Serializable {
    public String Name;
    public String TestId;
    public String Qzfen;
    public String StartTime;
    public String LimitTime;
}
