package com.wmy.main.login;

import android.content.Context;

/**
 * @author wmy
 * @Description: 用户接口及状态管理
 * @FileName: LoginContext
 * @Date 2018/4/20/020 11:28
 */
public class LoginContext {


    private User user;
    //设置登录状态为登出
    private UserState userState = new LogoutState();
    //单例模式(懒汉加载)
    private static LoginContext loginContext;

    private LoginContext() {
    }

    public static LoginContext getLoginContext() {
        if (loginContext == null) {
            loginContext = new LoginContext();
        }
        return loginContext;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public void upload(Context context) {
        userState.uplod(context);
    }

    /**
     * 对外不可修改
     * @return
     */
    public User getUser() {
        return  user.clone();
    }

    public void setUser(User user) {
        this.user = user;
    }

}
