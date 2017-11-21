package com.pheasant.shutterapp.shutter.ui.features.manage.friends;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.pheasant.shutterapp.R;
import com.pheasant.shutterapp.network.request.data.FriendData;
import com.pheasant.shutterapp.shutter.ui.features.manage.friends.FriendObject;

import java.util.ArrayList;

/**
 * Created by Peszi on 2017-11-09.
 */

public class FriendsTmpAdapter extends ArrayAdapter<FriendObject> {

    private ArrayList<FriendData> friendsList;

    private LayoutInflater layoutInflater;

    public FriendsTmpAdapter(Context context) {
        super(context, R.layout.layout_recipient);
        this.layoutInflater = LayoutInflater.from(context);
        this.friendsList = new ArrayList<>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View baseView = FriendObject.getView(this.getContext(), this.layoutInflater, convertView, parent);
        this.getItem(position).setupView(baseView);
        return baseView;
    }

    // Filtering list
    public void setFilter(String keyword) {
        if (!keyword.isEmpty()) {
            final ArrayList<FriendData> filteredFriendsList = this.getFriendsListWithKeyword(keyword);
            this.reloadList(filteredFriendsList);
            if (filteredFriendsList.size() == 0)
                this.noFriendsWithKeyword();
        } else {
            this.reloadList(this.friendsList);
        }
    }

    private ArrayList<FriendData> getFriendsListWithKeyword(String keyword) {
        ArrayList<FriendData> filteredFriendsList = new ArrayList<>();
        for (FriendData friend : this.friendsList)
            if (friend.getName().contains(keyword))
                filteredFriendsList.add(friend);
        return filteredFriendsList;
    }

    // Update list (setup)
    // TODO update existing friends (data)
    public void updateList(ArrayList<FriendData> friendsList) {
        this.friendsList = friendsList;
        this.reloadList(this.friendsList);
    }

    // Update view
    public void reloadList(ArrayList<FriendData> updatedFriendsList) {
        this.clear();
        for (FriendData friend : updatedFriendsList)
            this.add(new FriendObject(friend));
        this.notifyDataSetChanged();
    }

    private void noFriendsWithKeyword() {
        // TODO no friends w/ keyword
    }
}