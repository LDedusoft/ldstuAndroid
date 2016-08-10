package com.ldedusoft.ldstu.util.interfacekits;

import android.text.TextUtils;

/**
 * Created by wangjianwei on 2016/6/27.
 */
public class InterfaceParam {

    private static InterfaceParam param;

    /**
     * 服务器地址
     */
    public static final String SERVER_PATH = "http://ct08.ld-edusoft.com/webservice.asmx";

    public static final String SYS_USER = "ldedusoft";

    public static final String SYS_PASSWORD = "_longding2016";


    /**登陆*/
    private String  Login;


    private InterfaceParam(){
    }

    public static InterfaceParam getInstance() {
        if(param==null){
            param = new InterfaceParam();
        }
        return param;
    }

    /**登陆*/
    public String getLogin(String userName,String password) {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <Login xmlns=\"GuoSai\">\n" +
                "      <UName>@userName</UName>\n" +
                "      <Pwd>@password</Pwd>\n" +
                "    </Login>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        xml = xml.replace("@userName",userName);
        xml = xml.replace("@password",password);
        return xml;
    }

    /**知识类型*/
    public String getExeCategory() {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <CourseList xmlns=\"GuoSai\" />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        return xml;
    }

    /**保存用户信息*/
    public String saveUserInfo(String userInfo) {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <ModifyUserInfo xmlns=\"GuoSai\">\n" +
                "      <pInfo>@userInfo</pInfo>\n" +
                "    </ModifyUserInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        xml = xml.replace("@userInfo",userInfo);
        return xml;
    }

    /**获取用户信息*/
    public String getUserInfo(String userName) {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <UserInfo xmlns=\"GuoSai\">\n" +
                "      <UName>@userName</UName>\n" +
                "    </UserInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        xml = xml.replace("@userName",userName);
        return xml;
    }

    /**获取试题*/
    public String getExercises(String type,String course) {
        if(TextUtils.isEmpty(type)){
            type = "";
        }
        if(TextUtils.isEmpty(course)){
            course = "";
        }
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <GetSubject xmlns=\"GuoSai\">\n" +
                "      <type>@type</type>\n" +
                "      <course>@course</course>\n" +
                "    </GetSubject>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        xml = xml.replace("@type",type);
        xml = xml.replace("@course",course);
        return xml;
    }

    /**获取考试查询*/
    public String getExaQuery(String raceName,String userName) {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <ScoreInfo xmlns=\"GuoSai\">\n" +
                "      <TestId>@raceName</TestId>\n" +
                "      <UName>@userName</UName>\n" +
                "    </ScoreInfo>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        xml = xml.replace("@raceName",raceName);
        xml = xml.replace("@userName",userName);
        return xml;
    }

    /**获取比赛列表*/
    public String getRaceQuery(String time,String userName) {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <TestList xmlns=\"GuoSai\">\n" +
                "      <time>@time</time>\n" +
                "       <UName>@userName</UName>"+
                "    </TestList>\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        xml = xml.replace("@time",time);
        xml = xml.replace("@userName",userName);
        return xml;
    }

    /**获取比赛列表*/
    public String getSchoolQuery() {
        String xml =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "  <soap:Header>\n" +
                "    <MySoapHeader xmlns=\"GuoSai\">\n" +
                "      <UserName>@sysUser</UserName>\n" +
                "      <PassWord>@sysPassword</PassWord>\n" +
                "    </MySoapHeader>\n" +
                "  </soap:Header>\n" +
                "  <soap:Body>\n" +
                "    <SchoolList xmlns=\"GuoSai\" />\n" +
                "  </soap:Body>\n" +
                "</soap:Envelope>";
        xml = xml.replace("@sysUser",SYS_USER);
        xml = xml.replace("@sysPassword",SYS_PASSWORD);
        return xml;
    }

}
