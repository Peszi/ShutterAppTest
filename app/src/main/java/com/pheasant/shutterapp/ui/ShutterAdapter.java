package com.pheasant.shutterapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.pheasant.shutterapp.R;
import com.pheasant.shutterapp.api.ShutterDataManager;
import com.pheasant.shutterapp.ui.features.ManageFragment;
import com.pheasant.shutterapp.ui.features.BrowseFragment;
import com.pheasant.shutterapp.ui.features.CameraFragment;
import com.pheasant.shutterapp.ui.shared.LockingViewPager;
import com.pheasant.shutterapp.ui.shared.NotifiableFragment;
import com.pheasant.shutterapp.util.IntentKey;

/**
 * Created by Peszi on 2017-04-24.
 */

public class ShutterAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private final int FRAGMENTS_COUNT = 3;

    private NotifiableFragment[] shutterFragments;

    private LockingViewPager viewPager;
    private ShutterInterface shutterInterface;

    private ShutterDataManager shutterDataController;

    public ShutterAdapter(FragmentManager fragmentManager, Context context, View view, Bundle bundle, ShutterInterface shutterInterface) {
        super(fragmentManager);
        this.shutterInterface = shutterInterface;
        this.shutterDataController = new ShutterDataManager(context, bundle.getString(IntentKey.USER_API_KEY));
        this.shutterDataController.downloadFriends();
        this.shutterDataController.downloadInvites();
        this.shutterDataController.downloadPhotos();
        this.setupViewPager(view);
        this.setupFragments(bundle);
    }

    public void setupViewPager(View view) {
        this.viewPager = (LockingViewPager) view.findViewById(R.id.shutter_view_pager);
        this.viewPager.setOffscreenPageLimit(this.FRAGMENTS_COUNT);
        this.viewPager.setEnabled(true);
        this.viewPager.setAdapter(this);
        this.viewPager.addOnPageChangeListener(this);
    }

    public void setupFragments(Bundle bundle) {
        this.shutterFragments = new NotifiableFragment[this.FRAGMENTS_COUNT];
        // Camera
        final CameraFragment cameraFragment = new CameraFragment();
        cameraFragment.setShutterDataManager(this.shutterDataController);
        cameraFragment.setPagerInterface(this.viewPager);
        this.shutterFragments[0] = cameraFragment;
        // Browse
        final BrowseFragment browseFragment = new BrowseFragment();
        browseFragment.setShutterDataManager(this.shutterDataController);
        this.shutterFragments[1] = browseFragment;
        // Manage
        final ManageFragment manageFragment = new ManageFragment(bundle.getString(IntentKey.USER_API_KEY));
        manageFragment.setShutterDataManager(this.shutterDataController);
        this.shutterFragments[2] = manageFragment;
    }

    @Override
    public Fragment getItem(int position) {
        return this.shutterFragments[position];
    }

    @Override
    public int getCount() {
        return this.FRAGMENTS_COUNT;
    }

    // Back button actions
    public void onBack() {
        if (this.canGoBack())
            if (!this.canSwitchToPrevFragment())
                this.shutterInterface.logoutUser();
    }

    private boolean canGoBack() {
        return this.shutterFragments[this.viewPager.getCurrentItem()].onBack();
    }

    // Switching fragment to the first one
    private boolean canSwitchToPrevFragment() {
        if (this.viewPager.getCurrentItem() > 0) {
            this.viewPager.setCurrentItem(this.viewPager.getCurrentItem() - 1, true);
            return true;
        }
        return false;
    }

    // On visible fragment change
    @Override
    public void onPageSelected(int position) {
        this.shutterFragments[position].onShow();
    }

    // Not used
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    // Not used
    @Override
    public void onPageScrollStateChanged(int state) {

    }

}