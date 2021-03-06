package com.pheasant.shutterapp.api.container;

import com.pheasant.shutterapp.api.data.FriendData;
import com.pheasant.shutterapp.api.request.FriendsListRequest;
import com.pheasant.shutterapp.api.listeners.RequestResultListener;
import com.pheasant.shutterapp.api.listeners.FriendsListListener;
import com.pheasant.shutterapp.api.util.Request;

import java.util.ArrayList;

/**
 * Created by Peszi on 2017-11-06.
 */

public class FriendsListContainer implements RequestResultListener {

    private ArrayList<FriendData> friendsList;

    private FriendsListRequest friendsRequest;
    private ArrayList<FriendsListListener> friendsListeners;

    public FriendsListContainer(String apiKey) {
        this.friendsList = new ArrayList<>();
        this.friendsListeners = new ArrayList<>();
        this.friendsRequest = new FriendsListRequest(apiKey);
        this.friendsRequest.setRequestListener(this);
    }

    public void registerFriendsListener(FriendsListListener friendsListener) {
        this.friendsListeners.add(friendsListener);
    }

    public void downloadFriendsList() {
        this.friendsRequest.sendRequest();
    }

    @Override
    public void onRequestResult(int resultCode) {
        if (resultCode == Request.RESULT_OK) {
            final int changesCount = this.friendsRequest.getFriendsList().size() - this.friendsList.size();
            this.friendsList.clear();
            this.friendsList.addAll(this.friendsRequest.getFriendsList());
            this.notifyListeners(changesCount);
        }
    }

//    @Override
//    public void onFriendsListDownloaded(ArrayList<FriendData> newFriendsList) {
//        ArrayList<FriendData> addedFriendsList = new ArrayList<>();
//        for (FriendData newFriend : newFriendsList) {
//            if (!this.updateFriend(newFriend)) {// if users is NOT in our list
//                this.friendsList.add(newFriend);
//                addedFriendsList.add(newFriend);
//            }
//        }
//        this.notifyListeners(addedFriendsList);
//    }

    // Updating friend data if user already exist
    private boolean updateFriend(FriendData updatedFriend) {
        for (FriendData friend : this.friendsList)
            if (friend.getId() == updatedFriend.getId()) {
                friend.update(updatedFriend);
                return true;
            }
        return false;
    }

    private void notifyListeners(int changesCount) {
        for (FriendsListListener friendsListener : this.friendsListeners)
            friendsListener.onFriendsListDownloaded(changesCount);
    }

    public ArrayList<FriendData> getFriendsList() {
        return this.friendsList;
    }

}
