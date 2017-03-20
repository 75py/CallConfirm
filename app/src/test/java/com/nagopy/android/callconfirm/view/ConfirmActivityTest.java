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

import android.content.Intent;

import com.nagopy.android.callconfirm.BuildConfig;
import com.nagopy.android.callconfirm.R;
import com.nagopy.android.callconfirm.viewmodel.ConfirmViewModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ConfirmActivityTest {

    private Intent intent;
    private ActivityController<ConfirmActivity> controller;

    @Before
    public void setUp() throws Exception {
        intent = new Intent();
        intent.putExtra(Intent.EXTRA_PHONE_NUMBER, "1234567890");
        controller = Robolectric.buildActivity(ConfirmActivity.class, intent);
    }

    @Test
    public void onCreate_phoneNumberEmpty() throws Exception {
        intent.removeExtra(Intent.EXTRA_PHONE_NUMBER);

        try {
            controller.create().get();
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("phoneNumber is empty");
            return; // success
        }
        fail();
    }

    @Test
    public void onDestroy() throws Exception {
        ConfirmActivity activity = controller.create().get();
        activity.viewModel = mock(ConfirmViewModel.class);

        activity = controller.destroy().get();
        verify(activity.viewModel, times(1)).destroy();
    }

    @Test
    public void validateIntent() throws Exception {
        ConfirmActivity activity = controller.get();

        // nullじゃなければ落ちない
        activity.validateIntent(intent);
    }


    @Test(expected = RuntimeException.class)
    public void validateIntent_null() throws Exception {
        ConfirmActivity activity = controller.get();
        activity.validateIntent(null);
    }

    @Test
    public void onBackPressed() throws Exception {
        ConfirmActivity activity = controller
                .create()
                .get();
        activity.onBackPressed();

        Intent service = Shadows.shadowOf(activity).getNextStartedService();
        assertThat(service).isNotNull();
        assertThat(service.getComponent().getClassName()).isEqualTo(ToastService.class.getName());
        assertThat(service.getStringExtra(Intent.EXTRA_TEXT)).isEqualTo(RuntimeEnvironment.application.getString(R.string.canceled));
    }

}