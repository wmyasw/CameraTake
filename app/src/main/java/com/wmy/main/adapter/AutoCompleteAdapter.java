package com.wmy.main.adapter;

import android.content.Context;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wmy.main.R;
import com.wmy.main.common.GridItem;
import com.wmy.main.entity.NewsParper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/25/025.
 */

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    Context context;
    private ArrayFilter mFilter;
    List<NewsParper> mlist;
    private int selectPosition;
    private ArrayList<NewsParper> mUnfilteredData;
    private ArrayList<String> mList;
    public AutoCompleteAdapter(Context context, List<NewsParper> list) {
        this.mlist = list;
        this.context = context;
    }

    public void setData(  List<NewsParper> mlist){
        this.mlist=mlist;
        System.out.println("mlist :"+mlist.size());
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mlist==null?0:mlist.size();
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    @Override
    public Object getItem(int i) {
        selectPosition=i;
        return  mlist==null?null:mlist.get(i);
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if (convertView == null) {
            convertView = View.inflate(context,R.layout.activity_list_item, null);
            viewHolder=new ViewHolder();
            viewHolder.name=convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.name.setText(mlist.get(i).getName());
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new ArrayFilter();
        }
        return mFilter;
    }

    private  class ViewHolder {
        private TextView name;//文字

    }

    private class ArrayFilter
            extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mUnfilteredData ==
                    null) {
                mUnfilteredData = new ArrayList<>();
            }

            if (prefix == null || prefix.length() ==
                    0) {
                ArrayList<NewsParper> list = (ArrayList<NewsParper>) mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                ArrayList<NewsParper> unfilteredValues = (ArrayList<NewsParper>) mUnfilteredData;
                int count = unfilteredValues.size();

                ArrayList<NewsParper> newValues =
                        new ArrayList<NewsParper>(count);

                for (int i =
                     0; i < count; i++) {
                    NewsParper pc = unfilteredValues.get(i);
                    if (pc !=
                            null) {

                        if(pc.getName()!=null && pc.getName().startsWith(prefixString)){

                            newValues.add(pc);
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            //noinspection unchecked
            mList = (ArrayList<String>) results.values;
            if (results.count >
                    0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
