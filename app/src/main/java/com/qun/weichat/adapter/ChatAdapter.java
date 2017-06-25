package com.qun.weichat.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.DensityUtil;
import com.qun.weichat.R;
import com.qun.weichat.utils.ToastUtil;
import com.qun.weichat.widget.ImageProgressBar;

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
    public static final int MAX_WIDTH = 200;
    public static final int MIN_WIDTH = 100;
    public static final int MAX_HEIGHT = 300;
    public static final int MIN_HEIGHT = 150;
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
            switch (status) {
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
        } else if (emMessage.direct() == EMMessage.Direct.RECEIVE && emMessage.getType() == EMMessage.Type.IMAGE) {
            //接收的图片
            EMMessageBody imageBody = emMessage.getBody();
            if (imageBody instanceof EMImageMessageBody) {
                EMImageMessageBody imageMessageBody = (EMImageMessageBody) imageBody;
                String remoteUrl = imageMessageBody.getRemoteUrl();
                String fileName = imageMessageBody.getFileName();
                //缩略图的地址(网络)
//                String thumbnailUrl = imageMessageBody.getThumbnailUrl();
                //缩略图本地地址(显示并不清晰)
                String thumbnailLocalPath = imageMessageBody.thumbnailLocalPath();
                Log.d(TAG, "onBindViewHolder: thumbnailLocalPath=" + thumbnailLocalPath + "/remoteUrl=" + remoteUrl + "/fileName=" + fileName);
                loadPictureWithGlide(holder, thumbnailLocalPath);
            }
            //            holder.mIvImage
        } else if (emMessage.direct() == EMMessage.Direct.SEND && emMessage.getType() == EMMessage.Type.IMAGE) {
            //发送的图片
            EMMessageBody imageBody = emMessage.getBody();
            if (imageBody instanceof EMImageMessageBody) {
                EMImageMessageBody imageMessageBody = (EMImageMessageBody) imageBody;
                String remoteUrl = imageMessageBody.getRemoteUrl();
                String fileName = imageMessageBody.getFileName();
                String localUrl = imageMessageBody.getLocalUrl();
                //缩略图的地址
                String thumbnailUrl = imageMessageBody.getThumbnailUrl();
                Log.d(TAG, "onBindViewHolder: thumbnailUrl=" + thumbnailUrl + "/remoteUrl=" + remoteUrl + "/fileName=" + fileName + "/localUrl=" + localUrl);
                loadPictureWithGlide(holder, localUrl);
            }
            //监听图片发送的状态
            emMessage.setMessageStatusCallback(new CallBack() {
                @Override
                public void onMainSuccess() {
                    holder.mIpbImage.setVisibility(View.GONE);
                    Log.d(TAG, "onMainSuccess: ");
                    ToastUtil.showMsg(mContext, "上传成功");
                }

                @Override
                public void onMainError(int code, String msg) {
                    holder.mIpbImage.setProgress(-1);
                    holder.mIpbImage.setVisibility(View.VISIBLE);
                    ToastUtil.showMsg(mContext, "上传失败：" + msg);
                    Log.d(TAG, "onMainError: " + msg);
                }

                @Override
                public void onMainProgress(int progress, String msg) {
                    holder.mIpbImage.setProgress(progress);
                    holder.mIpbImage.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onProgress: " + progress);
                }
            });

            //处理ImageProgressBar
            switch (emMessage.status()) {
                case CREATE:
                case INPROGRESS:
                    holder.mIpbImage.setVisibility(View.VISIBLE);
                case SUCCESS:
                    holder.mIpbImage.setVisibility(View.GONE);
                    break;
                case FAIL:
                    holder.mIpbImage.setProgress(-1);
                    holder.mIpbImage.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
        }
    }

    private void loadPictureWithGlide(final ChatViewHolder holder, String imagePath) {
        Glide.with(mContext).load(imagePath).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                //需求：ImageView最宽不能超过200dp，最窄不能低于100dp，最高不能超过300dp，最低不能低于150dp
                //如果是宽图，将宽度限制，高度根据比例计算
                //如果是长图，将高度限制，宽度根据比例计算
                int width = resource.getWidth();
                int height = resource.getHeight();
                int maxWidth = DensityUtil.dip2px(mContext, MAX_WIDTH);
                int minWidth = DensityUtil.dip2px(mContext, MIN_WIDTH);
                int maxHeight = DensityUtil.dip2px(mContext, MAX_HEIGHT);
                int minHeight = DensityUtil.dip2px(mContext, MIN_HEIGHT);

                int realWidth = width;
                int realHeight = height;
                if (width / height >= 1) {
                    //宽图
                    if (width > maxWidth) {
                        realWidth = maxWidth;
                        realHeight = maxWidth * height / width;
                    } else if (width < minWidth) {
                        realWidth = minWidth;
                        realHeight = minWidth * height / width;
                    }
                } else {
                    //高图
                    if (height > maxHeight) {
                        realHeight = maxHeight;
                        realWidth = maxWidth * height / width;
                    } else if (height < minHeight) {
                        realHeight = minHeight;
                        realWidth = minWidth * height / width;
                    }
                }

                ViewGroup.LayoutParams layoutParams = holder.mIvImage.getLayoutParams();
                layoutParams.width = realWidth;
                layoutParams.height = realHeight;
                holder.mIvImage.setLayoutParams(layoutParams);

                if (holder.mIpbImage != null) {
                    holder.mIpbImage.setLayoutParams(layoutParams);
                }
                Log.d(TAG, "onResourceReady: " + resource);
                holder.mIvImage.setImageBitmap(resource);
            }
        });
    }

    private void showFrameAnimation(ImageView imageState) {
        imageState.setVisibility(View.VISIBLE);
        imageState.setImageResource(R.drawable.send_text_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) imageState.getDrawable();
        if (animationDrawable.isRunning()) {
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
        private final ImageProgressBar mIpbImage;

        public ChatViewHolder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
            mTvMsg = (TextView) itemView.findViewById(R.id.tv_msg);
            mIvImage = (ImageView) itemView.findViewById(R.id.iv_image);
            mIvState = (ImageView) itemView.findViewById(R.id.iv_state);
            mIpbImage = (ImageProgressBar) itemView.findViewById(R.id.ipb_image);
        }
    }
}
