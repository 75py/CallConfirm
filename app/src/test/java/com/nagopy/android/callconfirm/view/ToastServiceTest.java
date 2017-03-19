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
import android.os.Handler;
import android.os.Looper;

import com.nagopy.android.callconfirm.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class ToastServiceTest {

    @Mock
    Intent intent;

    @Mock
    Handler mainHandler;

    @InjectMocks
    ToastService toastService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        doReturn(false).when(mainHandler).post(any());
    }

    @Test
    public void onHandleIntent() throws Exception {
        toastService.onHandleIntent(null);
        verify(mainHandler, times(0)).post(any());

        doReturn(null).when(intent).getStringExtra(Intent.EXTRA_TEXT);
        toastService.onHandleIntent(intent);
        verify(mainHandler, times(0)).post(any());

        doReturn("").when(intent).getStringExtra(Intent.EXTRA_TEXT);
        toastService.onHandleIntent(intent);
        verify(mainHandler, times(0)).post(any());

        doReturn("msg").when(intent).getStringExtra(Intent.EXTRA_TEXT);
        toastService.onHandleIntent(intent);
        verify(mainHandler, times(1)).post(any());
    }

    @Test
    public void showToast() throws Exception {
        toastService.mainHandler = new Handler(Looper.getMainLooper());
        doReturn("msg").when(intent).getStringExtra(Intent.EXTRA_TEXT);
        toastService.onHandleIntent(intent);

        String text = ShadowToast.getTextOfLatestToast();
        assertThat(text).isNotEmpty();
        assertThat(text).isEqualTo("msg");
    }

}