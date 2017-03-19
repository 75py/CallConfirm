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

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.nagopy.android.callconfirm.App;

import javax.inject.Inject;

import timber.log.Timber;

public class ToastService extends IntentService {

    @Inject
    Handler mainHandler;

    public ToastService() {
        super(ToastService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.getComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Timber.d("onHandleIntent %s", intent);

        if (intent == null) {
            return;
        }

        String msg = intent.getStringExtra(Intent.EXTRA_TEXT);
        Timber.d("onHandleIntent msg=%s", msg);

        if (msg != null && !msg.isEmpty()) {
            mainHandler.post(() ->
                    Toast.makeText(ToastService.this, msg, Toast.LENGTH_LONG).show()
            );
        }
    }

    public static void show(Context context, String msg) {
        context.startService(new Intent(context, ToastService.class)
                .putExtra(Intent.EXTRA_TEXT, msg));
    }

}
