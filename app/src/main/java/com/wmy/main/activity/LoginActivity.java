package com.wmy.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.unstoppable.submitbuttonview.SubmitButton;
import com.wmy.main.R;
import com.wmy.main.base.BaseActivity;
import com.wmy.main.common.AppConstant;
import com.wmy.main.common.JsonCallback;
import com.wmy.main.common.Url;
import com.wmy.main.entity.ResultEntity;
import com.wmy.main.login.LoginContext;
import com.wmy.main.login.LoginedState;
import com.wmy.main.login.User;
import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.setting.PermissionSetting;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wmy
 * @Description: 登录页
 * @FileName: LoginActivity
 * @Date 2018/4/19/019 19:12
 */
public class LoginActivity extends BaseActivity {
    private static final int REQUEST_CODE = 919; // 请求码

    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.http_url)
    EditText http_url;

    @BindView(R.id.submitbutton)
    SubmitButton submitbutton;

    private User user;
    PermissionSetting mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }


    @OnClick(R.id.submitbutton)
    public void click(View view) {
        login();
    }
    private void login() {
        if (!TextUtils.isEmpty(http_url.getText())) {
            savaUrl(http_url.getText().toString().trim());
            AppConstant.getAppConstant().setBaseUrl(http_url.getText().toString().trim());

        }
        OkGo.<ResultEntity<User>>get( AppConstant.getAppConstant().getBaseUrl()+Url.Login_url)//
                .tag(this)//
                //.isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("login_name", username.getText().toString())        // 这里可以上传参数
                .params("password", password.getText().toString())   // 可以添加文件上传
                .execute(new JsonCallback<ResultEntity<User>>() {
                    @Override
                    public void onError(Response<ResultEntity<User>> response) {
                        super.onError(response);
                        showToast(response.getException().getMessage());
                        submitbutton.reset();
                    }

                    @Override
                    public void onSuccess(final Response<ResultEntity<User>> response) {
                        Log.d("response", response.isSuccessful() + "");
                        if (!response.isSuccessful()) {
                            submitbutton.reset();
                            return;
                        }
                        final ResultEntity<User> users = response.body();
                        if (users.getErrcode() == 0) {
                            submitbutton.doResult(true);
                            submitbutton.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
                                @Override
                                public void onResultEnd() {

                                    user = new Gson().fromJson(response.body() + "", new TypeToken<User>() {
                                    }.getType());
                                    user.setPassword(password.getText().toString());
                                    user.setUsername(username.getText().toString());
                                    user.setUrl(AppConstant.getAppConstant().getBaseUrl());
                                    user.setUser_id(users.getData().get(0).getUser_id());

//                                    user.setUser_id(((User)entity.getData().get(0)).getUser_id());
                                    LoginContext.getLoginContext().setUser(user);
                                    LoginContext.getLoginContext().setUserState(new LoginedState());
                                    statPhotos();
                                    submitbutton.reset();
                                }
                            });
                        } else {
                            submitbutton.doResult(false);
                            showToast(users.getErrmsg());
                            submitbutton.reset();
                        }
                    }
                });
    }

    private void resutl(String result) {
        try {
            User entity = new Gson().fromJson(result, new TypeToken<User>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 跳转到图片集合页
     */
    private void statPhotos() {
        startActivity(new Intent(this, PhotosActivity.class));
        submitbutton.setProgress(100);
    }

    @SuppressLint("NewApi")
    protected void init() {
//        SharedPreferences sp = getSharedPreferences("network_url", 0);
        http_url.setText(getUrl());
        submitbutton.setOnResultEndListener(new SubmitButton.OnResultEndListener() {
            @Override
            public void onResultEnd() {
                submitbutton.reset();
            }
        });
        user = new User();
        requestPermission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
    }
    private void savaUrl(String url){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putString("url", url);
        edit.commit();//提交修改
    }
    private String getUrl(){
        SharedPreferences sp = getPreferences(MODE_PRIVATE);
        return  sp.getString("url",AppConstant.getAppConstant().getBaseUrl());
    }
}

