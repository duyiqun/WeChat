package com.qun.weichat.presenter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.qun.weichat.view.activity.ChatView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qun on 2017/5/26.
 */

public class ChatPresenterImpl implements ChatPresenter {

    private ChatView mChatView;
    private List<EMMessage> mEMMessageList = new ArrayList<>();
    private EMConversation mConversation;

    public ChatPresenterImpl(ChatView chatView) {
        this.mChatView = chatView;
    }

    /**
     * 调用环信SDK，获取最近的pageSize条聊天记录
     *
     * @param username
     * @param pageSize
     */
    @Override
    public void init(String username, int pageSize) {
        mEMMessageList.clear();
        //注意：如果两个好友不曾聊天过，那么返回的是null
        mConversation = EMClient.getInstance().chatManager().getConversation(username);
        if (mConversation != null) {
            //最后的一条聊天记录
            EMMessage lastMessage = mConversation.getLastMessage();//1
            //从数据库中获取最近的pageSize条 //19
            List<EMMessage> emMessageList = mConversation.loadMoreMsgFromDB(lastMessage.getMsgId(), pageSize - 1);
            mEMMessageList.addAll(emMessageList);
            mEMMessageList.add(lastMessage);
        }
        mChatView.onInit(mEMMessageList);
    }

    @Override
    public void sendTextMessage(String msg, String username) {
        /**
         * 1. 将msg添加到mEMMessageList中
         * 2. 调用环信SDK发送消息
         * 3. 通知ChatView更新界面
         */
        EMMessage message = EMMessage.createTxtSendMessage(msg,username);
        mEMMessageList.add(message);
        EMClient.getInstance().chatManager().sendMessage(message);//异步方法
        mChatView.onSendMsg(message);
    }
}
