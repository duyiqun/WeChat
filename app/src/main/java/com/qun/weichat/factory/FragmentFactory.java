package com.qun.weichat.factory;

import com.qun.weichat.view.fragment.BaseFragment;
import com.qun.weichat.view.fragment.ContactFragment;
import com.qun.weichat.view.fragment.ConversationFragment;
import com.qun.weichat.view.fragment.PluginFragment;

/**
 * Created by Qun on 2017/5/22.
 */

public class FragmentFactory {

    private static ConversationFragment sConversationFragment;
    private static ContactFragment sContactFragment;
    private static PluginFragment sPluginFragment;

    public static BaseFragment getFragment(int index) {

        switch (index) {
            case 0:
                if (sConversationFragment == null) {
                    sConversationFragment = new ConversationFragment();
                }
                return sConversationFragment;
            case 1:
                if (sContactFragment == null) {
                    sContactFragment = new ContactFragment();
                }
                return sContactFragment;
            case 2:
                if (sPluginFragment == null) {
                    sPluginFragment = new PluginFragment();
                }
                return sPluginFragment;
        }
        return null;
    }
}
