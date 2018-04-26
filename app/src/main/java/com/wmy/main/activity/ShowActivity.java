package com.wmy.main.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wmy.main.R;
import com.wmy.main.base.BaseActivity;
import com.wmy.main.common.AppConstant;
import com.wmy.main.utils.FileUtils;
import com.wmy.main.utils.PhotoBitmapUtils;
import com.wmy.main.view.QNumberPicker;
import com.wmy.main.view.photoview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ShowActivity extends BaseActivity {
    @BindView(R.id.img)
    PhotoView img;
    private int picWidth;
    private int picHeight;


    private String[] names = {"A", "B", "C", "D"};
    private String[] codes = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    private String path;

    private PopupWindow popupWindow;
    //名称
    private QNumberPicker id_name;
    //版号
    private QNumberPicker id_code;
    private TextView tv_title;

    //取消
    private TextView tv_cancle;
    //确认
    private TextView tv_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        init();
    }

    private void init() {
        initToolBar(true, "拍照上传");
        picWidth = getIntent().getIntExtra(AppConstant.KEY.PIC_WIDTH, 0);
        picHeight = getIntent().getIntExtra(AppConstant.KEY.PIC_HEIGHT, 0);
        path = getIntent().getStringExtra(AppConstant.KEY.IMG_PATH);
        Log.d("ShowPicActivity", path);
        img = (PhotoView) findViewById(R.id.img);
        Bitmap bitmap = PhotoBitmapUtils.rotaingImageView(90,BitmapFactory.decodeFile(path));
        if (bitmap == null) {
            showToast("图像不存在");
            finish();
        }
        ;
        img.setImageBitmap(bitmap);
        showPopupWindow();
    }

    @OnClick(R.id.img_ok)
    public void click(View view) {
        show();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        FileUtils.deleteFile(path);
    }


    //显示省市县
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.popwindow_location, null);

        //设置contentView
        popupWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        popupWindow.setContentView(contentView);
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        popupWindow.setBackgroundDrawable(dw);
        id_name = contentView.findViewById(R.id.number_picker);
        id_code = contentView.findViewById(R.id.number_picker_code);
        tv_confirm = contentView.findViewById(R.id.tv_confirm);
        tv_cancle = contentView.findViewById(R.id.tv_cancle);
        tv_title = contentView.findViewById(R.id.tv_title);
        id_name.setDisplayedValues(names);//设置需要显示的数组
        id_name.setMinValue(0);
        id_name.setWrapSelectorWheel(false);
        id_name.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        id_name.setMaxValue(names.length - 1);//这两行不能缺少,不然只能显示第一个，关联到format方法
        id_name.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return names[i];
            }
        });

        id_code.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        id_code.setDisplayedValues(codes);//设置需要显示的数组
        id_code.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return codes[i];
            }
        });
        id_code.setMinValue(0);
        id_code.setMaxValue(codes.length - 1);//这两行不能缺少,不然只能显示第一个，关联到format方法
        id_code.setWrapSelectorWheel(false);
        id_code.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                tv_title.setText(names[id_name.getValue()] + "" + codes[id_code.getValue()]);
            }
        });

        id_name.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                tv_title.setText(names[id_name.getValue()] + "" + codes[id_code.getValue()]);
            }
        });
        //设置默认配送方式
        popupWindow.setAnimationStyle(R.style.AnimBottom);

        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
//                Eyes.setStatusBarColor(this, ContextCompat.getColor(this, R.color.title_bg));
//                Eyes.setStatusBarColorForCollapsingToolbar(this, app_bar, collapsing_toolbar_layout, toolbar, ContextCompat.getColor(ShopProductDetailActivity.this, R.color.title_bg));
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = names[id_name.getValue()] + "" + codes[id_code.getValue()];
//                FileUtils.renameFile(path, AppConstant.filepath + "/" + name +".jpg");
                boolean isFile = false;
                if (FileUtils.isFile(AppConstant.filepath + "/" + name + ".jpg")) {

                    confirm("该名称的图片已存在,请重新输入", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                        }
                    });
                } else {
                    isFile = FileUtils.renameFile(path, AppConstant.filepath + "/" + name + ".jpg");
                    if (isFile) {
                        popupWindow.dismiss();
                        finish();
                    } else {
                        showToast("创建文件失败");
                    }
                }

            }
        });
        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
//                FileUtils.deleteFile(path);
            }
        });
        //设置默认值
        tv_title.setText(names[id_name.getValue()] + "" + codes[id_code.getValue()]);
    }

    //这个方法是根据index 格式化先生的文字,需要先 implements NumberPicker.Formatter
    private void show() {
        popupWindow.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
//        Eyes.setStatusBarColor(this, 0xb0000000);
    }
}
