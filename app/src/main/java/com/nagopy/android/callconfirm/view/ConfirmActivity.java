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
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.nagopy.android.callconfirm.R;
import com.nagopy.android.callconfirm.databinding.ActivityConfirmBinding;
import com.nagopy.android.callconfirm.viewmodel.ConfirmViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class ConfirmActivity extends BaseActivity {

    @Inject
    ConfirmViewModel viewModel;

    ActivityConfirmBinding binding;

    List<Disposable> disposables = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);

        disposables.add(viewModel.callObserver
                .compose(bindToLifecycle())
                .subscribe(this::finish));
        disposables.add(viewModel.cancelObserver
                .compose(bindToLifecycle())
                .subscribe(() -> {
                    ToastService.show(this, getString(R.string.canceled));
                    finish();
                }));
        viewModel.longClickObserver
                .compose(bindToLifecycle())
                .subscribe(view -> Toast.makeText(ConfirmActivity.this, view.getContentDescription(), Toast.LENGTH_SHORT).show());

        Intent intent = getIntent();
        validateIntent(intent);

        String pn = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        viewModel.setPhoneNumber(pn);
        viewModel.findContactData();

        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm);
        binding.setViewModel(viewModel);
    }

    @Override
    protected void onDestroy() {
        disposables.forEach(disposable -> {
            if (!disposable.isDisposed()) {
                disposable.dispose();
            }
        });
        super.onDestroy();
        viewModel.destroy();
    }

    void validateIntent(Intent intent) {
        if (intent == null) {
            throw new RuntimeException("getIntent() returns null");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ToastService.show(ConfirmActivity.this, getString(R.string.canceled));
    }
}
