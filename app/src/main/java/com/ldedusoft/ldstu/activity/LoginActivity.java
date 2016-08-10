package com.ldedusoft.ldstu.activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.ldedusoft.ldstu.R;
import com.ldedusoft.ldstu.adapters.LoginSpinnerAdapter;
import com.ldedusoft.ldstu.model.SysProperty;
import com.ldedusoft.ldstu.model.UserProperty;
import com.ldedusoft.ldstu.util.HttpCallbackListener;
import com.ldedusoft.ldstu.util.HttpUtil;
import com.ldedusoft.ldstu.util.ParseXML;
import com.ldedusoft.ldstu.util.interfacekits.InterfaceParam;
import com.ldedusoft.ldstu.viewpagerfragment.FragmentMainActivity;

/**
 * Created by wangjianwei on 2016/6/22.
 */
public class LoginActivity extends com.ldedusoft.ldstu.activity.BaseActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener {

    private final String CONFIG_SAVEPASSWORD = "config_savePassword";
    private final String CONFIG_USERNAME = "config_username";
    private final String CONFIG_PASSWORD = "config_password";
    private EditText username;
    private EditText password;
    private Button loginBtn;
    private String serverPath ;
    private String paramXml;
    private Spinner loginTypeSpinner;
    private ArrayAdapter<String> mAdapter ;
    private String loginResult; //登录返回值
    private ProgressDialog progressDialog;
    private SharedPreferences pref; //保存文件
    private SharedPreferences.Editor editor;
    private CheckBox savePassword;
    private Button offLine;
    private Button register;


