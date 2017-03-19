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

package com.nagopy.android.callconfirm.view.helper;

import android.content.Intent;
import android.provider.Settings;

import com.nagopy.android.callconfirm.BuildConfig;
import com.nagopy.android.callconfirm.view.LicenseActivity;
import com.nagopy.android.callconfirm.view.StartActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigatorTest {

    private Navigator navigator;

    @Before
    public void setUp() throws Exception {
        navigator = new Navigator();
        navigator.activity = Robolectric.setupActivity(StartActivity.class);
    }

    @Test
    public void startAppSetting() throws Exception {
        navigator.startAppSetting();
        Intent nextIntent = Shadows.shadowOf(navigator.activity).getNextStartedActivity();
        assertThat(nextIntent).isNotNull();
        assertThat(nextIntent.getAction()).isEqualTo(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        assertThat(nextIntent.getData()).isNotNull();
        assertThat(nextIntent.getData().getScheme()).isEqualTo("package");
        assertThat(nextIntent.getData().getEncodedSchemeSpecificPart()).isEqualTo(BuildConfig.APPLICATION_ID);
        assertThat(nextIntent.getData().getEncodedFragment()).isNull();
    }

    @Test
    public void startLicenseActivity() throws Exception {
        navigator.startLicenseActivity();
        Intent nextIntent = Shadows.shadowOf(navigator.activity).getNextStartedActivity();
        assertThat(nextIntent).isNotNull();
        assertThat(nextIntent.getComponent().getClassName()).isEqualTo(LicenseActivity.class.getName());
    }

    @Test
    public void openGitHub() throws Exception {
        navigator.openGitHub();
        Intent nextIntent = Shadows.shadowOf(navigator.activity).getNextStartedActivity();
        assertThat(nextIntent).isNotNull();
        assertThat(nextIntent.getData()).isNotNull();
        assertThat(nextIntent.getDataString()).isEqualTo("https://github.com/75py/CallConfirm");
    }

    @Test
    public void call() throws Exception {
        navigator.activity = spy(navigator.activity);
        doReturn(PERMISSION_GRANTED).when(navigator.activity).checkPermission(anyString(), anyInt(), anyInt());
        doCallRealMethod().when(navigator.activity).startActivity(any());

        navigator.call("1234567890");

        Intent nextIntent = Shadows.shadowOf(navigator.activity).getNextStartedActivity();
        assertThat(nextIntent).isNotNull();
        assertThat(nextIntent.getAction()).isEqualTo(Intent.ACTION_CALL);
        assertThat(nextIntent.getData()).isNotNull();
        assertThat(nextIntent.getDataString()).isEqualTo("tel:1234567890");
    }

    @Test(expected = RuntimeException.class)
    public void call_noPermissions() throws Exception {
        navigator.activity = spy(navigator.activity);
        doReturn(PERMISSION_DENIED).when(navigator.activity).checkPermission(anyString(), anyInt(), anyInt());
        doCallRealMethod().when(navigator.activity).startActivity(any());

        navigator.call("1234567890");
    }

}