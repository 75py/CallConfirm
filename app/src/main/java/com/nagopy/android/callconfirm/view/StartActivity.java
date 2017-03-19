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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.nagopy.android.callconfirm.R;
import com.nagopy.android.callconfirm.databinding.ActivityStartBinding;
import com.nagopy.android.callconfirm.view.helper.Navigator;
import com.nagopy.android.callconfirm.viewmodel.StartViewModel;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import timber.log.Timber;

@RuntimePermissions
public class StartActivity extends BaseActivity {

    @Inject
    StartViewModel viewModel;

    @Inject
    Navigator navigator;

    ActivityStartBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_start);
        binding.setViewModel(viewModel);

        StartActivityPermissionsDispatcher.startPermissionSettingsIfNeededWithCheck(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.updateGrantedPermissions();
    }

    @NeedsPermission({Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS})
    void startPermissionSettingsIfNeeded() {
        // do nothing
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        StartActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
        viewModel.updateGrantedPermissions();
    }

    @OnPermissionDenied({Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS})
    @OnNeverAskAgain({Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS})
    void onPermissionDenied() {
        Timber.d("@OnNeverAskAgain");
        new AlertDialog.Builder(this)
                .setTitle(R.string.need_permission)
                .setMessage(R.string.msg_need_permission)
                .setPositiveButton(R.string.app_setting, (dialogInterface, i) -> navigator.startAppSetting())
                .setNegativeButton(R.string.close, (dialogInterface, i) -> {
                    // do nothing
                    //finish()
                })
                .show();
    }

}
