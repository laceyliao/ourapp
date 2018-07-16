package com.example.beijing.qzszg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by admin on 2018/7/14.
 */

public class ProContentsAdapter extends BaseAdapter {
    private List<Q_SingleChoose> list;
    private List<Integer> listid;
    private Context context;
    public ProContentsAdapter(List<Q_SingleChoose> list , List<Integer> listid, Context context){
        this.list=list;
        this.listid=listid;
        this.context=context;
    }
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null){
            convertView = LayoutInflater.from(SearchActivity.getContext()).inflate(R.layout.contents_view,parent,false);
            holder = new ViewHolder();
            holder.content = convertView.findViewById(R.id.contents_name);
            holder.content.setText(list.get(position).getDescribe().trim());
            holder.content.setTextSize(16);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
            holder.content.setText((list.get(position).getDescribe().trim()));
        }
        return convertView;
    }

    private static class ViewHolder{
        TextView content;
    }
}
