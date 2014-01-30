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

import com.greatbytes.guestmode.ui.StepPagerStrip;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class SetupActivity extends FragmentActivity {
	private ViewPager mPager;
	private MyPagerAdapter mPagerAdapter;

	private boolean mLastPage;

	private boolean mConsumePageSelectedEvent;

	private Button mNextButton;
	private Button mPrevButton;

	private StepPagerStrip mStepPagerStrip;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		
		mPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mPagerAdapter);
		mStepPagerStrip = (StepPagerStrip) findViewById(R.id.strip);
		mStepPagerStrip.setOnPageSelectedListener(new StepPagerStrip.OnPageSelectedListener() {
			@Override
			public void onPageStripSelected(int position) {
				position = Math.min(mPagerAdapter.getCount() - 1, position);
				if (mPager.getCurrentItem() != position) {
					mPager.setCurrentItem(position);
				}
			}
		});

		mStepPagerStrip.setPageCount(2);

		mNextButton = (Button) findViewById(R.id.next_button);
		mPrevButton = (Button) findViewById(R.id.prev_button);

		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				mStepPagerStrip.setCurrentPage(position);

				if (mConsumePageSelectedEvent) {
					mConsumePageSelectedEvent = false;
					return;
				}

				if(mPager.getCurrentItem() == 1){
					mLastPage = true;
				} else {
					mLastPage = false;
				}

				updateBottomBar();
			}
		});

		mNextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (mLastPage) {
					finish();
				} else {
					mPager.setCurrentItem(mPager.getCurrentItem() + 1);
				}
			}
		});

		mPrevButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mPager.setCurrentItem(mPager.getCurrentItem() - 1);
			}
		});

		updateBottomBar();
	}
	
	public void advanceToLastPage(){
		mPager.setCurrentItem(mPager.getChildCount() - 1);
	}

	private void updateBottomBar() {
		int position = mPager.getCurrentItem();
		mNextButton.setText(mLastPage ? R.string.done : R.string.next);
		mNextButton.setBackgroundResource(R.drawable.selectable_item_background);
		TypedValue v = new TypedValue();
		getTheme().resolveAttribute(android.R.attr.textAppearanceMedium, v, true);
		mNextButton.setTextAppearance(this, v.resourceId);

		mPrevButton.setVisibility(position <= 0 ? View.INVISIBLE : View.VISIBLE);
	}


	public class MyPagerAdapter extends FragmentStatePagerAdapter {
		private Fragment mPrimaryItem;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch(i){
			case 0:
				return new SetupFragment();
			case 1:
				return new InfoFragment();
			}
			return null;
		}

		@Override
		public int getItemPosition(Object object) {
			if (object == mPrimaryItem) {
				// Re-use the current fragment (its position never changes)
				return POSITION_UNCHANGED;
			}

		return POSITION_NONE;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			super.setPrimaryItem(container, position, object);
			mPrimaryItem = (Fragment)object;
		}

		@Override
		public int getCount() {
			return 2;
		}

	}
}
