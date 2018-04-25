package com.wmy.main.login;

import android.content.Context;
import android.content.Intent;

import com.wmy.main.activity.LoginActivity;

/**
 * @author wmy
 * @Description: 已登出状态
 * @FileName: LoginedState
 * @Date 2018/4/20/020 11:30
 */
public class LogoutState implements UserState {
    @Override
    public void uplod(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }
}
