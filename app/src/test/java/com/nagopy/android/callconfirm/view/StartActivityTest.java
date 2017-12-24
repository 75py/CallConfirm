/*
 * Copyright 2017 75py
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nagopy.android.callconfirm.view;

import android.Manifest;
import android.content.pm.PackageManager;

import com.nagopy.android.callconfirm.BuildConfig;
import com.nagopy.android.callconfirm.R;
import com.nagopy.android.callconfirm.viewmodel.StartViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class StartActivityTest {

    @Test
    public void test() throws Exception {
        ActivityController<StartActivity> controller = Robolectric.buildActivity(StartActivity.class);
        StartActivity activity = controller.create().get();

        assertThat(activity.navigator).isNotNull();
        assertThat(activity.viewModel).isNotNull();
        assertThat(activity.binding).isNotNull();

        assertThat(activity.binding.appName.getText()).isEqualTo(RuntimeEnvironment.application.getText(R.string.app_name));
        //assertThat(activity.binding.version.getText()).isEqualTo(RuntimeEnvironment.application.getString(R.string.version, BuildConfig.VERSION_NAME));
    }

    @Test
    public void startPermissionSettingsIfNeeded() throws Exception {
        ActivityController<StartActivity> controller = Robolectric.buildActivity(StartActivity.class);
        StartActivity activity = controller.create().start().resume().get();

        activity.startPermissionSettingsIfNeeded();
        // 落ちなければOK
    }

    @Test
    public void onRequestPermissionsResult() throws Exception {
        ActivityController<StartActivity> controller = Robolectric.buildActivity(StartActivity.class);
        StartActivity activity = controller.create().start().resume().get();

        activity.viewModel = mock(StartViewModel.class);
        activity.onRequestPermissionsResult(0
                , new String[]{Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS}
                , new int[]{PackageManager.PERMISSION_GRANTED, PackageManager.PERMISSION_GRANTED});
        verify(activity.viewModel, times(1)).updateGrantedPermissions();
    }

    @Test
    public void onPermissionDenied() throws Exception {
        ActivityController<StartActivity> controller = Robolectric.buildActivity(StartActivity.class);
        StartActivity activity = controller.create().start().resume().get();

        activity.onPermissionDenied();

        // とりあえず落ちなければOK
        // AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        // assertThat(dialog).isNotNull();
    }

}