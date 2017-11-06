package com.pheasant.shutterapp.utils;

import com.pheasant.shutterapp.network.request.data.UserData;

import java.util.List;

/**
 * Created by Peszi on 2017-05-20.
 */

public interface DataListener {
    UserData getUserData();
    List<UserData> getFriendsData();
}