package com.qun.weichat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.qun.weichat.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Qun on 2017/6/25.
 */

public class ConversationAdapter extends RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private List<EMConversation> mEMConversationList;

    public ConversationAdapter(List<EMConversation> EMConversationList) {
        mEMConversationList = EMConversationList;
    }

    @Override
    public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_conversation, parent, false);
        ConversationViewHolder conversationViewHolder = new ConversationViewHolder(view);
        return conversationViewHolder;
    }

    @Override
    public void onBindViewHolder(ConversationViewHolder holder, final int position) {
        final EMConversation emConversation = mEMConversationList.get(position);
        EMMessage lastMessage = emConversation.getLastMessage();
        //对方是谁
        String userName = lastMessage.getUserName();
        holder.mTvUsername.setText(userName);
        long msgTime = lastMessage.getMsgTime();
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        int unreadMsgCount = emConversation.getUnreadMsgCount();
        if (unreadMsgCount > 99) {
            holder.mTvUnread.setText("99+");
            holder.mTvUnread.setVisibility(View.VISIBLE);
        } else if (unreadMsgCount > 0) {
            holder.mTvUnread.setVisibility(View.VISIBLE);
            holder.mTvUnread.setText(unreadMsgCount + "");
        } else {
            holder.mTvUnread.setVisibility(View.GONE);
        }
        EMMessageBody body = lastMessage.getBody();
        if (body instanceof EMImageMessageBody) {
            holder.mTvMsg.setText("【图片】");
        } else if (body instanceof EMTextMessageBody) {
            EMTextMessageBody textMessageBody = (EMTextMessageBody) body;
            String message = textMessageBody.getMessage();
            holder.mTvMsg.setText(message);
        } else {
            holder.mTvMsg.setText("未知消息类型");
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnConversationClickListener != null) {
                    mOnConversationClickListener.onConversationClick(emConversation, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mEMConversationList.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvUsername;
        private final TextView mTvMsg;
        private final TextView mTvTime;
        private final TextView mTvUnread;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvUnread = (TextView) itemView.findViewById(R.id.tv_unread);
        }
    }

    public interface OnConversationClickListener {
        void onConversationClick(EMConversation conversation, int position);
    }

    private OnConversationClickListener mOnConversationClickListener;

    public void setOnConversationClickListener(OnConversationClickListener onConversationClickListener) {
        this.mOnConversationClickListener = onConversationClickListener;
    }
}
