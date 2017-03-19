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

package com.nagopy.android.callconfirm.receiver.helper;

import android.content.Intent;

import com.nagopy.android.callconfirm.BuildConfig;
import com.nagopy.android.callconfirm.view.ConfirmActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class NavigatorTest {

    private Navigator navigator;

    @Before
    public void setUp() throws Exception {
        navigator = new Navigator();
        navigator.context = RuntimeEnvironment.application;
    }

    @Test
    public void startConfirmActivity() throws Exception {
        navigator.startConfirmActivity("1234567890");
        Intent nextIntent = Shadows.shadowOf(navigator.context).getNextStartedActivity();
        assertThat(nextIntent).isNotNull();
        assertThat(nextIntent.getComponent().getClassName()).isEqualTo(ConfirmActivity.class.getName());
        assertThat(nextIntent.getFlags()).isEqualTo(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        assertThat(nextIntent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)).isEqualTo("1234567890");
    }

}