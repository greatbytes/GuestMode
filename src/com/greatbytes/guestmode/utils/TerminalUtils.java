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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import android.util.Log;

public class TerminalUtils {

	public static String TAG = "TerminalUtils";

	public static void runAsRoot(String[] cmds){
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  
			BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));
			for (String tmpCmd : cmds) {
				os.writeBytes(tmpCmd + "\n");
				String test;
				while((test = bf.readLine()) != null)
				{
					Log.i(TAG, test);
				}
			}
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "runAsRoot IOException: " + e.getMessage());
		}
	}

	public static int createGuestUser()
	{
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  

			os.writeBytes("pm create-user \"guest\"\n");
			os.writeBytes("exit\n");
			os.flush();

			InputStream is = p.getInputStream();
			byte[] buffer = new byte[BUFF_LEN];
			int read;
			String out = new String();
			//read method will wait forever if there is nothing in the stream
			while(true){
				read = is.read(buffer);
				out += new String(buffer, 0, read);
				if(read<BUFF_LEN){
					//we have read everything
					break;
				}
			}

			Log.i(TAG, "out: " + out);

			if(out.contains("Success:")){
				out = out.replace("\n", "").replace("\r", "");
				String[] strUserId = out.split("id ");
				//Successfully created new user
				int userId = Integer.valueOf(strUserId[1]);
				return userId;
			} else {
				//Multiuser feature is enabled
				return -1;
			}
		} catch (IOException e) {
			Log.e(TAG, "createGuestUser IOException: " + e.getMessage());
		} catch (Exception e){
			Log.e(TAG, "createGuestUser Exception: " + e.getMessage());
		}
		
		return -1;
	}

	public static void switchUser(String id)
	{
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  

			os.writeBytes("am switch-user " + id + "\n");
			os.writeBytes("exit\n");
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "switchUser IOException: " + e.getMessage());
		}
	}

	public static void reboot() {
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  

			os.writeBytes("reboot\n");
			os.writeBytes("exit\n");
			os.flush();
		} catch (IOException e) {
			Log.e(TAG, "reboot IOException: " + e.getMessage());
		}
	}

	public static String[] getUserList()
	{
		Process p;
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());  
			BufferedReader bf = new BufferedReader(new InputStreamReader(p.getInputStream()));

			os.writeBytes("pm list users"+"\n"); //TODO: 4.2-command seems to be "pm list-users"!			
			os.writeBytes("exit\n"); 
			ArrayList<String> users = new ArrayList<String>();
			String test;
			bf.readLine();
			while((test = bf.readLine()) != null){
				users.add(test);
			}

			String[] userList = (String[]) users.toArray(new String[users.size()]);

			os.flush();
			return userList;
		} catch (IOException e) {
			Log.e(TAG, "getUserList IOException: " + e.getMessage());
			return null;
		}
	}
	
	public static void enableMultiUserMode(){
		try{
			Process p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("setprop fw.max_users 8\n");
			os.flush();

		} catch (IOException e){
			Log.e(TAG, "enableMultiUserMode IOException: " + e.getMessage());
		}
	}

	public static void enableGuestModeAppForUser(String userId){
		try{
			Process p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("pm enable --user " + userId + " com.greatbytes.guestmode" + "\n");
			os.flush();
		} catch (Exception e){
			Log.e(TAG, "enableGuestModeAppForUser Exception: " + e.getMessage());
		}
	}
	
	private static final int BUFF_LEN = 4096;
	public static boolean checkMultiUserEnabled(){
		try{
			Process p = Runtime.getRuntime().exec("pm get-max-users\n");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.flush();

			InputStream is = p.getInputStream();
			byte[] buffer = new byte[BUFF_LEN];
			int read;
			String out = new String();
			//read method will wait forever if there is nothing in the stream
			while(true){
				read = is.read(buffer);
				out += new String(buffer, 0, read);
				if(read<BUFF_LEN){
					//we have read everything
					break;
				}
			}

			Log.i(TAG, "out: " + out);

			if(out.contains("Maximum supported users:")){
				out = out.replace("\n", "").replace("\r", "");
				String[] maxUsers = out.split(": ");
				if(Integer.valueOf(maxUsers[1]) == 1){
					//Multiuser feature is disabled
					return false;
				} else {
					//Multiuser feature is enabled
					return true;
				}
			}

		} catch (IOException e){
			Log.e(TAG, "checkFeatureEnabled IOException: " + e.getMessage());
		} catch (Exception e){
			Log.e(TAG, "checkFeatureEnabled Exception: " + e.getMessage());
		}
		return false;
	}
	
}