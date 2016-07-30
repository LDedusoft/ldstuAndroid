package com.ldedusoft.ldstu.util.interfacekits;

/**
 * Created by wangjianwei on 2016/6/27.
 */
public class InterfaceParam {

    private static InterfaceParam param;

    /**
     * 服务器地址
     */
    public static final String SERVER_PATH = "http://ldbm.ld-edusoft.com/webservers/WebService.asmx";

    public static final String SYS_USER = "admin";

    public static final String SYS_PASSWORD = "zwj6756";


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
    public String getLogin(String userName,String passWord) {
        Login =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <soap:Header>" +
                "    <MySoapHeader xmlns=\"LDBM4S\">" +
                "      <UserName>@sysUser</UserName>" +
                "      <PassWord>@sysPassword</PassWord>" +
                "    </MySoapHeader>" +
                "  </soap:Header>" +
                "  <soap:Body>" +
                "    <Login xmlns=\"LDBM4S\">" +
                "      <UName>@userName</UName>" +
                "      <Pwd>@passWord</Pwd>" +
                "    </Login>" +
                "  </soap:Body>" +
                "</soap:Envelope>";
        Login = Login.replace("@sysUser",SYS_USER);
        Login = Login.replace("@sysPassword",SYS_PASSWORD);
        Login = Login.replace("@userName",userName);
        Login = Login.replace("@passWord",passWord);
        return Login;
    }

    /**获取用户信息*/
    public String getUserInfo(String userName) {
        Login =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <soap:Header>" +
                "    <MySoapHeader xmlns=\"LDBM4S\">" +
                "      <UserName>@sysUser</UserName>" +
                "      <PassWord>@sysPassword</PassWord>" +
                "    </MySoapHeader>" +
                "  </soap:Header>" +
                "  <soap:Body>" +
                "    <Login xmlns=\"LDBM4S\">" +
                "      <UName>@userName</UName>" +
                "      <Pwd>@passWord</Pwd>" +
                "    </Login>" +
                "  </soap:Body>" +
                "</soap:Envelope>";
        Login = Login.replace("@sysUser",SYS_USER);
        Login = Login.replace("@sysPassword",SYS_PASSWORD);
        Login = Login.replace("@userName",userName);
        return Login;
    }

    /**获取试题*/
    public String getExercises(String userName) {
        Login =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <soap:Header>" +
                "    <MySoapHeader xmlns=\"LDBM4S\">" +
                "      <UserName>@sysUser</UserName>" +
                "      <PassWord>@sysPassword</PassWord>" +
                "    </MySoapHeader>" +
                "  </soap:Header>" +
                "  <soap:Body>" +
                "    <Login xmlns=\"LDBM4S\">" +
                "      <UName>@userName</UName>" +
                "      <Pwd>@passWord</Pwd>" +
                "    </Login>" +
                "  </soap:Body>" +
                "</soap:Envelope>";
        Login = Login.replace("@sysUser",SYS_USER);
        Login = Login.replace("@sysPassword",SYS_PASSWORD);
        Login = Login.replace("@userName",userName);
        return Login;
    }

    /**获取考试查询*/
    public String getExaQuery(String userName) {
        Login =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <soap:Header>" +
                "    <MySoapHeader xmlns=\"LDBM4S\">" +
                "      <UserName>@sysUser</UserName>" +
                "      <PassWord>@sysPassword</PassWord>" +
                "    </MySoapHeader>" +
                "  </soap:Header>" +
                "  <soap:Body>" +
                "    <Login xmlns=\"LDBM4S\">" +
                "      <UName>@userName</UName>" +
                "      <Pwd>@passWord</Pwd>" +
                "    </Login>" +
                "  </soap:Body>" +
                "</soap:Envelope>";
        Login = Login.replace("@sysUser",SYS_USER);
        Login = Login.replace("@sysPassword",SYS_PASSWORD);
        Login = Login.replace("@userName",userName);
        return Login;
    }

    /**获取考试查询*/
    public String getRaceQuery(String userName) {
        Login =  "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "  <soap:Header>" +
                "    <MySoapHeader xmlns=\"LDBM4S\">" +
                "      <UserName>@sysUser</UserName>" +
                "      <PassWord>@sysPassword</PassWord>" +
                "    </MySoapHeader>" +
                "  </soap:Header>" +
                "  <soap:Body>" +
                "    <Login xmlns=\"LDBM4S\">" +
                "      <UName>@userName</UName>" +
                "      <Pwd>@passWord</Pwd>" +
                "    </Login>" +
                "  </soap:Body>" +
                "</soap:Envelope>";
        Login = Login.replace("@sysUser",SYS_USER);
        Login = Login.replace("@sysPassword",SYS_PASSWORD);
        Login = Login.replace("@userName",userName);
        return Login;
    }

}
