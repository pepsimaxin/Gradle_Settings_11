/*
 * Copyright (C) 2018 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.homepage;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.settings.SettingsEnums;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toolbar;

import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.settings.DisplaySettings;
import com.android.settings.MasterClear;
import com.android.settings.R;
import com.android.settings.accounts.AvatarViewMixin;
import com.android.settings.applications.AppAndNotificationDashboardFragment;
import com.android.settings.core.HideNonSystemOverlayMixin;
import com.android.settings.deviceinfo.StorageSettings;
import com.android.settings.deviceinfo.aboutphone.MyDeviceInfoFragment;
import com.android.settings.homepage.contextualcards.ContextualCardsFragment;
import com.android.settings.notification.SoundSettings;
import com.android.settings.overlay.FeatureFactory;
import com.android.settings.system.ResetDashboardFragment;
import com.boring.settings.MenuFragment;

public class SettingsHomepageActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.boring_settings_split_container);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.menu_container, new MenuFragment())
                    .commit();
        }

        onMenuItemSelected(R.id.menu_display);

//        setContentView(R.layout.settings_homepage_container);

//        final View root = findViewById(R.id.settings_homepage_container);
//        root.setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//
//        setHomepageContainerPaddingTop();
//
//        final Toolbar toolbar = findViewById(R.id.search_action_bar);
//        FeatureFactory.getFactory(this).getSearchFeatureProvider()
//                .initSearchToolbar(this /* activity */, toolbar, SettingsEnums.SETTINGS_HOMEPAGE);
//
//        final ImageView avatarView = findViewById(R.id.account_avatar);
//        getLifecycle().addObserver(new AvatarViewMixin(this, avatarView));
//        getLifecycle().addObserver(new HideNonSystemOverlayMixin(this));
//
//        if (!getSystemService(ActivityManager.class).isLowRamDevice()) {
//            // Only allow contextual feature on high ram devices.
//            showFragment(new ContextualCardsFragment(), R.id.contextual_cards_content);
//        }
//        showFragment(new TopLevelSettings(), R.id.main_content);
//        ((FrameLayout) findViewById(R.id.main_content))
//                .getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
    }

    @SuppressLint("NonConstantResourceId")
    public void onMenuItemSelected(int menuId) {
        Fragment fragment = null;
        switch (menuId) {
            case R.id.menu_display:
                fragment = new DisplaySettings();
                break;
            case R.id.menu_about:
                fragment = new MyDeviceInfoFragment();
                break;
            case R.id.menu_notification:
                fragment = new AppAndNotificationDashboardFragment();
                break;
            case R.id.menu_reset:
                fragment = new MasterClear();
                break;
            case R.id.menu_storage:
                fragment = new StorageSettings();
                break;
        }

        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_container, fragment)
                    .commit();
        }
    }

    private void showFragment(Fragment fragment, int id) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment showFragment = fragmentManager.findFragmentById(id);

        if (showFragment == null) {
            fragmentTransaction.add(id, fragment);
        } else {
            fragmentTransaction.show(showFragment);
        }
        fragmentTransaction.commit();
    }

    @VisibleForTesting
    void setHomepageContainerPaddingTop() {
        final View view = this.findViewById(R.id.homepage_container);

        final int searchBarHeight = getResources().getDimensionPixelSize(R.dimen.search_bar_height);
        final int searchBarMargin = getResources().getDimensionPixelSize(R.dimen.search_bar_margin);

        // The top padding is the height of action bar(48dp) + top/bottom margins(16dp)
        final int paddingTop = searchBarHeight + searchBarMargin * 2;
        view.setPadding(0 /* left */, paddingTop, 0 /* right */, 0 /* bottom */);

        // Prevent inner RecyclerView gets focus and invokes scrolling.
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }
}
