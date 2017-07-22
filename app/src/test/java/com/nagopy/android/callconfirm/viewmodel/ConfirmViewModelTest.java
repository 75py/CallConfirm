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

import android.view.View;

import com.nagopy.android.callconfirm.helper.HookState;
import com.nagopy.android.callconfirm.view.helper.Navigator;
import com.nagopy.android.callconfirm.viewmodel.ConfirmViewModel.OnCancelListener;
import com.nagopy.android.callconfirm.viewmodel.ConfirmViewModel.OnFinishListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConfirmViewModelTest {

    @Mock
    HookState hookState;

    @Mock
    Navigator navigator;

    @Mock
    OnFinishListener onFinishListener;

    @Mock
    OnCancelListener onCancelListener;

    @Mock
    ConfirmViewModel.OnLongClickListener onLongClickListener;

    @Mock
    View view;

    @InjectMocks
    ConfirmViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setOnFinishListener() throws Exception {
        OnFinishListener l = mock(OnFinishListener.class);

        viewModel.setOnFinishListener(l);

        assertThat(viewModel.onFinishListener).isEqualTo(l);
    }

    @Test
    public void setOnCancelListener() throws Exception {
        OnCancelListener l = mock(OnCancelListener.class);

        viewModel.setOnCancelListener(l);

        assertThat(viewModel.onCancelListener).isEqualTo(l);
    }

    @Test
    public void setOnLongClickListener() throws Exception {
        ConfirmViewModel.OnLongClickListener l = mock(ConfirmViewModel.OnLongClickListener.class);

        viewModel.setOnLongClickListener(l);

        assertThat(viewModel.onLongClickListener).isEqualTo(l);
    }

    @Test(expected = RuntimeException.class)
    public void setPhoneNumber_null() throws Exception {
        viewModel.setPhoneNumber(null);
    }

    @Test(expected = RuntimeException.class)
    public void setPhoneNumber_empty() throws Exception {
        viewModel.setPhoneNumber("");
    }

    @Test(expected = RuntimeException.class)
    public void setPhoneNumber_illegal() throws Exception {
        viewModel.setPhoneNumber("abc");
    }

    @Test
    public void setPhoneNumber() throws Exception {
        String pn = "1234567890";
        viewModel.setPhoneNumber(pn);
        assertThat(viewModel.phoneNumber.get()).isEqualTo(pn);
    }

    @Test
    public void destroy() throws Exception {
        viewModel.onFinishListener = null;
        viewModel.onCancelListener = null;

        viewModel.setOnFinishListener(mock(OnFinishListener.class));
        viewModel.setOnCancelListener(mock(OnCancelListener.class));
        assertThat(viewModel.onFinishListener).isNotNull();
        assertThat(viewModel.onCancelListener).isNotNull();

        viewModel.destroy();

        assertThat(viewModel.onFinishListener).isNull();
        assertThat(viewModel.onCancelListener).isNull();
    }

    @Test
    public void onClickCall() throws Exception {
        doNothing().when(hookState).setHookEnabled(anyBoolean());
        doNothing().when(navigator).call(anyString());
        doNothing().when(onFinishListener).onFinish();
        String pn = "1234567890";
        viewModel.setPhoneNumber(pn);

        viewModel.onClickCall.onClick(null);

        verify(hookState, times(1)).setHookEnabled(false);
        verify(navigator, times(1)).call(pn);
        verify(onFinishListener, times(1)).onFinish();
    }

    @Test
    public void onClickCancel() throws Exception {
        doNothing().when(onCancelListener).onCancel();

        viewModel.onClickCancel.onClick(null);

        verify(onCancelListener, times(1)).onCancel();
    }

    @Test
    public void onLongClick() throws Exception {
        doNothing().when(onLongClickListener).onLongClick(view);

        viewModel.onLongClick.onLongClick(view);

        verify(onLongClickListener, times(1)).onLongClick(view);
    }

}