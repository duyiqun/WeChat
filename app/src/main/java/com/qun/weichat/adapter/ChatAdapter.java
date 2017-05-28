package com.qun.weichat.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMMessage;
import com.qun.weichat.R;

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
    public void onBindViewHolder(ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mEMMessageList == null ? 0 : mEMMessageList.size();
    }

    class ChatViewHolder extends RecyclerView.ViewHolder {

        public ChatViewHolder(View itemView) {
            super(itemView);
        }
    }
}
