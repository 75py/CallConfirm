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

package com.nagopy.android.callconfirm.receiver;

import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneNumberUtils;

import com.nagopy.android.callconfirm.helper.HookState;
import com.nagopy.android.callconfirm.helper.PermissionHelper;
import com.nagopy.android.callconfirm.receiver.helper.Navigator;

import javax.inject.Inject;

import timber.log.Timber;

public class NewOutgoingCallReceiver extends BaseBroadcastReceiver {

    @Inject
    PermissionHelper permissionHelper;

    @Inject
    HookState hookState;

    @Inject
    Navigator navigator;

    @Override
    public void onReceive(Context context, Intent intent) {
        getComponent().inject(this);

        Timber.d("Received. action=%s, flags=%d, extra=%s", intent.getAction(), intent.getFlags(), intent.getExtras());

        if (permissionHelper.areGrantedPermissions()) {
            if (hookState.isHookEnabled()) {
                String phoneNumber = getResultData();
                if (phoneNumber != null
                        && !phoneNumber.isEmpty()
                        && !PhoneNumberUtils.isEmergencyNumber(phoneNumber)) {
                    setResultData(null);
                    navigator.startConfirmActivity(phoneNumber);
                }
            } else {
                // hook disabled
                // reset hook status to enabled
                hookState.setHookEnabled(true);
            }
        }
    }

}
