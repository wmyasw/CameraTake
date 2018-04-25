package com.wmy.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Contacts;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import com.wmy.main.R;
import com.wmy.main.base.BaseActivity;
import com.wmy.main.common.AppConstant;
import com.wmy.main.common.TPApp;
import com.wmy.main.utils.CameraUtil;
import com.wmy.main.utils.DateUtil;
import com.wmy.main.utils.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KingJA on 2016/1/24.
 */
public class CustomCameraActivity extends Activity implements SurfaceHolder.Callback, View.OnClickListener {
    private Camera mCamera;

    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private ImageButton shutter;
    private ImageView iv_preview;
    private TextView tv_date;
    private File imageFile;
    private Bitmap newBitmap;
    private List<String> imgList = new ArrayList<String>();

    private PopupWindow pw;
    private ScaleAnimation sa;
    private View popupView;
    private RelativeLayout rl_bottom;
    private String imgUrl;
    private ScaleGestureDetector mScaleGestureDetector = null;
    private SurfaceHolder mSurfaceHolder = null;
    private Bitmap mBitmap = null;
    /**
     * 拍照回调
     */
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            imageFile = CameraUtil.createImageFile();
            try {

                FileOutputStream fis = new FileOutputStream(imageFile);
                fis.write(data);
                fis.close();
                Bitmap oldBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                iv_preview.setImageBitmap(oldBitmap);
//                CameraUtil.bitmap2Location(newBitmap,imageFile.getAbsolutePath());

            } catch (IOException e) {
                e.printStackTrace();
            }

            takePicTime = DateUtil.getSecond(System.currentTimeMillis());

            handleImg();//处理照片
            releaseCameraAndPreview();
        }
    };
    private String takePicTime;

    private void handleImg() {
        if (imgList.size() > 0) {
            imgList.clear();
        }
        if (imageFile == null) return;
//        imgUrl = imageFile.getAbsolutePath();
        imgUrl = AppConstant.filepath + "/" + imageFile.getName();
        Intent intent = new Intent(CustomCameraActivity.this, ShowActivity.class);
        intent.putExtra(AppConstant.KEY.IMG_PATH, imgUrl);
//        intent.putExtra("type", Constant.FROM_CAMERA);
//        intent.putExtra("takePicTime", takePicTime);
        startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.custom_camera);


        initView();

        //初始化popupwindow

        //SQLiteDatabase db = Connector.getDatabase();


    }

    //独立线程 处理对焦
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mCamera != null) {
                mCamera.autoFocus(myAutoFocusCallback);//自动对焦
            }

        }
    };
    //自动对焦
    Camera.AutoFocusCallback myAutoFocusCallback = new Camera.AutoFocusCallback() {

        public void onAutoFocus(boolean success, Camera camera) {
            // TODO Auto-generated method stub
            if (success)//success表示对焦成功
            {

                TPApp.getmMainThreadHandler().removeCallbacks(runnable);
                //myCamera.setOneShotPreviewCallback(null);

            } else {
                //未对焦成功
                TPApp.getmMainThreadHandler().post(runnable);

            }


        }
    };

    //    private final Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
//        @Override
//        public void onAutoFocus(boolean success, Camera camera) {
//            if (success && isPreviewing()) {
//                mCamera.setOneShotPreviewCallback(mPreviewCallback);
//            } else {
//                mCameraAutoFocusHandler.sendEmptyMessageDelayed(MSG_DO_AUTO_FOCUS, TIME_BETWEEN_CAPTURE);
//            }
//        }
//    };
    private void initView() {
//        tv_more = (TextView) findViewById(R.id.tv_more);
//        tv_more.setOnClickListener(this);
        rl_bottom = (RelativeLayout) findViewById(R.id.rl_bottom);

        mPreview = (SurfaceView) findViewById(R.id.preview);
        shutter = (ImageButton) findViewById(R.id.shutter);
        tv_date = (TextView) findViewById(R.id.tv_date);
        iv_preview = (ImageView) findViewById(R.id.iv_preview);
        iv_preview.setOnClickListener(this);
        tv_date.setText(CameraUtil.getDateString());
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);
//        mPreview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mCamera.autoFocus(null);
//            }
//        });
//        mSurfaceHolder = mPreview.getHolder();
//        mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureListener());
//        mPreview.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                return mScaleGestureDetector.onTouchEvent(motionEvent);
//            }
//        });
        mHolder.setKeepScreenOn(true);//设置屏幕常亮
    }

    Camera.Parameters parameters;

    /**
     * 快门
     *
     * @param view
     */
    public void shutter(View view) {
        parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);

