/*
 * Copyright (C) 2013 The Android Open Source Project
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

package com.sec.omnium.omnikey.hce;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Utility class for persisting password phrase to disk.
 *
 * <p>The default SharedPreferences instance is used as the backing storage. Values are cached
 * in memory for performance.
 *
 * <p>This class is thread-safe.
 */
public class PasswordStorage {
    private static final String PREF_PASSWORD_PHRASE = "user_pass";
    private static final String DEFAULT_PASSWORD_PHRASE = "00000000";
    private static final String TAG = "PasswordStorage";
    private static String sPassword = null;
    private static final Object sPasswordLock = new Object();

    public static void SetAccount(Context c, String s) {
        synchronized(sPasswordLock) {
            Log.i(TAG, "Setting account number: " + s);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
            prefs.edit().putString(PREF_PASSWORD_PHRASE, s).commit();
            sPassword = s;
        }
    }

    public static String GetAccount(Context c) {
        synchronized (sPasswordLock) {
            if (sPassword == null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(c);
                String account = prefs.getString(PREF_PASSWORD_PHRASE, DEFAULT_PASSWORD_PHRASE);
                sPassword = account;
            }
            return sPassword;
        }
    }
}
