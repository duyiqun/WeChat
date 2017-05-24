package com.qun.weichat.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qun.weichat.R;
import com.qun.weichat.utils.StringUtils;

import java.util.List;

/**
 * Created by Qun on 2017/5/23.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> implements IContactAdapter {

    private List<String> contactsList;

    public ContactAdapter(List<String> contactsList) {
        this.contactsList = contactsList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_contact, parent, false);
        ContactViewHolder contactViewHolder = new ContactViewHolder(view);
        return contactViewHolder;
    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        String contact = contactsList.get(position);
        holder.mTvUsername.setText(contact);
        String initial = StringUtils.getInitial(contact);
        if (position == 0) {
            holder.mTvSection.setVisibility(View.VISIBLE);
        } else {
            String preContact = contactsList.get(position - 1);
            if (StringUtils.getInitial(preContact).equals(initial)) {
                //不用显示自己的Section
                holder.mTvSection.setVisibility(View.GONE);
            } else {
                holder.mTvSection.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return contactsList == null ? 0 : contactsList.size();
    }

    @Override
    public List<String> getData() {
        return contactsList;
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {

        private final TextView mTvSection;
        private final TextView mTvUsername;

        public ContactViewHolder(View itemView) {
            super(itemView);
            mTvSection = (TextView) itemView.findViewById(R.id.tv_section);
            mTvUsername = (TextView) itemView.findViewById(R.id.tv_username);
        }
    }
}
