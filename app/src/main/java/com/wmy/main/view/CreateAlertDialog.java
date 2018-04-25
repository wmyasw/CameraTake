package com.wmy.main.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.wmy.main.R;


/**
 * @author wmy
 * @Description: 创建不同样式的 dialog
 * @FileName: CreateAlertDialog
 * @Date 2017/10/24/024 14:02
 */
public class CreateAlertDialog {
    private static AlertDialog dialog;
    CreateAlertDialog createAlertDialog;
    CreateAlertDialog(){

    }
    private CreateAlertDialog getInstance(){
        if(createAlertDialog==null){
            createAlertDialog=new CreateAlertDialog();
        }
        return createAlertDialog;
    }


    /**
     * alert 布局以子view 为基准
     * @param context
     * @param layout_id
     * @return
     */
    public static AlertDialog createDialog(Activity context,int layout_id){
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(layout_id, null);

        //初始化dialog
        dialog = new AlertDialog.Builder(context, R.style.CustomAlertDialogBackground).create();
        dialog.show();
//        dialog.setContentView(view);
        dialog.setContentView(layout_id);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;// (int) (display.getHeight() * 0.7);


        dialog.getWindow().setAttributes(layoutParams);
//        dialog.setContentView(R.layout.dialog_room_detail);

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        return dialog;
    }
    public static AlertDialog createAlert(Activity context,int layout_id){
        LayoutInflater inflater = context.getLayoutInflater();
        View view = inflater.inflate(layout_id, null);

        //初始化dialog
        dialog = new AlertDialog.Builder(context, R.style.AlertDialogBackground).create();
        dialog.show();
        dialog.setContentView(view);
        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;// (int) (display.getHeight() * 0.7);


        dialog.getWindow().setAttributes(layoutParams);
//        dialog.setContentView(R.layout.dialog_room_detail);

        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        return dialog;
    }
}
