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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.reactivex.observers.TestObserver;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ConfirmViewModelTest {

    @Mock
    HookState hookState;

    @Mock
    Navigator navigator;

    @Mock
    View view;

    @InjectMocks
    ConfirmViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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
        viewModel.destroy();
    }

    @Test
    public void onClickCall() throws Exception {
        doNothing().when(hookState).setHookEnabled(anyBoolean());
        doNothing().when(navigator).call(anyString());
        String pn = "1234567890";
        viewModel.setPhoneNumber(pn);
        TestObserver<Void> test = viewModel.callObserver.test();

        viewModel.onClickCall.onClick(null);

        verify(hookState, times(1)).setHookEnabled(false);
        verify(navigator, times(1)).call(pn);
        test.assertComplete();
    }

    @Test
    public void onClickCancel() throws Exception {
        TestObserver<Void> test = viewModel.cancelObserver.test();

        viewModel.onClickCancel.onClick(null);

        test.assertComplete();
    }

    @Test
    public void onLongClick() throws Exception {
        TestObserver<View> test = viewModel.longClickObserver.test();

        viewModel.onLongClick.onLongClick(view);

        test.assertValue(view)
                .assertNoErrors();

    }

}