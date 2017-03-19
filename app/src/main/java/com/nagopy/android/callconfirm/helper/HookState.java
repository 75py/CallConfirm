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

package com.nagopy.android.callconfirm.helper;

import android.content.SharedPreferences;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class HookState {

    @Inject
    SharedPreferences sp;

    static final String KEY_HOOK_ENABLED = "HOOK_ENABLED";

    @Inject
    HookState() {
    }


    public boolean isHookEnabled() {
        return sp.getBoolean(KEY_HOOK_ENABLED, true);
    }

    public void setHookEnabled(boolean enabled) {
        sp.edit().putBoolean(KEY_HOOK_ENABLED, enabled).apply();
    }
}
