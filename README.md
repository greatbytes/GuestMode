# GuestMode

Have you ever given your phone to a friend? Has this resulted in a semi-funny new Twitter status update that you didn't write yourself? You betcha! This app enables a guest user mode on your rooted 4.2+ phone, which you can easily switch to using a provided launcher shortcut. The guest environment is completely separate from your main account and doesn't have access to your email, text messages, image gallery, browsing history, or other app data. When setting up the guest account for the first time, please select "Not now" when prompted for a Google account. **DON'T** sign in with your own Google account or the guest will have access to all your data.

The app will:
* Enable multi-user mode on your device
* Create a new user called "guest"
* Enable this app for the guest user account

![Guest Mode Screenshot](https://lh6.googleusercontent.com/-eeFTUxry3nQ/UuqXKzZ1JiI/AAAAAAAAAQ4/cguR51WjRpM/w557-h942-no/guest_mode_screenshot_1.png)![Guest Mode Screenshot](https://lh3.googleusercontent.com/-1IRzhPsOCFI/UuqXK_gEfBI/AAAAAAAAAQ8/dWBPn_3SqTo/w557-h942-no/guest_mode_screenshot_2.png)

**This app requires a *rooted* device running Android 4.2+**

If you want to be able to use the app's shortcut to switch *back* to the main user from the guest mode, please enable multi-user support in SuperSU (or your superuser-manager of choice). If you don't perform this step, you will have to reboot your phone to get back into your main account! It may be necessary to separately install this app for the guest after switching to the guest account for the first time. For security purposes, it is recommended *NOT* to enable the multi-user support in SuperSU (because the guest would then be able to execute root-commands). Make sure you enable a secure lockscreen option for the main user account!

Please note that you are using this app at your own risk! I am not responsible for any damage it may cause to your device. The multi-user feature that this app relies on is currently not officially released/support for phones. Please perform a full device backup (e.g. using nandroid) before using this app. If you get stuck in the guest's account for whatever reason, please reboot your device. If this doesn't help, open a command prompt on your PC, enter `adb shell` followed by `am switch-user 0`. This will switch you back to the main account.

The app is available on Google Play:

<a href="https://play.google.com/store/apps/details?id=com.greatbytes.guestmode">
  <img alt="Android app on Google Play"
       src="https://developer.android.com/images/brand/en_app_rgb_wo_45.png" />
</a>

## Changelog

### Current Version: 1.0
 * Initial release
 
## Developed By

* [Great Bytes Software](http://www.greatbytes.org)
 
## Attribution

 * Raghav Sood - Author of the [UserManagement](https://github.com/RaghavSood/UserManagement) app
 * SferaDev - Author of the [Multiple User Enabler](https://github.com/SferaDev/4.2MultiuserEnabler) app
 * Roman Nurik - Author of the [Wizard Pager](https://github.com/romannurik/Android-WizardPager/) library 

## License

    Copyright 2014 Great Bytes Software

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.