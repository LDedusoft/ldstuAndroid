package com.ldedusoft.ldstu.activity.answer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.activity.BaseActivity;
import com.ldedusoft.ldstu.component.customComp.TopBar;
import com.ldedusoft.ldstu.component.iconfont.IconfontView;
import com.ldedusoft.ldstu.interfaces.FormToolBarListener;
import com.ldedusoft.ldstu.model.ExeModel;
import com.ldedusoft.ldstu.model.UserProperty;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceResault;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by wangjianwei on 2016/7/27.
 */
public class AnswerActivity extends BaseActivity{
    private String model = "";
    private final String EXE_TYPE_NORMAL = "normal";
    private final String EXE_TYPE_WRONG = "wrong";
    private final String EXE_TYPE_FAVORITE = "favorite";
    private final String EXE_TYPE_DANXUAN = "danxuan";
    private final String EXE_TYPE_DUOXUAN = "duoxuan";
    private final String EXE_TYPE_SHIFEI = "shifei";
    private SharedPreferences pref; //保存文件
    private SharedPreferences.Editor editor;
    private ExeModel exeModel,exeModelTemp;
    private TopBar topBar;
    private TextView answer_content;
    private LinearLayout radioLayout,checkBoxLayout,bottomBar,bottomBarTitle,bottombarValue,delLayout,noExeLayout,titlLayout,favLayout;
    private RadioButton radioA,radioB,radioC,radioD;
    private RadioGroup radioGroup;
    private CheckBox checkA,checkB,checkC,checkD;
    private TextView radioSubmit,checkSubmit,typeIcon,lastExe,nextExe,dangQian,zhengQue,delText,favText;
    private int pageNumber = 1;
    private String selected;
    private String param;
    private int exeCount = 0;//总题数
    private int thisId = 0; //当前题位置
    private ArrayList<ExeModel> exeList;
    private String title;
    private String category;
    final int RIGHT = 0;
    final int LEFT = 1;
    private IconfontView delIcon,favIcon;
    private GestureDetector gestureDetector;
    boolean noExe = false; //当前是否有试题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_answer);
        gestureDetector = new GestureDetector(AnswerActivity.this,onGestureListener);
        Intent intent= getIntent();
        param = intent.getStringExtra("param"); //模式
        title = intent.getStringExtra("title"); //标题
        category = intent.getStringExtra("category"); //分类
        if (TextUtils.isEmpty(category)){
            category = EXE_TYPE_NORMAL;
        }
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        initView();
        if(EXE_TYPE_NORMAL.equals(param)) { //普通模式
            model = EXE_TYPE_NORMAL;
            getNormalExeData(category);
        }else if(EXE_TYPE_WRONG.equals(param)){//错题模式
            model = EXE_TYPE_WRONG;
            delLayout.setVisibility(View.VISIBLE);
            getModelExeData(EXE_TYPE_WRONG);
        }else if(EXE_TYPE_FAVORITE.equals(param)){//收藏模式
            model = EXE_TYPE_FAVORITE;
            delLayout.setVisibility(View.VISIBLE);
            getModelExeData(EXE_TYPE_FAVORITE);
        }

    }
    private void initView(){
        topBar = (TopBar)findViewById(R.id.answer_top_bar);
        topBar.setTitle(title);
        topBar.showBackBtn();
        topBar.setFormToolBarListener(new FormToolBarListener() {
            @Override
            public void OnSaveClick() {
            }

            @Override
            public void OnBackClick() {
                finish();
            }
        });
        answer_content = (TextView)findViewById(R.id.answer_content);
        radioLayout = (LinearLayout)findViewById(R.id.answer_radio_layout);
        checkBoxLayout = (LinearLayout)findViewById(R.id.answer_checkbox_layout);
        radioGroup = (RadioGroup)findViewById(R.id.answer_radio_group);
        radioA= (RadioButton)findViewById(R.id.answer_radio_A);
        radioB= (RadioButton)findViewById(R.id.answer_radio_B);
        radioC= (RadioButton)findViewById(R.id.answer_radio_C);
        radioD= (RadioButton)findViewById(R.id.answer_radio_D);
        checkA= (CheckBox)findViewById(R.id.answer_checkbox_A);
        checkB= (CheckBox)findViewById(R.id.answer_checkbox_B);
        checkC= (CheckBox)findViewById(R.id.answer_checkbox_C);
        checkD= (CheckBox)findViewById(R.id.answer_checkbox_D);
        radioSubmit = (TextView)findViewById(R.id.answer_radio_submit);
        checkSubmit = (TextView)findViewById(R.id.answer_checkbox_submit);
        typeIcon = (TextView)findViewById(R.id.answer_title_typeIcon);
        lastExe = (TextView)findViewById(R.id.answer_last);
        nextExe = (TextView)findViewById(R.id.answer_next);
        dangQian = (TextView)findViewById(R.id.answer_dangQianXuanZe);
        zhengQue = (TextView)findViewById(R.id.answer_ZhengQueDaAn);
        bottomBar = (LinearLayout)findViewById(R.id.answer_bottom_bar);
        bottomBarTitle = (LinearLayout)findViewById(R.id.answer_bottom_title);
        bottombarValue = (LinearLayout)findViewById(R.id.answer_bottom_value);
        delLayout =(LinearLayout)findViewById(R.id.answer_del_layout);
        delIcon = (IconfontView)findViewById(R.id.answer_del_icon);
        delText = (TextView)findViewById(R.id.answer_del_txt);
        favLayout =(LinearLayout)findViewById(R.id.answer_fav_layout);
        favIcon = (IconfontView)findViewById(R.id.answer_fav_icon);
        favText = (TextView)findViewById(R.id.answer_fav_txt);
        noExeLayout = (LinearLayout)findViewById(R.id.answer_no_exe);
        titlLayout = (LinearLayout)findViewById(R.id.answer_title_layout);

        //收藏
        favIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExe(EXE_TYPE_FAVORITE);
            }
        });
        favText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveExe(EXE_TYPE_FAVORITE);
            }
        });
        //移除错题
        delIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delExe(model);
            }
        });
        delText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delExe(model);
            }
        });
        //上一页
        lastExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLastExe();
            }
        });
        //下一页
        nextExe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextExe();
            }
        });
        radioA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        radioB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        radioC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        radioD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        checkA.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        checkB.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        checkC.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        checkD.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        radioSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
        checkSubmit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