//        parameters.setPreviewSize(1280,720);
        List<Camera.Size> pictureSizes = parameters.getSupportedPictureSizes();

        // parameters.setPictureSize(1280,720);
//        parameters.setPictureSize(1024,768);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = screenWidth = display.getWidth();
        int screenHeight = screenHeight = display.getHeight();
        Camera.Parameters mParameters = mCamera.getParameters();
        mParameters.setPictureSize(screenWidth, screenHeight);
//        parameters.setPictureSize(pictureSizes.get(pictureSizes.size() - 4).width, pictureSizes.get(pictureSizes.size() - 4).height);
        parameters.setRotation(90);
        parameters.setFlashMode(Camera.Parameters.FOCUS_MODE_AUTO);

//        mCamera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                if (success) {
//                    //mCamera.takePicture(null,null,pictureCallback);
//                    mCamera.cancelAutoFocus();
//                }
//            }
//        });
        mCamera.setParameters(parameters);
        mCamera.takePicture(null, null, pictureCallback);

        // System.out.println("===相机参数=="+mCamera.getParameters().flatten().toString());
//        for (int i = 0; i < pictureSizes.size(); i++) {
//            System.out.println("相机参数"+ pictureSizes.get(i).width + "x" + pictureSizes.get(i).height);
//        }
    }

    private SeekBar seekBar;

    /**
     * 获取系统相机
     *
     * @return
     */
    private Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            camera = null;
        }
        return camera;
    }

    /**
     * 释放相机
     */
    public void releaseCameraAndPreview() {
        if (mCamera != null) {
            // 停止预览
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            // 释放相机资源
            mCamera.release();
            mCamera = null;
        }

    }

    public void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);
            camera.startPreview();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("111111111111", "1111111111111");
        // 在activity运行时绑定
        if (mCamera == null) {
            mCamera = getCamera();
            if (mHolder != null) {
                setStartPreview(mCamera, mHolder);
            }
        }
        if (iv_preview != null)
            iv_preview.setImageBitmap(null);
        if (FileUtils.isFile(imgUrl)) {
            Bitmap oldBitmap = BitmapFactory.decodeFile(imgUrl);
            iv_preview.setImageBitmap(oldBitmap);
        }
        int staffType = getIntent().getIntExtra("staffType", 1);
        if (staffType == 2) {
        }
//        mCamera.autoFocus(new Camera.AutoFocusCallback() {
//            @Override
//            public void onAutoFocus(boolean success, Camera camera) {
//                camera.cancelAutoFocus();
//            }
//        });
//        TPApp.getmMainThreadHandler().post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCameraAndPreview();

        TPApp.getmMainThreadHandler().removeCallbacks(runnable);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
        setStartPreview(mCamera, mHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releaseCameraAndPreview();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_preview://更多
            {
                if (FileUtils.isFile(imgUrl)) {
                    handleImg();
                } else {
                    imgUrl = null;
                }
            }
            break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
        TPApp.getmMainThreadHandler().removeCallbacks(runnable);
        FileUtils.deleteFile(imgUrl);
    }



    public class ScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub78
            Matrix mMatrix = new Matrix();
            //缩放比例
            float scale = detector.getScaleFactor() / 3;
            mMatrix.setScale(scale, scale);

            //锁定整个SurfaceView
            Canvas mCanvas = mSurfaceHolder.lockCanvas();
            if(mCanvas!=null)
                //清屏
                mCanvas.drawColor(Color.BLACK);
                //画缩放后的图
                mCanvas.drawBitmap(mBitmap, mMatrix, null);
            //绘制完成，提交修改
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            //重新锁一次
            mSurfaceHolder.lockCanvas(new Rect(0, 0, 0, 0));
            mSurfaceHolder.unlockCanvasAndPost(mCanvas);

            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
            //一定要返回true才会进入onScale()这个函数
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            // TODO Auto-generated method stub
        }

    }

}
