package com.pheasant.shutterapp.shutter.ui.features.manage.object;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pheasant.shutterapp.R;
import com.pheasant.shutterapp.network.request.data.StrangerData;
import com.pheasant.shutterapp.shutter.ui.shared.InviteTmpButton;
import com.pheasant.shutterapp.utils.Util;

/**
 * Created by Peszi on 2017-11-20.
 */

public class StrangerObject implements InviteTmpButton.InviteButtonListener {

    private StrangerData strangerData;

    private StrangerInviteListener objectListener;

    public StrangerObject(StrangerData strangerData) {
        this.strangerData = strangerData;
    }

    public void setupView(View view) {
        if (this.strangerData != null) {
            final ImageView avatar = (ImageView) view.getTag(R.id.friend_avatar);
            final TextView name = (TextView) view.getTag(R.id.friend_name);
            final InviteTmpButton inviteBtn = (InviteTmpButton) view.getTag(R.id.friend_invite_btn);
            avatar.setImageResource(R.drawable.avatar_default);
            name.setText(this.strangerData.getName());
            inviteBtn.setState(this.strangerData.getInvite());
            inviteBtn.setListener(this);
        }
    }

    public void setObjectListener(StrangerInviteListener objectListener) {
        this.objectListener = objectListener;
    }

    public static View getView(Context context, LayoutInflater layoutInflater, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            view = layoutInflater.inflate(R.layout.layout_stranger, parent, false);
            view.setTag(R.id.friend_avatar, view.findViewById(R.id.friend_avatar));
            view.setTag(R.id.friend_name, view.findViewById(R.id.friend_name));
            view.setTag(R.id.friend_invite_btn, view.findViewById(R.id.friend_invite_btn));
        }
        Util.setupFont(context, view, Util.FONT_PATH_LIGHT);
        return view;
    }

    @Override
    public void onInvite() {
        if (this.objectListener != null)
            this.objectListener.onInviteEvent(this.strangerData.getId());
    }

    @Override
    public void onUndo() {
        if (this.objectListener != null)
            this.objectListener.onInviteDeleteEvent(this.strangerData.getId());
    }

    public interface StrangerInviteListener {
        void onInviteEvent(int userId);
        void onInviteDeleteEvent(int userId);
    }
}
