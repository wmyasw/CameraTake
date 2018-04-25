package com.wmy.main.activity;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.base.ProgressRequestBody;
import com.lzy.okgo.request.base.Request;
import com.wmy.main.R;
import com.wmy.main.adapter.AutoCompleteAdapter;
import com.wmy.main.adapter.GridAdapter;
import com.wmy.main.base.BaseActivity;
import com.wmy.main.common.AppConstant;
import com.wmy.main.common.JsonCallback;
import com.wmy.main.common.TPApp;
import com.wmy.main.common.Url;
import com.wmy.main.entity.NewsParper;
import com.wmy.main.entity.ResultEntity;
import com.wmy.main.entity.UplaodEntity;
import com.wmy.main.login.User;
import com.wmy.main.utils.DateUtil;
import com.wmy.main.utils.FileUtils;
import com.wmy.main.view.CustomDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

public class PhotosActivity extends BaseActivity implements AbsListView.MultiChoiceModeListener {

    private static final String TAG = "PhotosActivity";
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 919; // 请求码
    public static int REQUEST_CODE = 101;
    @BindView(R.id.upload)
    Button upload;
    String filePath;  //图片路径
    Uri photoUri;
    @BindView(R.id.gridview)
    GridView gridview;
    @BindView(R.id.btn_selected)
    Button btn_selected;
    @BindView(R.id.btn_del)
    Button btn_del;
    EditText et_date_v;//显示日期
    ImageView select_date; //选择日期
    private LinearLayoutManager gManager;
    // 用于存放sdcard卡上的所有图片路径
    public ArrayList<UplaodEntity.ImagesBean> dirAllStrArr = new ArrayList<>();
    GridAdapter adapter;
    AutoCompleteAdapter autoCompleteAdapter;
    Date myDate;
    List<NewsParper> list;
    AutoCompleteTextView textView;
    int newParperId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        initToolBar(false, "拍照上传");
//        initControls();
        try {
            initGrid();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initCalendar();
    }

    /**
     * 初始化日期
     */
    private void initCalendar() {
        Calendar d = Calendar.getInstance(Locale.CHINA);
        // 创建一个日历引用d，通过静态方法getInstance() 从指定时区 Locale.CHINA 获得一个日期实例
        myDate = Calendar.getInstance().getTime();
        // 创建一个Date实例

        // 设置日历的时间，把一个新建Date实例myDate传入
        mYear = d.get(Calendar.YEAR);
        mMonth = d.get(Calendar.MONTH);
        mDay = d.get(Calendar.DAY_OF_MONTH);
    }


