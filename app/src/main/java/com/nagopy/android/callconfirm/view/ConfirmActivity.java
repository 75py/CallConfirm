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
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import com.nagopy.android.callconfirm.R;
import com.nagopy.android.callconfirm.databinding.ActivityConfirmBinding;
import com.nagopy.android.callconfirm.viewmodel.ConfirmViewModel;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class ConfirmActivity extends BaseActivity {

    @Inject
    ConfirmViewModel viewModel;

    ActivityConfirmBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getComponent().inject(this);
        viewModel.setOnFinishListener(this::finish);
        viewModel.setOnCancelListener(() -> {
            ToastService.show(ConfirmActivity.this, getString(R.string.canceled));
            finish();
        });

        Intent intent = getIntent();
        validateIntent(intent);

        String pn = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        viewModel.setPhoneNumber(pn);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_confirm);
        binding.setViewModel(viewModel);

        Single.create((SingleOnSubscribe<String>) e -> {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(pn));
            Cursor c = getContentResolver().query(
                    lookupUri
                    , new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}
                    , null, null, null);
            if (c != null && c.moveToFirst()) {
                e.onSuccess(c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
                c.close();
            } else {
                e.onSuccess(getString(R.string.unregistered));
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> viewModel.name.set(s), Timber::e);
    }

    @Override
    protected void onDestroy() {
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
