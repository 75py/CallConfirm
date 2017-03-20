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

import com.nagopy.android.callconfirm.helper.PermissionHelper;
import com.nagopy.android.callconfirm.view.helper.Navigator;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StartViewModelTest {

    @Mock
    Navigator navigator;

    @Mock
    PermissionHelper permissionHelper;

    @InjectMocks
    StartViewModel viewModel;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void updateGrantedPermissions_true() throws Exception {
        doReturn(true).when(permissionHelper).areGrantedPermissions();
        viewModel.updateGrantedPermissions();
        assertThat(viewModel.arePermissionsGranted.get()).isTrue();
    }

    @Test
    public void updateGrantedPermissions_false() throws Exception {
        doReturn(false).when(permissionHelper).areGrantedPermissions();
        viewModel.updateGrantedPermissions();
        assertThat(viewModel.arePermissionsGranted.get()).isFalse();
    }

    @Test
    public void startAppSetting() throws Exception {
        doNothing().when(navigator).startAppSetting();

        viewModel.startAppSetting.onClick(null);

        verify(navigator, times(1)).startAppSetting();
    }

    @Test
    public void startLicenseActivity() throws Exception {
        doNothing().when(navigator).startLicenseActivity();

        viewModel.startLicenseActivity.onClick(null);

        verify(navigator, times(1)).startLicenseActivity();
    }

    @Test
    public void openGitHub() throws Exception {
        doNothing().when(navigator).openGitHub();

        viewModel.openGitHub.onClick(null);

        verify(navigator, times(1)).openGitHub();
    }

}