    @OnClick({R.id.upload,R.id.btn_del,R.id.btn_selected})
    public void click(View v) {
        switch (v.getId()){
            case R.id.upload:
                showConfirm();
                break;
            case R.id.btn_del:
                if(adapter.getCheckList()==null||adapter.getCheckList().size()==0) {
                    showToast("请选择图片");
                    return;
                }
                confirm("是否删除选中图片？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        if(adapter.getCheckList().size()==dirAllStrArr.size()){
                            FileUtils.deleteFiles(AppConstant.filepath);
                        }else {
                            for(int i=0;i<adapter.getCheckList().size();i++){
                                FileUtils.deleteFile(adapter.getCheckList().get(i).getFile_stream().getPath());
                            }
                        }
                        dirAllStrArr.clear();
                        try {
                            DirAll(new File(AppConstant.filepath));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                });
                break;
            case R.id.btn_selected:
                if(dirAllStrArr==null||dirAllStrArr.size()==0) return;
                if(dirAllStrArr.size()==mSelectMap.size()){
                    reverseSelection();
                    btn_selected.setText("全选");
                }
                else {
                    btn_selected.setText("反选");
                    selectAll();
                }
                break;
        }

    }

    /**
     * 全选
     */
    private void selectAll(){
        for(int i=0;i<dirAllStrArr.size();i++){
            mSelectMap.put(i,  true );/* 放入选中的集合中 */
            gridview.setItemChecked(i, mSelectMap.get(i));
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * 反选
     */
    private void reverseSelection(){
        mSelectMap.clear();
//        for(int i=0;i<dirAllStrArr.size();i++){
//            mSelectMap.put(i,false );/* 放入选中的集合中 */
//            gridview.setItemChecked(i, mSelectMap.get(i));
//        }
        adapter.notifyDataSetChanged();
    }
    /**
     * 获取联想输入信息
     *
     * @param name
     */
    private void getTitle(String name) {
//
        OkGo.<ResultEntity<NewsParper>>post( AppConstant.getAppConstant().getBaseUrl()+Url.get_newsparper_name)//
                .tag(this)//
                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("name", name)        // 这里可以上传参数
                .execute(new JsonCallback<ResultEntity<NewsParper>>() {
                    @Override
                    public void onError(com.lzy.okgo.model.Response<ResultEntity<NewsParper>> response) {
                        super.onError(response);
                        dissmissDialog();
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<ResultEntity<NewsParper>> response) {

                        if (response.isSuccessful()) {
                            dissmissDialog();
                            showToast(response.body().getErrmsg());
                            list = response.body().getData();
                            autoCompleteAdapter.setData(list);
//                            autoCompleteAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    /**
     * 多文件上传
     *
     * @param url
     * @param files 文件集合
     */
    private void uploadFiles(String url, List<UplaodEntity.ImagesBean> files) throws IOException {
        final MediaType mediaType = MediaType.parse("application/octet-stream; charset=utf-8");
        List<HttpParams.FileWrapper> list=new ArrayList<>();
        for (UplaodEntity.ImagesBean imagesBean : files) {
            HttpParams.FileWrapper fileWrapper=new HttpParams.FileWrapper(imagesBean.getFile_stream(),imagesBean.getName(),mediaType);
            list.add(fileWrapper);

        }
        OkGo.<ResultEntity<Object>>post( AppConstant.getAppConstant().getBaseUrl()+url)//
                .tag(this)//
                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("name", textView.getText() + "")        // 这里可以上传参数
                .params("user_id", "1")   // 可以添加文件上传
                .params("update_time", et_date_v.getText() + "")   // 可以添加文件上传
                .params("id", "1")   // 可以添加文件上传
                .addFileWrapperParams("images", list)    // 这里支持一个key传多个文件
                .uploadInterceptor(new ProgressRequestBody.UploadInterceptor() {
                    @Override
                    public void uploadProgress(Progress progress) {
                        if (pDialog != null) {
                            if(pDialog.isShowing())
                                pDialog.getProgressHelper().setProgress(progress.fraction);
                            if(progress.fraction==1){
                                pDialog.dismiss();
                            }
                        }
                    }
                })
//                .upJson(json.toString())
                .execute(new JsonCallback<ResultEntity<Object>>() {
                    @Override
                    public void onError(com.lzy.okgo.model.Response<ResultEntity<Object>> response) {
                        super.onError(response);
//                        warning(response.getException().getMessage());
                        showToast(response.getException().getMessage());
                        dissmissDialog();
                    }

                    @Override
                    public void onSuccess(com.lzy.okgo.model.Response<ResultEntity<Object>> response) {
//                        success(response.body().getErrmsg());
                        if(response.body()!=null &&response.body().getErrcode()==0){
                            showToast(response.body().getErrmsg());
                        }
                        dissmissDialog();
                    }
                });
    }

    /**
     * 动态权限验证
     *
     * @param permissions
     */
    protected void requestPermission(String... permissions) {
        AndPermission.with(this)
                .permission(permissions)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        showToast("2222"+permissions.get(0));
                        startCamera(REQUEST_CODE, photoUri);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(PhotosActivity.this, permissions)) {
//                            showToast("3333"+permissions.get(0));
                            finish();
                        }

                    }
                })
                .start();
    }

    /**
     * 初始化图片集
     * @throws Exception
     */
    private void initGrid() throws Exception {
        DirAll(new File(AppConstant.filepath));
        adapter = new GridAdapter(this, dirAllStrArr, mSelectMap);
        gridview.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);// 设置为多选模式
        gridview.setAdapter(adapter);// 数据适配
        gridview.setMultiChoiceModeListener(this);// 设置多选模式监听器
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectMap.put(position, mSelectMap.get(position) == null ? true : !mSelectMap.get(position));/* 放入选中的集合中 */
                gridview.setItemChecked(position, mSelectMap.get(position));
                adapter.getCheckList();
            }
        });

    }


    /**
     * 图片保存路径  这里是在SD卡目录下创建了MyPhoto文件夹
     *
     * @param fileName
     * @return
     */
    private Uri patrUri(String fileName) {  //指定了图片的名字，可以使用时间来命名
        // TODO Auto-generated method stub
        String strPhotoName = fileName + ".jpg";
        String savePath = AppConstant.filepath + "/";
        File dir = new File(savePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        filePath = savePath + strPhotoName;
        return Uri.fromFile(new File(dir, strPhotoName));
    }
    /***********************************相机相关 start**********************/
    /**
     * 启动相机
     */
    private void startCamera() {
        // 进行正常操作
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            photoUri = patrUri(System.currentTimeMillis() + "");
            requestPermission(Permission.CAMERA, Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE);
        } else {
            showToast("SD卡不可用");
        }

    }

    //打开相机
    private void startCamera(int requestCode, Uri photoUri) {
        // TODO Auto-generated method stub

        Intent intent = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, requestCode);
    }

    /***********************************相机相关end**********************/
    //页面回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            try {
                dirAllStrArr.clear();
                DirAll(new File(AppConstant.filepath));
                adapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 创建toolbar右侧按钮
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 此方法用于初始化菜单，其中menu参数就是即将要显示的Menu实例。 返回true则显示该menu,false 则不显示;
         * (只会在第一次初始化菜单时调用) Inflate the menu; this adds items to the action bar
         * if it is present.
         */
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_camera: {
                startCamera();
                break;
            }
        }
        return true;
    }

    // 用于遍历sdcard卡上所有文件的类
    public void DirAll(File dirFile) throws Exception {
        try {


            if (dirFile.exists()) {
                File files[] = dirFile.listFiles();
                for (File file : files) {
                    if (file.isDirectory()) {
                        String fileName = file.getName();
                        // 除sdcard上Android这个文件夹以外。
                        if (!fileName.endsWith("Android")) {
                            // 如果遇到文件夹则递归调用。
                            DirAll(file);
                        }
                    } else {
                        // 如果是图片文件压入数组
                        String fileName = file.getName();
                        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                                || fileName.endsWith(".bmp")
                                || fileName.endsWith(".gif")
                                || fileName.endsWith(".png")) {
                            UplaodEntity.ImagesBean map = new UplaodEntity.ImagesBean();
                            // 如果遇到文件则放入数组
                            if (dirFile.getPath().endsWith(File.separator)) {
                                map.setName(file.getName().substring(0, file.getName().length() - 4));
//                            map.put("path",dirFile.getPath()+ file.getName());
                                map.setFile_stream(file);
                                dirAllStrArr.add(map);
                            } else {
                                map.setName(file.getName().substring(0, file.getName().length() - 4));
//                            map.put("path",dirFile.getPath()+ file.getName());
                                map.setFile_stream(file);
                                dirAllStrArr.add(map);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Map<Integer, Boolean> mSelectMap = new HashMap<Integer, Boolean>();

    @Override
    public void onItemCheckedStateChanged(ActionMode actionMode, int position, long l, boolean checked) {
        // 当每个项状态改变的时候的操作
//        mActionText.setText(formatString(gridview.getCheckedItemCount()));
        Log.d(TAG, "position :" + position + " checked:" + checked + "");
        mSelectMap.put(position, checked);/* 放入选中的集合中 */

        if (checked) {

        }
        actionMode.invalidate();
    }

    TextView mActionText;

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        return false;

    }

    private static final int MENU_SELECT_ALL = 0;

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;

    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        return true;

    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        adapter.notifyDataSetChanged();
    }


    private void showConfirm() {
        CustomDialog customDialog;
        CustomDialog.Builder alert = new CustomDialog.Builder(this).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                dialogInterface.dismiss();
                try {
                    loading();
                    uploadFiles(Url.upload_image, adapter.getCheckList());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        alert.setTitle("请填写描述信息");
        alert.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        customDialog = alert.create();
        textView = customDialog.findViewById(R.id.tv_title_v);
        et_date_v = customDialog.findViewById(R.id.et_date_v);
        select_date = customDialog.findViewById(R.id.select_date);
        setAdapter();
        select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DATE_DIALOG);
            }
        });
        textView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                NewsParper obj = (NewsParper) parent.getItemAtPosition(position);
                textView.setText(obj.getName());
                newParperId = obj.getId();
            }

        });
        textView.add
        textView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                getTitle(charSequence.toString());

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        customDialog.show();


    }

    final int DATE_DIALOG = 1;
    int mYear, mMonth, mDay;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                DatePickerDialog pickerDialog = new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
//            pickerDialog.getDatePicker().setMaxDate();  //设置日期最大值

                pickerDialog.getDatePicker().setMinDate(myDate.getTime());
                return pickerDialog;
        }
        return null;
    }

    /**
     * 设置日期 利用StringBuffer追加
     */
    public void display() {
        et_date_v.setText(new StringBuffer().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };

    private void loading() {
        if (pDialog == null)
            pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("");
        pDialog.setCancelable(false);
        pDialog.show();
    }


    private void warning(String string) {
        if (pDialog == null)
         pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("");
        pDialog.setCancelText(string);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void success(String string) {
        if (pDialog == null)
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("");
        pDialog.setContentText(string);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    private void dissmissDialog() {
        if (pDialog != null)
            pDialog.dismissWithAnimation();

    }

    private void setAdapter() {
        list = new ArrayList<>();
        autoCompleteAdapter = new AutoCompleteAdapter(this, list);
        textView.setAdapter(autoCompleteAdapter);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelAll();
    }
}