    /**
     * 登录页面布局
     */
    private LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ldbm_login);
        initLoginPage();
    }

    /**
     * 初始化登录页面
     */
    private void initLoginPage(){
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        mainLayout = (LinearLayout)findViewById(R.id.root_layout);
        username = (EditText)findViewById(R.id.edittext_username);
        password = (EditText)findViewById(R.id.edittext_password);
        loginTypeSpinner = (Spinner)findViewById(R.id.spinner_loginType);
        offLine = (Button)findViewById(R.id.login_offline);
        offLine.setOnClickListener(this);
        register = (Button)findViewById(R.id.login_register);
        register.setOnClickListener(this);
        loginBtn = (Button)findViewById(R.id.btn_login);
        loginBtn.setOnClickListener(this);
        username.setSelection(username.getText().length());
        savePassword = (CheckBox)findViewById(R.id.login_savePassword);
        username.setText(pref.getString(CONFIG_USERNAME, ""));
        String savePd = pref.getString(CONFIG_SAVEPASSWORD,"");
        if("true".equals(savePd)){
            savePassword.setChecked(true);
            password.setText(pref.getString(CONFIG_PASSWORD,""));
        }else{
            savePassword.setChecked(false);
        }

        //注册下拉列表监听器
        loginTypeSpinner.setOnItemSelectedListener(this);
        //通过加载xml文件配置的数据源
        Resources res=getResources();
        String[] loginTyps=res.getStringArray(R.array.loginType_array);
        mAdapter = new LoginSpinnerAdapter(this, loginTyps);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loginTypeSpinner.setAdapter(mAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在登录");
        progressDialog.setCanceledOnTouchOutside(false);


//        if( this.getResources().getConfiguration().orientation
//                != Configuration.ORIENTATION_LANDSCAPE){
//            //控制键盘
//            controlKeyboardLayout(mainLayout, findViewById(R.id.btn_login));
//        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                // 强制关闭系统键盘
                InputMethodManager imm = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                if(TextUtils.isEmpty(username.getText())){
                    Toast.makeText(LoginActivity.this.getApplicationContext(),
                            "请输入用户名!", Toast.LENGTH_SHORT).show();
                    username.findFocus();
                }else if(TextUtils.isEmpty(password.getText())){
                    Toast.makeText(LoginActivity.this.getApplicationContext(),
                            "请输入密码!", Toast.LENGTH_SHORT).show();
                    password.findFocus();
                }else {
                    ldbmLogin();
//                    loginSuccess();
                }
                break;
            case R.id.login_register:
                Toast.makeText(this,"暂未开放",Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_offline:
                if(TextUtils.isEmpty(username.getText())){
                    Toast.makeText(LoginActivity.this.getApplicationContext(),
                            "需要输入用户名!", Toast.LENGTH_SHORT).show();
                    username.findFocus();
                }
                SysProperty.offLineModel = true;
                loginSuccess();
                break;

        }
    }

    /**
     * 登录
     */
    private void ldbmLogin(){
        showProgressDialog();
        serverPath =InterfaceParam.SERVER_PATH;
        paramXml = InterfaceParam.getInstance().getLogin(username.getText().toString(), password.getText().toString());
        HttpUtil.sendHttpRequest(serverPath, paramXml, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loginResult = ParseXML.getItemValueWidthName(response, "LoginResult");
                        if ("false".equals(loginResult)||TextUtils.isEmpty(loginResult)) {
                            //登录失败方法
                            loginError();
                        } else {
                            //登录成功方法
                            loginSuccess();
                        }

                    }
                });
            }

            @Override
            public void onWarning(String warning) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(LoginActivity.this, "登录失败,网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(LoginActivity.this, "登录失败,网络错误", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }

    private void loginSuccess(){
        closeProgressDialog();
        editor = pref.edit();
        //保存用户名
        editor.putString(CONFIG_USERNAME,username.getText().toString());
        if(!SysProperty.offLineModel) {
            //// TODO: 2016/8/1 保存密码
            String savePd;
            if (savePassword.isChecked()) {
                savePd = "true";
            } else {
                savePd = "false";
            }
            editor.putString(CONFIG_SAVEPASSWORD, savePd);
            editor.putString(CONFIG_PASSWORD, password.getText().toString());
            editor.commit();
        }
        String selectedType = loginTypeSpinner.getSelectedItem().toString();
        SysProperty.getInstance().setMode(selectedType);
        UserProperty.getInstance().setUserName(username.getText().toString());
        UserProperty.getInstance().setPassWord(password.getText().toString());
        UserProperty.getInstance().setUserType(1);//设置用户类型。默认1，暂时不需要。
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        if(SysProperty.offLineModel){
            Log.d("LoginActivity", username.getText().toString() + "离线登录");
        }else{
            Log.d("LoginActivity", username.getText().toString() + "登录");
        }
        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
        startActivity(intent);

        System.gc();
        System.runFinalization();
        finish();
    }
    private void loginError(){
        closeProgressDialog();
        Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
    }



    /**
     * @param root 最外层布局，需要调整的布局
     * @param scrollToView 被键盘遮挡的scrollToView，滚动root,使scrollToView在root可视区域的底部
    */
    private void controlKeyboardLayout(final View root, final View scrollToView) {
        root.getViewTreeObserver().addOnGlobalLayoutListener( new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                //获取root在窗体的可视区域
                root.getWindowVisibleDisplayFrame(rect);
                //获取root在窗体的不可视区域高度(被其他View遮挡的区域高度)
                int rootInvisibleHeight = root.getRootView().getHeight() - rect.bottom;
                //若不可视区域高度大于100，则键盘显示
                if (rootInvisibleHeight > 100) {
                    int[] location = new int[2];
                    //获取scrollToView在窗体的坐标
                    scrollToView.getLocationInWindow(location);
                    //计算root滚动高度，使scrollToView在可见区域的底部
                    int srollHeight = (location[1] + scrollToView.getHeight()) - rect.bottom;
                    root.scrollTo(0, srollHeight);
                } else {
                    //键盘隐藏
                    root.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String itemString = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * 进度框
     */
    private void showProgressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在登录");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }
}
