package com.qun.slidemenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Qun on 2017/5/10.
 */

public class MainAdapter extends BaseAdapter {

    private List<String> mDatas;

    public MainAdapter(List<String> datas) {
        this.mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_avatar);
            viewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.mTextView.setText(mDatas.get(position));
        switch (position % 3) {
            case 0:
                viewHolder.mImageView.setImageResource(R.mipmap.head_1);
                break;

            case 1:
                viewHolder.mImageView.setImageResource(R.mipmap.head_2);
                break;

            case 2:
                viewHolder.mImageView.setImageResource(R.mipmap.head_3);
                break;
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView mImageView;
        TextView mTextView;
    }
}
