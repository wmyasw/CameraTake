package com.wmy.main.login;

import com.wmy.main.common.AppConstant;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/4/20/020.
 */

public class User implements Serializable,Cloneable{
    public String username;
    public String password;
    public int user_id;
    public String url=    AppConstant.getAppConstant().getBaseUrl();

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", user_id=" + user_id +
                '}';
    }

    @Override
    protected User clone()  {
        User user=null;
        try{
            user= (User) super.clone();

        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return user;
    }
}
