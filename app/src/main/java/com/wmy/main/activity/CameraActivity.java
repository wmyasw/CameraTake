package com.wmy.main.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.github.florent37.camerafragment.CameraFragment;
import com.github.florent37.camerafragment.PreviewActivity;
import com.github.florent37.camerafragment.configuration.Configuration;
import com.github.florent37.camerafragment.listeners.CameraFragmentResultListener;
import com.wmy.main.R;
import com.wmy.main.base.BaseActivity;
import com.wmy.main.common.AppConstant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class CameraActivity extends BaseActivity {
    public static final String FRAGMENT_TAG = "camera";

    @BindView(R.id.shutter)
    ImageView shutter;
    CameraFragment cameraFragment;

    @SuppressLint({"MissingPermission", "NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);


        //初始化popupwindow

        //SQLiteDatabase db = Connector.getDatabase();
        addCamera();
    }

    @RequiresPermission(Manifest.permission.CAMERA)
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addCamera() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        cameraFragment = CameraFragment.newInstance(new Configuration.Builder().build());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content, cameraFragment, FRAGMENT_TAG)
                .commit();
        cameraFragment.setResultListener(new CameraFragmentResultListener() {

            @Override
            public void onVideoRecorded(String filePath) {

            }

            @Override
            public void onPhotoTaken(byte[] bytes, String filePath) {
                //called when the photo is taken and saved
                Intent intent = new Intent(CameraActivity.this, ShowActivity.class);
                intent.putExtra(AppConstant.KEY.IMG_PATH, filePath);
//        intent.putExtra("type", Constant.FROM_CAMERA);
//        intent.putExtra("takePicTime", takePicTime);
                startActivity(intent);
            }
        });

    }


    @OnClick({R.id.shutter, R.id.settings_view, R.id.flash_switch_view, R.id.front_back_camera_switcher})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.flash_switch_view:
                cameraFragment.toggleFlashMode();
                break;
            case R.id.front_back_camera_switcher:
                cameraFragment.switchCameraTypeFrontBack();
                break;
            case R.id.settings_view:
                cameraFragment.openSettingDialog();
                break;
            case R.id.shutter:
                cameraFragment.toggleFlashMode();
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(System.currentTimeMillis());
                String imageFileName = "IMG_" + timeStamp;
                cameraFragment.takePhotoOrCaptureVideo(new CameraFragmentResultListener() {
                    @Override
                    public void onVideoRecorded(String filePath) {

                    }

                    @Override
                    public void onPhotoTaken(byte[] bytes, String filePath) {

                    }
                }, AppConstant.filepath, imageFileName);
                break;
        }

    }
}