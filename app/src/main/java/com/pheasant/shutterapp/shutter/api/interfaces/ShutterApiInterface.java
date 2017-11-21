package com.pheasant.shutterapp.shutter.api.interfaces;

import com.pheasant.shutterapp.network.request.data.FriendData;
import com.pheasant.shutterapp.network.request.data.UserData;
import com.pheasant.shutterapp.shutter.api.listeners.FriendsListListener;
import com.pheasant.shutterapp.shutter.api.listeners.InvitesListListener;
import com.pheasant.shutterapp.shutter.api.listeners.SearchListListener;

import java.util.ArrayList;

/**
 * Created by Peszi on 2017-11-06.
 */

public interface ShutterApiInterface {
    void setSearchListListener(SearchListListener listListener);
    void setInvitesListListener(InvitesListListener listListener);

    void registerFriendsListListener(FriendsListListener listListener);

    void downloadFriends();
    void downloadInvites();
    void searchUsers(String keyword);

    ArrayList<FriendData> getFriends();
    ArrayList<UserData> getInvites();
}
