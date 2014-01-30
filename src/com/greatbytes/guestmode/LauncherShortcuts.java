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

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

public class LauncherShortcuts extends Activity {

    private static final String EXTRA_KEY = "com.greatbytes.guestmode.LAUNCHER_SHORTCUTS";
    private static final String ACTION_SWITCH_TO_GUEST = "com.greatbytes.guestmode.ACTION_SWITCH_TO_GUEST";
    private static final String ACTION_SWITCH_TO_MAIN = "com.greatbytes.guestmode.ACTION_SWITCH_TO_MAIN";
    
	private final String TAG = "LauncherShortcuts";
	final Context mContext = this;
    
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        //Resolve the intent
        final Intent intent = getIntent();
        final String action = intent.getAction();

        //If the intent is a request to create a shortcut, we'll do that and exit
        if (Intent.ACTION_CREATE_SHORTCUT.equals(action)) {
            setupShortcut();
            return;
        }

        //If we weren't launched with a CREATE_SHORTCUT intent but with a "SWTICH"-action-extra, perform the switch
        String extra = intent.getStringExtra(EXTRA_KEY);
        if (extra.equals(ACTION_SWITCH_TO_GUEST)) {
    		Log.i(TAG, "SWITCH TO GUEST shortcut selected, switching...");
    		Toast.makeText(mContext, getString(R.string.shortcut_toast_switching_to_guest), Toast.LENGTH_SHORT).show();
    		MainActivity.switchToGuestUser(mContext);
        } else if (extra.equals(ACTION_SWITCH_TO_MAIN)) {
        	Log.i(TAG, "SWITCH TO MAIN shortcut selected, switching...");
        	Toast.makeText(mContext, getString(R.string.shortcut_toast_switchting_to_main), Toast.LENGTH_SHORT).show();
        	MainActivity.switchToMainUser();
        }
         
        //Close this activity
        finish();
    }

    private void setupShortcut() {
    	//Show the shortcut-dialog
    	showDialog(0);
    }
    
	@Override
	public Dialog onCreateDialog(int id) {
		int selectionOptions = R.array.shortcut_config_diag_entries;
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.shortcut_config_diag_title)
		.setSingleChoiceItems(selectionOptions, 0, selectShortcutActionDiagListener)
		.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.dismiss();
				finish();
			}
		});
		return builder.create();
	}
	
	private DialogInterface.OnClickListener selectShortcutActionDiagListener = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			switch(which){
			case 0:
				//Switch to the guest account
				returnShortcutIntent(ACTION_SWITCH_TO_GUEST, R.string.shortcut_label_switch_to_guest);
				break;
			case 1:
				//Switch back to the main account
				returnShortcutIntent(ACTION_SWITCH_TO_MAIN, R.string.shortcut_label_switch_to_main);
				break;
			}
		}
	};
	
	private void returnShortcutIntent(String action, int labelRes){
    	//Set up the shortcut intent
        Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
        shortcutIntent.setClassName(this, this.getClass().getName());
        shortcutIntent.putExtra(EXTRA_KEY, action);

        //Set up the container intent
        Intent intent = new Intent();
        intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(labelRes));
        Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this,  R.drawable.ic_launcher);
        intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);

        //Return the result to the launcher
        setResult(RESULT_OK, intent);
        finish();
	}
}