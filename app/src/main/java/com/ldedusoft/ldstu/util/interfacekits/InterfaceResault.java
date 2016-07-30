package com.ldedusoft.ldstu.util.interfacekits;

import android.text.TextUtils;

import com.ldedusoft.ldstu.model.ExaQuery;
import com.ldedusoft.ldstu.model.ExeModel;
import com.ldedusoft.ldstu.model.RaceQuery;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wangjianwei on 2016/6/30.
 */
public class InterfaceResault {

    public static String PU_UserInfoResult = "aaa";
    public static String userInfoResult = "bbb";
    public static String exeResult = "bbb";
    public static String exaQueryResult = "bbb";
    public static String raceQueryResult = "bbb";

    /**
     * 用户信息
     * @return
     */
    public static JSONObject getUserInfo(){
        JSONObject json = new JSONObject();
        try {
            String userInfoStr = "{\"user\":\"wangjianwei\",\"name\":\"wangjianwei\",\"bianhao\":\"286\",\"xuexiao\":\"奇峰汽车学院\",\"banji\":\"15级1班\",\"xingbie\":\"男\",\"nianling\":\"\"}";
             json = new JSONObject(userInfoStr);
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    /**
     * 获取试题
     * @param id 页码
     *  @param category 分类:单选，多选，是非，汽车知识，礼仪知识。。。。
     * @return
     */
    public static ExeModel getExercises(int id,String category){
        ExeModel  exeModel = new ExeModel();
        String exeStr1 = "{\"id\":\"1\",\"type\":\"单选题\",\"category\":\"汽车知识\",\"content\":\"机动车驾驶人造成事故后逃逸构成犯罪的，吊销驾驶证且多长时间不得重新取得驾驶证？\"," +
                "\"optionA\":\"六个月\",\"optionB\":\"一年\",\"optionC\":\"两年\",\"optionD\":\"终身不得获取\",\"answer\":\"4\"}";
        String exeStr2 = "{\"id\":\"2\",\"type\":\"多选题\",\"category\":\"汽车知识\",\"content\":\"世界上第一辆四轮汽车命名为____。\"," +
                "\"optionA\":\"戴姆勒1号\",\"optionB\":\"奔驰1号\",\"optionC\":\"超音速汽车\",\"optionD\":\"威廉一号\",\"answer\":\"1,3\"}";
        String exeStr3 = "{\"id\":\"3\",\"type\":\"单选题\",\"category\":\"汽车知识\",\"content\":\"美国福特汽车公司在1915年生产出一种新型的____\"," +
                "\"optionA\":\"V8型汽车\",\"optionB\":\"T型汽车\",\"optionC\":\"MINI型汽车\",\"optionD\":\"S型汽车\",\"answer\":\"2\"}";
        String exeStr4 = "{\"id\":\"4\",\"type\":\"是非题\",\"category\":\"汽车知识\",\"content\":\"大众是不是德国汽车\"," +
                "\"optionA\":\"是\",\"optionB\":\"否\",\"optionC\":\"\",\"optionD\":\"\",\"answer\":\"1\"}";

        String jsonStr = "";
        if(id==1){
            jsonStr = exeStr1;
        }else if (id==2){
            jsonStr = exeStr2;
        }else if (id==3){
            jsonStr = exeStr3;
        }else if (id==4){
            jsonStr = exeStr4;
        }else{
            jsonStr = "";
            return null;
        }
        try {
            JSONObject json = new JSONObject();
            json = new JSONObject(jsonStr);
            exeModel.id =Integer.parseInt(json.getString("id"));
            exeModel.type =json.getString("type");
            exeModel.category =json.getString("category");
            exeModel.content =json.getString("content");
            exeModel.optionA =json.getString("optionA");
            exeModel.optionB =json.getString("optionB");
            exeModel.optionC =json.getString("optionC");
            exeModel.optionD =json.getString("optionD");
            String answer = json.getString("answer");
            answer =answer.replace("1","A").replace("2","B").replace("3","C").replace("4","D").replaceAll(",","");
            if(exeModel.type.equals("是非题")){
                answer = answer.replace("A","是").replace("B","否");
            }
            exeModel.answer =answer;
        }catch (Exception e){
            e.printStackTrace();
        }
        return exeModel;
    }

    /**
     * 知识分类
     */
    public static String[] getExeCategory(){
        String str = "汽车文化,汽车保险,汽车销售,汽车信贷,汽车后市场,汽车配件,汽车营销考核,汽车商务,汽修管理";
        String[] cateArray = str.split(",");
        return  cateArray;
    }

    public static ArrayList<RaceQuery> getRaceQuery(ArrayList listData,String param){
        //从服务器获取查询值参数为param
        listData.clear();
        String dataStr = getDataFromService("raceQuery",param);
        if(TextUtils.isEmpty(dataStr)){
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(dataStr);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                RaceQuery eq = new RaceQuery();
                eq.name = obj.getString("name");
                eq.time = obj.getString("time");
                eq.type = obj.getString("type");
                eq.score = obj.getString("score");
                eq.rounds = obj.getString("rounds");
                listData.add(eq);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    public static ArrayList<ExaQuery> getExaQuery(ArrayList listData,String param){
        //从服务器获取查询值参数为param
        listData.clear();
        String dataStr = getDataFromService("exaQuery",param);
        if(TextUtils.isEmpty(dataStr)){
            return null;
        }
        try {
            JSONArray jsonArray = new JSONArray(dataStr);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject obj = jsonArray.getJSONObject(i);
                ExaQuery eq = new ExaQuery();
                eq.name = obj.getString("name");
                eq.time = obj.getString("time");
                if(obj.has("type")) {
                    eq.type = obj.getString("type");
                }else {
                    eq.type = "";
                }
                eq.score = obj.getString("score");
                listData.add(eq);
            }
        }catch (Exception e){e.printStackTrace();}
        return listData;
    }

    private static String getDataFromService(String type,String param){
        String result = "";
        if("exaQuery".equals(type)){
            result ="[{\"name\":\"汽车知识\",\"time\":\"2016-1-2\",\"score\":\"89\",\"type\":\"汽车\"},{\"name\":\"礼仪知识\",\"time\":\"2016-10-2\",\"score\":\"90\",\"type\":\"礼仪\"},\n" +
                    "{\"name\":\"销售知识\",\"time\":\"2016-6-12\",\"score\":\"79\",\"type\":\"销售\"},{\"name\":\"维修知识\",\"time\":\"2016-11-2\",\"score\":\"95\",\"type\":\"维修\"}]";
        }else if("raceQuery".equals(type)){
            result ="[{\"name\":\"汽车知识\",\"time\":\"2016-1-2\",\"score\":\"89\",\"type\":\"汽车\",\"rounds\":\"2\"},{\"name\":\"礼仪知识\",\"time\":\"2016-10-2\",\"score\":\"90\",\"type\":\"礼仪\",\"rounds\":\"3\"},\n" +
                    "{\"name\":\"销售知识\",\"time\":\"2016-6-12\",\"score\":\"79\",\"type\":\"销售\",\"rounds\":\"4\"},{\"name\":\"维修知识\",\"time\":\"2016-11-2\",\"score\":\"95\",\"type\":\"维修\",\"rounds\":\"5\"}]";
        }
        return result;
    }
}