//        单选提交按钮
        radioSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "";
                if (exeModel.type.equals("单选题")) {
                    if (radioA.isChecked()) {
                        selected = "A";
                    } else if (radioB.isChecked()) {
                        selected = "B";
                    } else if (radioC.isChecked()) {
                        selected = "C";
                    } else if (radioD.isChecked()) {
                        selected = "D";
                    }
                }else if(exeModel.type.equals("是非题")){
                    if (radioA.isChecked()) {
                        selected = "是";
                    } else if (radioB.isChecked()) {
                        selected = "否";
                    }
                }
                if(TextUtils.isEmpty(selected)){
                    Toast.makeText(AnswerActivity.this,"请选择答案",Toast.LENGTH_SHORT).show();
                    return;
                }
                dangQian.setText(selected);
                bottomBarTitle.setVisibility(View.VISIBLE);
                bottombarValue.setVisibility(View.VISIBLE);
                if(!selected.equals(exeModel.answer)){
//                    toast("错误(已加入我的错题)");
                    Toast.makeText(AnswerActivity.this,"错误" ,Toast.LENGTH_SHORT).show();
                    dangQian.setTextColor(getResources().getColor(R.color.red));
                    //// TODO: 2016/7/27  保存到错题集合
                    saveExe(EXE_TYPE_WRONG);
                }else{
                    Toast.makeText(AnswerActivity.this,"正确" ,Toast.LENGTH_SHORT).show();
                    dangQian.setTextColor(getResources().getColor(R.color.green));
                }
            }
        });

        checkSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = "";
                if (checkA.isChecked()) {
                    selected += "A";
                }
                if (checkB.isChecked()) {
                    selected += "B";
                }
                if (checkC.isChecked()) {
                    selected += "C";
                }
                if (checkD.isChecked()) {
                    selected += "D";
                }
                if (TextUtils.isEmpty(selected)) {
                    Toast.makeText(AnswerActivity.this, "请选择答案", Toast.LENGTH_SHORT).show();
                    return;
                }
                dangQian.setText(selected);
                bottomBarTitle.setVisibility(View.VISIBLE);
                bottombarValue.setVisibility(View.VISIBLE);
                if (!selected.equals(exeModel.answer)) {
                    Toast.makeText(AnswerActivity.this, "错误", Toast.LENGTH_SHORT).show();
                    dangQian.setTextColor(getResources().getColor(R.color.red));
                    //// TODO: 2016/7/27  保存到错题集合
                    saveExe(EXE_TYPE_WRONG);
                } else {
                    Toast.makeText(AnswerActivity.this, "正确", Toast.LENGTH_SHORT).show();
                    dangQian.setTextColor(getResources().getColor(R.color.green));
                }
            }
        });
    }


    private void delExe(String delType){
//        从列表中删除当前题，id序列中删除。
//        跳转到下一题，如果是最后一题则跳转到上一题
        boolean isonly = false;//唯一的一个
        boolean isFinal= false;//最后一题
        if(exeList.size()==1){
            isonly = true;
        }

        if(thisId==exeList.size()-1){
            isFinal = true;
        }
        //删除
        doDelExe(delType);
        exeList.remove(thisId);//删除当前缓存的集合
        if(!isonly) {
            //跳转
            if (isFinal) {
                doLastExe();
            } else {
                doNextExe();
            }
        }else{
            noExe = true;
            showNoExe();
        }
    }

    /**
     * 显示空白页
     * */
    private void showNoExe(){
        titlLayout.setVisibility(View.GONE);
        radioLayout.setVisibility(View.GONE);
        checkBoxLayout.setVisibility(View.GONE);
        delLayout.setVisibility(View.GONE);
        favLayout.setVisibility(View.GONE);
        noExeLayout.setVisibility(View.VISIBLE);
        bottomBar.setVisibility(View.INVISIBLE);
    }

    /**
     * 移除错题
     */
    private void doDelExe(String delType){
        String exeId = String.valueOf(exeModel.id);
        String newIdList = "";
        JSONArray jsonArray;
        String exeStr ="";
        String exeStrIdList ="";
        if(delType.equals(EXE_TYPE_WRONG)) {
            exeStr = pref.getString(UserProperty.getInstance().getUserName() + "_wrong", "");//获取用户错题集
            exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_wrong_idList", "");//获取用户错题集id集合
        }else if(delType.equals(EXE_TYPE_FAVORITE)){
            exeStr = pref.getString(UserProperty.getInstance().getUserName() + "_favorite", "");//获取用户收藏
            exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_favorite_idList", "");//获取用户收藏id集合
        }
            try {
            if (!TextUtils.isEmpty(exeStr)) {
                jsonArray = new JSONArray(exeStr);
            } else {
                jsonArray = new JSONArray();
                exeStrIdList = "";
            }
            String[] idArray = exeStrIdList.split(","); //已保存错题id集合
            for(int i=0;i<idArray.length;i++){
                String item = idArray[i];
                if(exeId.equals(item)){  //去除当前id
                    idArray[i]="";break;}
            }
            for(int i=0;i<idArray.length;i++){ //重新组合id列表
                String item = idArray[i];
                if(!TextUtils.isEmpty(item)){
                    newIdList+=","+item;}
            }

            int tempIndex = 0;
            for (int i=jsonArray.length()-1;i>=0;i--){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                if(jsonObject.getString("id").equals(exeId)) { //移除当前题
//                    jsonArray.remove(i); 不支持4.4以下版本,改为new一个jsonarray重新添加一遍
                    tempIndex = i;
                }
            }
            JSONArray newJSarray = new JSONArray();
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                if(i!=tempIndex){
                    newJSarray.put(jsonObject);
                }
            }

            String newExeStr = newJSarray.toString();
            editor = pref.edit();
                if(delType.equals(EXE_TYPE_WRONG)) {
                    editor.putString(UserProperty.getInstance().getUserName() + "_wrong_idList", newIdList);
                    editor.putString(UserProperty.getInstance().getUserName() + "_wrong", newExeStr);
                }else if(delType.equals(EXE_TYPE_FAVORITE)){
                    editor.putString(UserProperty.getInstance().getUserName() + "_favorite_idList", newIdList);
                    editor.putString(UserProperty.getInstance().getUserName() + "_favorite", newExeStr);
                }
            editor.commit();
        }catch (Exception e){e.printStackTrace();}
    }

    /**
     * 跳转到上一题
     */
    private void doLastExe(){
        if(noExe){
            return;
        }
        if(EXE_TYPE_NORMAL.equals(param)) { //随机练习
            exeModelTemp = exeModel;
            exeModel = InterfaceResault.getExercises(pageNumber-1,category);
            if(exeModel==null){
                exeModel = exeModelTemp;//恢复试题数据
                Toast.makeText(AnswerActivity.this,"当前是第一题",Toast.LENGTH_SHORT).show();
                return;
            }else{
                pageNumber--;//当前页码减1
            }
        }
        if(EXE_TYPE_WRONG.equals(param)||EXE_TYPE_FAVORITE.equals(param)){//错题练习,收藏练习
            if(thisId==0){
                Toast.makeText(AnswerActivity.this,"当前是第一题",Toast.LENGTH_SHORT).show();
                return;
            }
            thisId--;
            exeModel = exeList.get(thisId);
        }
        initData();
    }
    /**
     * 跳转到下一题
     */
    private void doNextExe(){
        if(noExe){
            return;
        }
        exeModelTemp = exeModel;
        if(EXE_TYPE_NORMAL.equals(param)) {//随机练习
            exeModel = InterfaceResault.getExercises(pageNumber+1,category);
            if(exeModel==null){
                exeModel = exeModelTemp;
                Toast.makeText(AnswerActivity.this,"当前是最后一题",Toast.LENGTH_SHORT).show();
                return;
            }else{
                pageNumber++;//当前页码加1
            }
        }
        if(EXE_TYPE_WRONG.equals(param)||EXE_TYPE_FAVORITE.equals(param)){//错题练习,收藏练习
            if(thisId==exeList.size()-1){
                Toast.makeText(AnswerActivity.this,"当前是最后一题",Toast.LENGTH_SHORT).show();
                return;
            }
            thisId++;
            exeModel = exeList.get(thisId);
        }
        initData();
    }

    /*保存到本地*/
    private void saveExe(String saveType){
        if(saveType.equals(EXE_TYPE_FAVORITE)) {
            favIcon.setTextColor(getResources().getColor(R.color.fav_icon_select));
            favText.setText("已收藏");
        }
        JSONArray jsonArray;
        String newExeStr;
        String exeStr ="";
        String exeStrIdList = "";
        if(saveType.equals(EXE_TYPE_WRONG)) {
             exeStr = pref.getString(UserProperty.getInstance().getUserName() + "_wrong", "");//获取用户错题集
             exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_wrong_idList", "");//获取用户错题集id集合
        }else if(saveType.equals(EXE_TYPE_FAVORITE)){
            exeStr = pref.getString(UserProperty.getInstance().getUserName() + "_favorite", "");//获取用户收藏
            exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_favorite_idList", "");//获取用户收藏id集合
        }
        try {
           if (!TextUtils.isEmpty(exeStr)) {
               jsonArray = new JSONArray(exeStr);
           } else {
               jsonArray = new JSONArray();
               exeStrIdList = "";
           }
           String[] idArray = exeStrIdList.split(","); //已保存题id集合
           boolean hasExe = false;
           for(String str:idArray){
               if(String.valueOf(exeModel.id).equals(str)){ //判断是否已包含当前题
                   hasExe = true;
                   break;
               }
           }
           if(hasExe){
               //保存错题，如果存在直接退出。 保存收藏，如果存在取消收藏

               if(saveType.equals(EXE_TYPE_FAVORITE)){
                   Log.d(saveType+"收藏习题：","已存在"+exeModel.id+" 取消收藏");
                   favIcon.setTextColor(getResources().getColor(R.color.fav_icon_unselect));
                   favText.setText("收藏本题");
                   doDelExe(EXE_TYPE_FAVORITE);//直接执行doDelExe（） 而不是delExe（）

               }else{
                   Log.d(saveType+"保存错题：","已存在"+exeModel.id+" 直接退出");
               }
               return;

           }

           JSONObject jsonObject = new JSONObject();
           jsonObject.put("id",exeModel.id);
           jsonObject.put("type",exeModel.type);
           jsonObject.put("category",exeModel.category);
           jsonObject.put("answer", exeModel.answer);
           jsonObject.put("content", exeModel.content);
//           jsonObject.put("difficulty",exeModel.difficulty);
//           jsonObject.put("mark",exeModel.mark);
           jsonObject.put("optionA",exeModel.optionA);
           jsonObject.put("optionB", exeModel.optionB);
           jsonObject.put("optionC",exeModel.optionC);
           jsonObject.put("optionD",exeModel.optionD);
           jsonArray.put(jsonObject);
           newExeStr = jsonArray.toString();
           exeStrIdList+=","+exeModel.id;
           editor = pref.edit();
            if(saveType.equals(EXE_TYPE_WRONG)) {
                editor.putString(UserProperty.getInstance().getUserName() + "_wrong", newExeStr);
                editor.putString(UserProperty.getInstance().getUserName() + "_wrong_idList", exeStrIdList);
            }else if (saveType.equals(EXE_TYPE_FAVORITE)){
                editor.putString(UserProperty.getInstance().getUserName() + "_favorite", newExeStr);
                editor.putString(UserProperty.getInstance().getUserName() + "_favorite_idList", exeStrIdList);
            }
           Log.d(saveType + "保存信息：", newExeStr);
           Log.d(saveType + "保存id列表：", exeStrIdList);
           editor.commit();

       }catch (Exception e){e.printStackTrace();}
    }

    /*获取随机练习题*/
    private void getNormalExeData(final String category){
        String serverPath = InterfaceParam.SERVER_PATH;
        String paramXml = InterfaceParam.getInstance().getExercises(UserProperty.getInstance().getUserName());
        HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String result = ParseXML.getItemValueWidthName(response, InterfaceResault.exeResult);
                        if (TextUtils.isEmpty(result)) {
                            //失败方法
//                            Toast.makeText(AnswerActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
                            //测试模式
                            Toast.makeText(AnswerActivity.this, "测试模式模拟数据", Toast.LENGTH_SHORT).show();
                            exeModel = InterfaceResault.getExercises(pageNumber, category);
                            favLayout.setVisibility(View.VISIBLE);
                            initData();
                        } else {
                            //成功方法
                            exeModel = InterfaceResault.getExercises(pageNumber, category);
                            favLayout.setVisibility(View.VISIBLE);
                            initData();
                        }

                    }
                });
            }
            @Override
            public void onWarning(String warning) {}
            @Override
            public void onError(Exception e) {}
        });

    }

    /**
     * 获取本地数据
     */
    private void getModelExeData(String modelType){
        JSONArray jsonArray = new JSONArray();
        exeList = new ArrayList<ExeModel>();
        try {
            String exeStr = "";
            String exeStrIdList = "";
            if(modelType.equals(EXE_TYPE_WRONG)) {
                exeStr = pref.getString(UserProperty.getInstance().getUserName() + "_wrong", "");//获取用户错题集
                exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_wrong_idList", "");//获取用户错题集id集合
            }else if(modelType.equals(EXE_TYPE_FAVORITE)){
                exeStr = pref.getString(UserProperty.getInstance().getUserName() + "_favorite", "");//获取用户收藏题集
                exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_favorite_idList", "");//获取用户收藏id集合
            }
            if (!TextUtils.isEmpty(exeStr)) {
                jsonArray = new JSONArray(exeStr);
                for (int i=0;i<jsonArray.length();i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    ExeModel exeModel = new ExeModel();
                    exeModel.id = Integer.parseInt(jsonObject.getString("id"));
                    exeModel.type = jsonObject.getString("type");
                    exeModel.category = jsonObject.getString("category");
                    exeModel.optionA = jsonObject.getString("optionA");
                    exeModel.optionB = jsonObject.getString("optionB");
                    exeModel.optionC = jsonObject.getString("optionC");
                    exeModel.optionD = jsonObject.getString("optionD");
                    exeModel.answer = jsonObject.getString("answer");
                    exeModel.content = jsonObject.getString("content");
//                    exeModel.difficulty = jsonObject.getString("difficulty");
//                    exeModel.mark = jsonObject.getString("mark");
                    exeList.add(exeModel);
                    if (i==0){
                        this.exeModel = exeModel;
                    }
                }
            }
            initData();
        }catch (Exception e){e.printStackTrace();}
    }

    private void initData(){
        if(exeModel==null){
            noExe = true;
            showNoExe();
            return;
        }
        bottomBar.setVisibility(View.VISIBLE);
        answer_content.setText(exeModel.content);
        if("单选题".equals(exeModel.type)){
            radioGroup.clearCheck();
            checkBoxLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.VISIBLE);
            typeIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.danxuanti));
            radioA.setText(exeModel.optionA);
            radioB.setText(exeModel.optionB);
            radioC.setText(exeModel.optionC);
            radioD.setText(exeModel.optionD);
            radioC.setVisibility(View.VISIBLE);
            radioD.setVisibility(View.VISIBLE);
        }else  if("是非题".equals(exeModel.type)){
            radioGroup.clearCheck();
            checkBoxLayout.setVisibility(View.GONE);
            radioLayout.setVisibility(View.VISIBLE);
            typeIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.shifeiti));
            radioA.setText(exeModel.optionA);
            radioB.setText(exeModel.optionB);
            radioC.setVisibility(View.GONE);
            radioD.setVisibility(View.GONE);

        }else  if("多选题".equals(exeModel.type)){
            checkBoxLayout.setVisibility(View.VISIBLE);
            radioLayout.setVisibility(View.GONE);
            typeIcon.setBackgroundDrawable(getResources().getDrawable(R.drawable.duoxuanti));
            checkA.setText(exeModel.optionA);
            checkB.setText(exeModel.optionB);
            checkC.setText(exeModel.optionC);
            checkD.setText(exeModel.optionD);
            checkA.setChecked(false);
            checkB.setChecked(false);
            checkC.setChecked(false);
            checkD.setChecked(false);
        }
        bottomBarTitle.setVisibility(View.INVISIBLE);
        bottombarValue.setVisibility(View.INVISIBLE);
        zhengQue.setText(exeModel.answer);
        initFavIconState();
    }

    private void initFavIconState(){
        String exeStrIdList = pref.getString(UserProperty.getInstance().getUserName() + "_favorite_idList", "");//获取用户收藏id集合
        Log.d("初始化收藏图标状态：", exeStrIdList);
        if(TextUtils.isEmpty(exeStrIdList)){
            return;
        }
        boolean isFav = false;
        String[] idArray = exeStrIdList.split(",");
        for(String id:idArray){
            if(id.equals(String.valueOf(exeModel.id))){
                isFav = true;
                break;
            }
        }
        if(isFav) {
            favIcon.setTextColor(getResources().getColor(R.color.fav_icon_select));
            favText.setText("已收藏");
        }else{
            favIcon.setTextColor(getResources().getColor(R.color.fav_icon_unselect));
            favText.setText("收藏本题");
        }
    }

    //滑动手势
    private GestureDetector.OnGestureListener onGestureListener =
            new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                       float velocityY) {
                    float x = e2.getX() - e1.getX();
                    float y = e2.getY() - e1.getY();

                    if (x > 0) {
                        doResult(RIGHT);
                    } else if (x < 0) {
                        doResult(LEFT);
                    }
                    return true;
                }
            };

    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    public void doResult(int action) {
        switch (action) {
            case RIGHT:
                doLastExe();
                break;
            case LEFT:
                doNextExe();
                break;
        }
    }

    protected void toast(String message) {
        View toastRoot = LayoutInflater.from(this).inflate(R.layout.toast_layout, null);
        Toast toast = new Toast(this);
        toast.setView(toastRoot);
        TextView tv = (TextView) toastRoot.findViewById(R.id.toast_notice);
        tv.setText(message);
        toast.show();
    }
}
