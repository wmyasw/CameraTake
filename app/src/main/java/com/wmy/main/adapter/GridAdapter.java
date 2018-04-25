package com.wmy.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.wmy.main.R;
import com.wmy.main.common.AppConstant;
import com.wmy.main.common.GridItem;
import com.wmy.main.entity.UplaodEntity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridAdapter extends BaseAdapter {

    private List<UplaodEntity.ImagesBean> list_data;
    private LayoutInflater inflater;
    private Context context;
    private Map<Integer, Boolean> mSelectMap ;
    String path = AppConstant.filepath + "/";
    public GridAdapter(Context ctx, ArrayList<UplaodEntity.ImagesBean> dirAllStrArr, Map mSelectMap) {
        context = ctx;
        list_data=dirAllStrArr;
        this.mSelectMap=mSelectMap;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return list_data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
 /* 得到View */
    public View getView(int position, View convertView, ViewGroup parent) {
        GridItem item;
        if (convertView == null) {
            item = new GridItem(context);
            convertView=   LayoutInflater.from(context).inflate(R.layout.activity_gallery_grid_item, null);
            item.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            item.setmImgView((ImageView) convertView.findViewById(R.id.img_view));
            item.setmSecletView((ImageView) convertView.findViewById(R.id.select));
            item.setName((TextView) convertView.findViewById(R.id.name));
            convertView.setTag(item);
        } else {
            item = (GridItem) convertView.getTag();
        }


        System.out.println(" name: "+  list_data.get(position).getName()+".jpg");
        DownloadImg(path+list_data.get(position).getName()+".jpg",item.getmImgView());
        item.setChecked(mSelectMap.get(position) == null ? false: (Boolean) mSelectMap.get(position));
        item.setName(list_data.get(position).getName());
        return convertView;
    }

    private void DownloadImg(String url, ImageView imageView) {
//        imageView.setImageBitmap( BitmapFactory.decodeFile(url));
        RequestOptions options = new RequestOptions()
                .override(400,400).centerCrop()
                .placeholder(R.mipmap.ic_launcher)  .error(R.mipmap.ic_launcher) .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        File file = new File( url);
        Glide.with(context.getApplicationContext())
                .load(file) .apply(options)
                .into(imageView);
    }



    /**
     * 获取选择数据
     * @return
     */
    public List<UplaodEntity.ImagesBean> getCheckList(){
        List<UplaodEntity.ImagesBean> list=new ArrayList<>();
        for(int key:mSelectMap.keySet())
        {
            System.out.println("Key: "+key+" Value: "+mSelectMap.get(key));
            if(mSelectMap.get(key)) {
                list.add(list_data.get(key));

                System.out.println("Key: "+key+"   list_data.get(key).getName(): "+  list_data.get(key).getName());
            }
        }
        System.out.println(" list.size: "+list.size());
        return list;
    }
}
  
