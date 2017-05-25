package com.qun.weichat.event;

/**
 * Created by Qun on 2017/5/25.
 */

public class ContactUpdateEvent {

    public String username;
    public boolean isAdd;

    public ContactUpdateEvent() {
    }

    public ContactUpdateEvent(String username, boolean isAdd) {
        this.username = username;
        this.isAdd = isAdd;
    }

    @Override
    public String toString() {
        return "ContactUpdateEvent{" + "username='" + username + '\'' + ", isAdd=" + isAdd + '}';
    }
}
