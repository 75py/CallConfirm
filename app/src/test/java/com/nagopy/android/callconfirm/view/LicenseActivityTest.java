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

import android.webkit.WebView;

import com.nagopy.android.callconfirm.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LicenseActivityTest {

    private ActivityController<LicenseActivity> controller;

    @Before
    public void setUp() throws Exception {
        controller = Robolectric.buildActivity(LicenseActivity.class);
    }

    @Test
    public void onCreate() throws Exception {
        controller.create();
        LicenseActivity activity = controller.get();
        assertThat(activity.webView).isNotNull();
    }

    @Test
    public void onDestroy() throws Exception {
        controller.create()
                .start()
                .resume()
                .pause()
                .stop();
        LicenseActivity activity = controller.get();
        activity.webView = mock(WebView.class);
        controller.destroy();
        activity = controller.get();
        verify(activity.webView, times(1)).destroy();
    }

}