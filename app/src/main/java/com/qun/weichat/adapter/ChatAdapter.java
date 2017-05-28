package com.qun.weichat.adapter;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.qun.weichat.R;

import java.util.Date;
import java.util.List;

/**
 * Created by Qun on 2017/5/26.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private static final String TAG = "ChatAdapter";
    private static final int RECEIVE_TEXT = 0;
    private static final int RECEIVE_IMAGE = 1;
    private static final int RECEIVE_UNKNOWN = 2;
    private static final int SEND_TEXT = 3;
    private static final int SEND_IMAGE = 4;
    private static final int SEND_UNKNOWN = 5;
    private List<EMMessage> mEMMessageList;
    private Context mContext;

    public ChatAdapter(List<EMMessage> EMMessageList, Context context) {
        mEMMessageList = EMMessageList;
        this.mContext = context;
    }

    /**
     * 根据position获取对象，然后判断这个对象的类型，不同的类型返回不同的值即可
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        EMMessage emMessage = mEMMessageList.get(position);
        EMMessage.Type type = emMessage.getType();
        switch (emMessage.direct()) {
            case RECEIVE:
                if (type == EMMessage.Type.TXT) {
                    return RECEIVE_TEXT;
                } else if (type == EMMessage.Type.IMAGE) {
                    return RECEIVE_IMAGE;
                } else {
                    //目前不支持的类型
                    return RECEIVE_UNKNOWN;
                }
            case SEND:
                if (type == EMMessage.Type.TXT) {
                    return SEND_TEXT;
                } else if (type == EMMessage.Type.IMAGE) {
                    return SEND_IMAGE;
                } else {
                    //目前不支持的类型
                    return SEND_UNKNOWN;
                }
        }
        return super.getItemViewType(position);
    }

    @Override
    public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resId = R.layout.list_item_receive_text;
        switch (viewType) {
            case RECEIVE_TEXT:
            case RECEIVE_UNKNOWN:
                resId = R.layout.list_item_receive_text;
                break;
            case RECEIVE_IMAGE:
                resId = R.layout.list_item_receive_image;
                break;
            case SEND_TEXT:
            case SEND_UNKNOWN:
                resId = R.layout.list_item_send_text;
                break;
            case SEND_IMAGE:
                resId = R.layout.list_item_send_image;
                break;
            default:
                break;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(resId, parent, false);
        ChatViewHolder chatViewHolder = new ChatViewHolder(view);
        return chatViewHolder;
    }

    @Override
    public void onBindViewHolder(final ChatViewHolder holder, int position) {
        EMMessage emMessage = mEMMessageList.get(position);

        //显示时间
        long msgTime = emMessage.getMsgTime();
        holder.mTvTime.setText(DateUtils.getTimestampString(new Date(msgTime)));
        if (position == 0) {
            holder.mTvTime.setVisibility(View.VISIBLE);
        } else {
            EMMessage preMessage = mEMMessageList.get(position - 1);
            long preTime = preMessage.getMsgTime();
            if (DateUtils.isCloseEnough(msgTime, preTime)) {
                holder.mTvTime.setVisibility(View.GONE);
            } else {
                holder.mTvTime.setVisibility(View.VISIBLE);
            }
        }

        //处理发送和接收的文字
        if (emMessage.direct() == EMMessage.Direct.RECEIVE && emMessage.getType() == EMMessage.Type.TXT) {
            //接收的文本
            EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
            String message = messageBody.getMessage();
            holder.mTvMsg.setText(message);
        } else if (emMessage.direct() == EMMessage.Direct.SEND && emMessage.getType() == EMMessage.Type.TXT) {
            //发送的文本
            EMTextMessageBody messageBody = (EMTextMessageBody) emMessage.getBody();
            String message = messageBody.getMessage();
            holder.mTvMsg.setText(message);

            //监听消息的状态
            emMessage.setMessageStatusCallback(new CallBack() {
                @Override
                public void onMainSuccess() {
                    holder.mIvState.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onMainError(int code, String msg) {
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.mipmap.msg_error);
                }

                @Override
                public void onMainProgress(int code, String msg) {

                }
            });
            //设置mIvState的状态
            EMMessage.Status status = emMessage.status();
            switch (status){
                case CREATE:
                case INPROGRESS:
                    showFrameAnimation(holder.mIvState);
                    break;
                case SUCCESS:
                    holder.mIvState.setVisibility(View.INVISIBLE);
                    break;
                case FAIL:
                    holder.mIvState.setVisibility(View.VISIBLE);
                    holder.mIvState.setImageResource(R.mipmap.msg_error);
                    break;
            }
        }
    }

    private void showFrameAnimation(ImageView imageState) {
        imageState.setVisibility(View.VISIBLE);
        imageState.setImageResource(R.drawable.send_text_animation);
        AnimationDrawable  animationDrawable = (AnimationDrawable) imageState.getDrawable();
        if (animationDrawable.isRunning()){
            animationDrawable.stop();
        }
        animationDrawable.start();
    }

    @Override
    public int getItemCount() {
        return mEMMessageList == null ? 0 : mEMMessageList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvTime;
        private final TextView mTvMsg;
        private final ImageView mIvImage;
        private final ImageView mIvState;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mIvImage = (ImageView) itemView.findViewById(R.id.iv_image);
            mIvState = (ImageView) itemView.findViewById(R.id.iv_state);
        }
    }
}
