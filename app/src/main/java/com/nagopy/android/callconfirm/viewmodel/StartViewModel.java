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

package com.nagopy.android.callconfirm.viewmodel;

import android.databinding.ObservableBoolean;
import android.view.View;

import com.nagopy.android.callconfirm.di.ActivityScope;
import com.nagopy.android.callconfirm.helper.PermissionHelper;
import com.nagopy.android.callconfirm.view.helper.Navigator;

import javax.inject.Inject;

@ActivityScope
public class StartViewModel {

    @Inject
    Navigator navigator;

    @Inject
    PermissionHelper permissionHelper;

    public ObservableBoolean arePermissionsGranted = new ObservableBoolean();

    @Inject
    StartViewModel() {
    }

    public void updateGrantedPermissions() {
        arePermissionsGranted.set(permissionHelper.areGrantedPermissions());
    }

    public final View.OnClickListener startAppSetting = (View v) -> navigator.startAppSetting();

    public final View.OnClickListener startLicenseActivity = (View v) -> navigator.startLicenseActivity();

    public final View.OnClickListener openGitHub = (View v) -> navigator.openGitHub();
}
