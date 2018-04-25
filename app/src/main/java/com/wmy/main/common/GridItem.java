package com.wmy.main.common;
/**
 * @author Kiritor
 * 实现自己的View继承Checable接口
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wmy.main.R;

public class GridItem extends RelativeLayout implements Checkable {

    private Context mContext;
    private boolean mChecked;//判断该选项是否被选上的标志量
    private ImageView mImgView = null;
    private ImageView mSecletView = null;
    private TextView name = null;
    public ImageView getmImgView() {
        return mImgView;
    }

    public void setmImgView(ImageView mImgView) {
        this.mImgView = mImgView;
    }

    public void setmSecletView(ImageView mSecletView) {
        this.mSecletView = mSecletView;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public GridItem(Context context) {
        this(context, null, 0);
    }

    public GridItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.activity_gallery_grid_item, this);
        mImgView = (ImageView) findViewById(R.id.img_view);
        mSecletView = (ImageView) findViewById(R.id.select);
        name=  findViewById(R.id.name);
    }

    public void setName(String name) {
        this.name.setText(name);
    }

    @Override
    public void setChecked(boolean checked) {
        // TODO Auto-generated method stub
        mChecked = checked;
        setBackgroundDrawable(checked ? ContextCompat.getDrawable(mContext,
                R.drawable.background) : null);
        mSecletView.setBackground(checked ? ContextCompat.getDrawable(mContext,
                R.drawable.ptyd_txrzrxx_icon_e) :ContextCompat.getDrawable(mContext,
                R.drawable.ptyd_txrzrxx_icon_f));//选上了则显示小勾图片
    }

    @Override
    public boolean isChecked() {
        // TODO Auto-generated method stub
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setImgResId(int resId) {
        if (mImgView != null) {
            mImgView.setBackgroundResource(resId);//设置背景
        }
    }
}
