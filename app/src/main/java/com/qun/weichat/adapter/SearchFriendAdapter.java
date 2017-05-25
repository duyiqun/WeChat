package com.qun.weichat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.hyphenate.util.DateUtils;
import com.qun.weichat.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Qun on 2017/5/25.
 */

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.SearchViewHolder> {

    private List<AVUser> mAVUserList;
    private SearchViewHolder mSearchViewHolder;
    private List<String> myFriendList;

    public SearchFriendAdapter(List<AVUser> AVUserList, List<String> myFriendList) {
        this.mAVUserList = AVUserList;
        this.myFriendList = myFriendList;
    }

    @Override
    public SearchFriendAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search_friend, parent, false);
        mSearchViewHolder = new SearchViewHolder(view);
        return mSearchViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchFriendAdapter.SearchViewHolder holder, final int position) {
        final AVUser avUser = mAVUserList.get(position);
        String username = avUser.getUsername();
        Date date = avUser.getCreatedAt();
        holder.mTvUsername.setText(username);
        holder.mTvTime.setText(DateUtils.getTimestampString(date));
        //判断username是否已经是好友了
        if (myFriendList.contains(username)) {
            //username已经是我的好友了
            holder.mBtnAdd.setText("已是好友");
            holder.mBtnAdd.setEnabled(false);
        } else {
            holder.mBtnAdd.setText("添加");
            holder.mBtnAdd.setEnabled(true);
        }
        holder.mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddBtnClickListener != null) {
                    mOnAddBtnClickListener.onClick(avUser, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAVUserList == null ? 0 : mAVUserList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        private final Button mBtnAdd;
        private final TextView mTvTime;
        private final TextView mTvUsername;

        public SearchViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mBtnAdd = (Button) itemView.findViewById(R.id.btn_add);
        }
    }

    public interface OnAddBtnClickListener {
        void onClick(AVUser avUser, int position);
    }

    private OnAddBtnClickListener mOnAddBtnClickListener;

    public void setOnAddBtnClickListener(OnAddBtnClickListener onAddBtnClickListener) {
        this.mOnAddBtnClickListener = onAddBtnClickListener;
    }
}
