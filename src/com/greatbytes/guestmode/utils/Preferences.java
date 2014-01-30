/*
 * Copyright 2014 Great Bytes Software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.greatbytes.guestmode.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;


public class Preferences implements OnSharedPreferenceChangeListener {

	//Internal preferences
	public final static String PREF_KEY_FIRST_LAUNCH = "first_launch";
	public final static String PREF_KEY_GUEST_USER_ID = "guest_user_id";
	
	private boolean mIsFirstLaunch;
	private int mGuestUserId;
	
	
	/**
	 * Section for singleton pattern
	 */
	private SharedPreferences mPref;
	private Preferences(Context context) {
		mPref = PreferenceManager.getDefaultSharedPreferences(context);
		mPref.registerOnSharedPreferenceChangeListener(this);
		
		reloadPreferences();
	}
		
	public void reloadPreferences() {
		mIsFirstLaunch = mPref.getBoolean(PREF_KEY_FIRST_LAUNCH, true);
		mGuestUserId = mPref.getInt(PREF_KEY_GUEST_USER_ID, -1);
	}
	
	private static Preferences mInstance;
	public static Preferences getInstance(Context context){
		return mInstance == null ?
				(mInstance = new Preferences(context)) :
					mInstance;
	}

	public boolean getIsFirstLaunch(){
		return mIsFirstLaunch;
	}
	
	public void setIsFirstLaunch(boolean firstLaunch){
		mIsFirstLaunch = firstLaunch;
		mPref.edit().putBoolean(PREF_KEY_FIRST_LAUNCH, mIsFirstLaunch).commit();	
	}
	
	public int getGuestUserId(){
		return mGuestUserId;
	}
	
	public void setGuestUserId(int userId){
		mGuestUserId = userId;
		mPref.edit().putInt(PREF_KEY_GUEST_USER_ID, mGuestUserId).commit();	
	}
	

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		reloadPreferences();
	}
}
