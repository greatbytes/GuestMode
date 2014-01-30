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

package com.greatbytes.guestmode;

import com.greatbytes.guestmode.utils.Preferences;
import com.greatbytes.guestmode.utils.TerminalUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DebugActivity extends Activity {

	private Context mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_debug);

		Button btnEnableMultiuserSupport = (Button)findViewById(R.id.btnEnableMultiuserSupport);
		btnEnableMultiuserSupport.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				TerminalUtils.enableMultiUserMode();
				showMultiUserEnabledStatus(TerminalUtils.checkMultiUserEnabled());
			}
		});
		
		Button btnCheckMultiuserSupport = (Button)findViewById(R.id.btnCheckMultiuserSupport);
		btnCheckMultiuserSupport.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 showMultiUserEnabledStatus(TerminalUtils.checkMultiUserEnabled());
			}
		});
		
		Button btnSwitchToGuestUser = (Button)findViewById(R.id.btnSwitchToGuestUser);
		btnSwitchToGuestUser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int storedGuestUserId = Preferences.getInstance(mContext).getGuestUserId();
				if(storedGuestUserId != -1){
					TerminalUtils.switchUser(String.valueOf(storedGuestUserId));
				} else {
					Toast.makeText(mContext, "No guest user set up", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		Button btnSwitchToMainUser = (Button)findViewById(R.id.btnSwitchToMainUser);
		btnSwitchToMainUser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				TerminalUtils.switchUser("0");
			}
		});
		
		Button btnEnableAppForGuestUser = (Button)findViewById(R.id.btnEnableAppForGuestUser);
		btnEnableAppForGuestUser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				int storedGuestUserId = Preferences.getInstance(mContext).getGuestUserId();
				if(storedGuestUserId != -1){
					TerminalUtils.enableGuestModeAppForUser(String.valueOf(storedGuestUserId));
				} else {
					Toast.makeText(mContext, "No guest user set up", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//Show the current multiuser-mode enabled status
		showMultiUserEnabledStatus(TerminalUtils.checkMultiUserEnabled());
	}

	private void showMultiUserEnabledStatus(boolean isFeatureEnabled){
		TextView tvStatus = (TextView)findViewById(R.id.multiuser_support_status);
		tvStatus.setText(isFeatureEnabled ? R.string.enabled : R.string.disabled);
		tvStatus.setTextColor(isFeatureEnabled ? getResources().getColor(R.color.green) : getResources().getColor(R.color.red));
	}

}
