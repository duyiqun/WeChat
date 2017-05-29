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
        EMMessage message = EMMessage.createTxtSendMessage(msg, username);
        mEMMessageList.add(message);
        EMClient.getInstance().chatManager().sendMessage(message);//异步方法
        mChatView.onSendMsg(message);
    }

    @Override
    public void receiveMsg(EMMessage emMessage) {
        mEMMessageList.add(emMessage);
    }

    @Override
    public void loadMoreMsg(int pageSize) {
        if (mConversation == null) {
            mChatView.onLoadMore(false, "没有更多消息了", 0);
        } else {
            //判断是否还有更多消息
            int allMsgCount = mConversation.getAllMsgCount();
            //如果当前已经显示出来的消息>=allMsgCount 则代表已经没有更多
            if (mEMMessageList.size() >= allMsgCount) {
                mChatView.onLoadMore(false, "没有更多消息了", 0);
            } else {
                EMMessage message = mEMMessageList.get(0);
                List<EMMessage> loadMoreMsgFromDB = mConversation.loadMoreMsgFromDB(message.getMsgId(), pageSize);
                mEMMessageList.addAll(0, loadMoreMsgFromDB);
                mChatView.onLoadMore(true, "又加载了" + loadMoreMsgFromDB.size() + "条数据", loadMoreMsgFromDB.size());
            }
        }
    }
}
