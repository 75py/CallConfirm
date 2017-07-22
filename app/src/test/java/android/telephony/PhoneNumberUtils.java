/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.telephony;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Mock
public class PhoneNumberUtils {

    /*
     * global-phone-number = ["+"] 1*( DIGIT / written-sep )
     * written-sep         = ("-"/".")
     */
    private static final Pattern GLOBAL_PHONE_NUMBER_PATTERN =
            Pattern.compile("[\\+]?[0-9.-]+");

    // copy from original
    public static boolean isGlobalPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() == 0 /*TextUtils.isEmpty(phoneNumber)*/) {
            return false;
        }

        Matcher match = GLOBAL_PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        return match.matches();
    }

    // return value of mock method
    public static boolean isEmergencyNumber = false;

    public static boolean isEmergencyNumber(String number) {
        return isEmergencyNumber;
    }
}
