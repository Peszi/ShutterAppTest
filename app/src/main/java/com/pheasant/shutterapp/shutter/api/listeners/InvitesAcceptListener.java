package com.pheasant.shutterapp.shutter.api.listeners;

/**
 * Created by Peszi on 2017-11-23.
 */

public interface InvitesAcceptListener {
    void onInviteRequestAccepted(int userId);
    void onInviteRequestRemoved(int userId);
}
