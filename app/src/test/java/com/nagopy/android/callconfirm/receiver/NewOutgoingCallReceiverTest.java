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

import com.nagopy.android.callconfirm.App;
import com.nagopy.android.callconfirm.di.ApplicationComponent;
import com.nagopy.android.callconfirm.di.ReceiverComponent;
import com.nagopy.android.callconfirm.helper.HookState;
import com.nagopy.android.callconfirm.helper.PermissionHelper;
import com.nagopy.android.callconfirm.receiver.helper.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class NewOutgoingCallReceiverTest {

    @Mock
    PermissionHelper permissionHelper;

    @Mock
    HookState hookState;

    @Mock
    Navigator navigator;

    @InjectMocks
    NewOutgoingCallReceiver newOutgoingCallReceiver;

    @Mock
    Context context;

    @Mock
    Intent intent;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        App.component = mock(ApplicationComponent.class);
        ReceiverComponent c = mock(ReceiverComponent.class);
        doReturn(c).when(App.component).createReceiverComponent(any());

        newOutgoingCallReceiver = spy(newOutgoingCallReceiver);
        doReturn("1234567890").when(newOutgoingCallReceiver).getResultData();
        doCallRealMethod().when(newOutgoingCallReceiver).onReceive(any(), any());
    }

    @Test
    public void onReceive_noPermissions() throws Exception {
        doReturn(false).when(permissionHelper).areGrantedPermissions();

        newOutgoingCallReceiver.onReceive(context, intent);

        verify(hookState, times(0)).isHookEnabled();
        verify(hookState, times(0)).setHookEnabled(anyBoolean());
        verify(navigator, times(0)).startConfirmActivity(anyString());
    }

    @Test
    public void onReceive_hook() throws Exception {
        doReturn(true).when(permissionHelper).areGrantedPermissions();
        doReturn(true).when(hookState).isHookEnabled();

        newOutgoingCallReceiver.onReceive(context, intent);

        verify(hookState, times(1)).isHookEnabled();
        verify(hookState, times(0)).setHookEnabled(anyBoolean());
        verify(newOutgoingCallReceiver, times(1)).setResultData(null);
        verify(navigator, times(1)).startConfirmActivity("1234567890");
    }

    @Test
    public void onReceive_hook_nullNumber() throws Exception {
        doReturn(true).when(permissionHelper).areGrantedPermissions();
        doReturn(true).when(hookState).isHookEnabled();
        doReturn(null).when(newOutgoingCallReceiver).getResultData();

        newOutgoingCallReceiver.onReceive(context, intent);

        verify(hookState, times(1)).isHookEnabled();
        verify(hookState, times(0)).setHookEnabled(anyBoolean());
        verify(newOutgoingCallReceiver, times(0)).setResultData(null);
        verify(navigator, times(0)).startConfirmActivity("1234567890");
    }

    @Test
    public void onReceive_hook_emptyNumber() throws Exception {
        doReturn(true).when(permissionHelper).areGrantedPermissions();
        doReturn(true).when(hookState).isHookEnabled();
        doReturn("").when(newOutgoingCallReceiver).getResultData();

        newOutgoingCallReceiver.onReceive(context, intent);

        verify(hookState, times(1)).isHookEnabled();
        verify(hookState, times(0)).setHookEnabled(anyBoolean());
        verify(newOutgoingCallReceiver, times(0)).setResultData(null);
        verify(navigator, times(0)).startConfirmActivity("1234567890");
    }

    @Test
    public void onReceive_through() throws Exception {
        doReturn(true).when(permissionHelper).areGrantedPermissions();
        doReturn(false).when(hookState).isHookEnabled();

        newOutgoingCallReceiver.onReceive(context, intent);

        verify(hookState, times(1)).isHookEnabled();
        verify(hookState, times(1)).setHookEnabled(true);
        verify(navigator, times(0)).startConfirmActivity(anyString());
    }

}