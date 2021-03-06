package com.pheasant.shutterapp.api.container;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import com.pheasant.shutterapp.api.data.UploadPhotoData;
import com.pheasant.shutterapp.api.listeners.PhotoUploadListener;
import com.pheasant.shutterapp.api.requester.PhotoUploadRequest;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Peszi on 2017-11-27.
 */

public class PhotoUploadContainer implements Runnable, Handler.Callback {

    private LinkedList<UploadPhotoData> photoBuffer;
    private boolean isSuccess;

    private PhotoUploadRequest uploadRequest;

    private Handler statusHandler;
    private PhotoUploadListener uploadListener;

    public PhotoUploadContainer(String apiKey) {
        this.photoBuffer = new LinkedList<>();
        this.uploadRequest = new PhotoUploadRequest(apiKey);
        this.statusHandler = new Handler(this);
        new Thread(this).start();
    }

    public void setUploadListener(PhotoUploadListener uploadListener) {
        this.uploadListener = uploadListener;
    }

    public void uploadPhoto(Bitmap bitmap, List<Integer> recipientsList) {
        synchronized (this.photoBuffer) {
            this.photoBuffer.add(new UploadPhotoData(bitmap, recipientsList));
            this.photoBuffer.notify();
        }
    }

    public void reUpload() {
        synchronized (this.photoBuffer) {
            this.photoBuffer.notify();
        }
    }

    @Override
    public void run() {
        UploadPhotoData uploadPhotoData;
        boolean isFailed = false;
        while (true) {
            synchronized (this.photoBuffer) {
                if (this.photoBuffer.isEmpty() || isFailed) {
                    try {
                        this.photoBuffer.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                uploadPhotoData = this.photoBuffer.poll();
            }
            this.uploadRequest.setData(uploadPhotoData.getUserPhotoData(), uploadPhotoData.getRecipientsList());
            isFailed = !this.uploadRequest.upload();
            if (isFailed) {
                synchronized (this.photoBuffer) {
                    this.photoBuffer.add(uploadPhotoData);
                }
            }
            this.notifyListeners(!isFailed);
        }
    }

    private void notifyListeners(boolean success) {
        this.isSuccess = success; // TODO should not work sometimes
        this.statusHandler.sendEmptyMessage(0);
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (this.uploadListener != null)
            this.uploadListener.onPhotoUploadStatusChange(this.isSuccess);
        return false;
    }
}
