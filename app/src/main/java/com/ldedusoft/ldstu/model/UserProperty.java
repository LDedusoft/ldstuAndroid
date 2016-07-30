package com.ldedusoft.ldstu.model;

/**
 * Created by wangjianwei on 2016/6/23.
 */
public class UserProperty {
    /*单例对象*/
    private static UserProperty user ;

    private String userName;

    private String passWord;

    private int userType;

    /*私有构造函数*/
    private UserProperty(){
    }

    /*获取单例对象*/
    public static UserProperty getInstance(){
        if(user == null){
            user = new UserProperty();
        }
        return user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }
}
