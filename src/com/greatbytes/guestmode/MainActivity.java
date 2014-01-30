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
import android.os.UserHandle;
import android.os.UserManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;


public class MainActivity extends Activity {

	private final static String TAG = "MainActivity";
	private Context mContext = this;

	//TODO: On Shortcut-click in guest mode, if SuperSU multi-user mode isn't enabled: "To be able to switch back to the main user via this shortcut, please enable \"Multiuser support\" in SuperSU while signed in as the main user. To switch back now, please reboot your phone!" 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		
		Button btnSwitchToGuestUser = (Button)findViewById(R.id.switch_to_guest_button);
		btnSwitchToGuestUser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				switchToGuestUser(mContext);
			}
		});
		
		Button btnSwitchToMainUser = (Button)findViewById(R.id.switch_to_main_button);
		btnSwitchToMainUser.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				switchToMainUser();
			}
		});

		//Get the current user's serial number (to determine if we are running as a guest or as the main account)
		long userSerialNumber = -1;
		UserHandle uh = android.os.Process.myUserHandle();
		UserManager um = (UserManager) mContext.getSystemService(Context.USER_SERVICE);
		if(um != null){
			userSerialNumber = um.getSerialNumberForUser(uh);
			Log.d(TAG, "userSerialNumber = " + userSerialNumber);
		}

		//Show the welcome dialog (and setup-activity) on the first app launch
		//Only show welcome dialog if we are the main user (guest users shouldn't be able to themselves spawn new guest accounts)
		if(Preferences.getInstance(mContext).getIsFirstLaunch() && userSerialNumber == 0){
			showWelcomeDialog();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
    public boolean onOptionsItemSelected(MenuItem item) {  
        switch (item.getItemId()) {
	    case R.id.action_debug:
	    	startActivity(new Intent(mContext, DebugActivity.class));
	        return true;
	    case R.id.action_about:
	    	showAboutDialog();
	        return true;
        }
        return super.onOptionsItemSelected(item);
    }  

    private void showWelcomeDialog(){
    	new AlertDialog.Builder(mContext) 
    	.setTitle(R.string.welcome_title) 
    	.setMessage(R.string.about_message) 
    	.setPositiveButton(R.string.welcome_agree, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//Show the first-start setup activity
				Preferences.getInstance(mContext).setIsFirstLaunch(false);
				startActivity(new Intent(mContext, SetupActivity.class));
			}
    	}) 
    	.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//User did not agree to disclaimer, exit the app
				finish();
			}
    	}) 
    	.setCancelable(false)
    	.show().setCanceledOnTouchOutside(false);
    }
    
    private void showAboutDialog(){
    	new AlertDialog.Builder(mContext) 
    	.setTitle(R.string.about_title) 
    	.setMessage(getString(R.string.about_message) + "\n\n" + getString(R.string.about_attribution)) 
    	.setPositiveButton(android.R.string.ok, null) 
    	.setCancelable(true)
    	.show();
    }

    public static void switchToGuestUser(Context context){
		int storedGuestUserId = Preferences.getInstance(context).getGuestUserId();
		if(storedGuestUserId != -1){
			TerminalUtils.switchUser(String.valueOf(storedGuestUserId));
		} else {
			//No guest-id stored in internal preferences, try to switch to user id 10 (usually the first guest user)
			TerminalUtils.switchUser("10");			
		}
    }
    
    public static void switchToMainUser(){
    	TerminalUtils.switchUser("0");			
    }

}
