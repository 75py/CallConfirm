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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.ActivityCompat;

import com.nagopy.android.callconfirm.BuildConfig;
import com.nagopy.android.callconfirm.di.ActivityScope;
import com.nagopy.android.callconfirm.view.LicenseActivity;

import javax.inject.Inject;

@ActivityScope
public class Navigator {

    @Inject
    Activity activity;

    @Inject
    Navigator() {
    }

    public void startAppSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        activity.startActivity(intent);
    }

    public void startLicenseActivity() {
        activity.startActivity(new Intent(activity, LicenseActivity.class));
    }

    public void openGitHub() {
        CustomTabsIntent intent = new CustomTabsIntent.Builder()
                .setShowTitle(true)
                .build();
        intent.launchUrl(activity, Uri.parse("https://github.com/75py/CallConfirm"));
    }

    public void call(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(String.format("tel:%s", Uri.encode(phoneNumber))));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            throw new RuntimeException("Permission denied");
        }
        activity.startActivity(intent);
    }
}
