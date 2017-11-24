package com.pheasant.shutterapp.shutter.api.requester;

import com.pheasant.shutterapp.network.request.friends.InviteCreateRequest;
import com.pheasant.shutterapp.network.request.util.Request;
import com.pheasant.shutterapp.network.request.util.RequestResultListener;
import com.pheasant.shutterapp.shutter.api.listeners.InvitesCreateListener;
import com.pheasant.shutterapp.shutter.api.util.InviteDataHolder;

import java.util.LinkedList;

/**
 * Created by Peszi on 2017-11-22.
 */

public class InviteCreateRequester implements RequestResultListener {

    private LinkedList<InviteDataHolder> requestsBuffer;

    private InviteCreateRequest inviteRequest;
    private InvitesCreateListener invitesCreateListener;

    public InviteCreateRequester(String apiKey) {
        this.requestsBuffer = new LinkedList<>();
        this.inviteRequest = new InviteCreateRequest(apiKey);
        this.inviteRequest.setOnRequestResultListener(this);
    }

    public void setListener(InvitesCreateListener invitesCreateListener) {
        this.invitesCreateListener = invitesCreateListener;
    }

    public void sendInvitation(int userId) {
        this.requestsBuffer.add(new InviteDataHolder(userId, true));
        this.checkStack();
    }

    public void removeInvitation(int userId) {
        this.requestsBuffer.add(new InviteDataHolder(userId, false));
        this.checkStack();
    }

    private void checkStack() {
        if (!this.requestsBuffer.isEmpty()) {
            this.inviteRequest.sendRequest(this.requestsBuffer.poll());
            this.inviteRequest.execute();
        }
    }

    @Override
    public void onResult(int resultCode) {
        if (resultCode == Request.RESULT_OK) {
            if (this.invitesCreateListener != null) {
                if (this.inviteRequest.getInviteDataHolder().isNotRemoving())
                    this.invitesCreateListener.onInviteRequestCreate(this.inviteRequest.getInviteDataHolder().getUserId());
                else
                    this.invitesCreateListener.onInviteRequestRemove(this.inviteRequest.getInviteDataHolder().getUserId());
            }
            this.checkStack();
        }
    }
}