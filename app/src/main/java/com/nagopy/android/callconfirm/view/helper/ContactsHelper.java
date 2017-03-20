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

package com.nagopy.android.callconfirm.view.helper;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;

import com.nagopy.android.callconfirm.R;
import com.nagopy.android.callconfirm.di.ActivityScope;

import javax.inject.Inject;

import io.reactivex.Single;

@ActivityScope
public class ContactsHelper {

    @Inject
    Activity activity;

    @Inject
    ContactsHelper() {
    }

    public Single<Data> findBy(String phoneNumber) {
        return Single.create(e -> {
            Uri lookupUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));

            Cursor c = activity.getContentResolver().query(
                    lookupUri
                    , new String[]{
                            ContactsContract.PhoneLookup.DISPLAY_NAME
                            , ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI
                    }
                    , null, null, null);

            Data data = new Data();
            if (c != null && c.moveToFirst()) {
                data.dispName = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
                data.thumbnailUri = c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI));
                c.close();
            } else {
                data.dispName = activity.getString(R.string.unregistered);
            }

            e.onSuccess(data);
        });
    }

    public static class Data {
        @Nullable
        public String dispName;

        @Nullable
        public String thumbnailUri;
    }
}
