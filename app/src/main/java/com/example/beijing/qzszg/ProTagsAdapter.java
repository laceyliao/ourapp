package com.example.beijing.qzszg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 卢亮 on 2018/7/13.
 */

public class ProTagsAdapter extends BaseAdapter {
    private List<String> lists;
    private Context context;
    ProTagsAdapter(List<String> lists, Context context){
        this.lists = lists;
        this.context = context;
    }
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView ==null){
            convertView = LayoutInflater.from(SelectTag.getContext()).inflate(R.layout.tags_view, parent, false);
            holder = new ViewHolder();
            holder.tag = convertView.findViewById(R.id.tag_name);
            holder.tag.setText(lists.get(position));
            holder.tag.setTextSize(20);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            holder.tag.setText(lists.get(position));

        }
        return convertView;
    }
    private static class ViewHolder{
        TextView tag;
    }
}
