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

import com.greatbytes.guestmode.R;
import com.greatbytes.guestmode.utils.Preferences;
import com.greatbytes.guestmode.utils.TerminalUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class SetupFragment extends Fragment {

	private static final String TAG = "SetupFragment";

	Button btnSetup;
	
    public SetupFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setup, container, false);

        TextView titleView = (TextView)rootView.findViewById(android.R.id.title);
        titleView.setText(R.string.title_setup);
        
        btnSetup = (Button)rootView.findViewById(R.id.setup_button);
        btnSetup.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				setupGuestMode();				
			}
        });
        
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
    
    private void setupGuestMode(){
    	//Enable multiuser support (8 users)
    	TerminalUtils.enableMultiUserMode();
		
    	//Create new user named "guest"
    	int createdUserId = TerminalUtils.createGuestUser();
    	if(createdUserId != -1){
    		Preferences.getInstance(getActivity()).setGuestUserId(createdUserId);
    		Log.i(TAG, "User created successfully, ID=" + createdUserId);
    		btnSetup.setEnabled(false);
    	} else {
    		Toast.makeText(getActivity(), R.string.error_creating_user, Toast.LENGTH_LONG).show();
    		return;
    	}
    	
    	//Enable guestmode-app for user "guest"
    	TerminalUtils.enableGuestModeAppForUser(String.valueOf(createdUserId));
    	
    	//Move to the next page if the feature was successfully enabled
    	if(TerminalUtils.checkMultiUserEnabled()){
    		Log.i(TAG, "Feature enabled successfully - move to last page");
    		((SetupActivity)getActivity()).advanceToLastPage();
    	} else {
    		Log.e(TAG, "Enabling multiuser feature failed - show dialog");
    	}
    }

}
