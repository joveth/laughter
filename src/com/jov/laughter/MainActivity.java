package com.jov.laughter;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.offers.OffersManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.jov.laughter.net.UpdateManager;

public class MainActivity extends FragmentActivity {
	public static final String PAGE1_ID = "page1";
	public static final String PAGE2_ID = "page2";
	public static final String PAGE3_ID = "page3";

	private TabHost tabHost;
	private List<View> views;
	private FragmentManager manager;
	private ViewPager pager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		AdManager.getInstance(this).init("e132f8d9bc487ae0",
				"edec8be30763828b", false);
		OffersManager.getInstance(this).onAppLaunch();
		pager = (ViewPager) findViewById(R.id.viewpager);
		tabHost = (TabHost) findViewById(R.id.tab_host);
		manager = getSupportFragmentManager();
		views = new ArrayList<View>();

		views.add(manager.findFragmentById(R.id.images_tab).getView());
		views.add(manager.findFragmentById(R.id.texts_tab).getView());
		views.add(manager.findFragmentById(R.id.goods_tab).getView());
		tabHost.setup();
		TabContentFactory factory = new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return new View(MainActivity.this);
			}
		};
		// tab1
		TabSpec tabSpec = tabHost.newTabSpec(PAGE1_ID);
		tabSpec.setIndicator(createTabView(R.string.image_tab_name));
		tabSpec.setContent(factory);
		tabHost.addTab(tabSpec);
		// tab2
		TabSpec tabSpec2 = tabHost.newTabSpec(PAGE2_ID);
		tabSpec2.setIndicator(createTabView(R.string.text_tab_name));
		tabSpec2.setContent(factory);
		tabHost.addTab(tabSpec2);
		// tab3
		TabSpec tabSpec3 = tabHost.newTabSpec(PAGE3_ID);
		tabSpec3.setIndicator(createTabView(R.string.both_tab_name));
		tabSpec3.setContent(factory);
		tabHost.addTab(tabSpec3);

		tabHost.setCurrentTab(0);
		// updateTab(tabHost);
		UpdateManager updateManager = new UpdateManager(this);
		updateManager.checkVersionThread();
		pager.setAdapter(new PageAdapter());
		pager.setOnPageChangeListener(new PageChangeListener());
		tabHost.setOnTabChangedListener(new TabChangeListener());
	}

	/**
	 * PageView Adapter
	 * 
	 * @author Administrator
	 * 
	 */
	private class PageAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup view, int position, Object arg2) {
			view.removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			try {
				if (views.get(position).getParent() == null) {
					view.addView(views.get(position));
				} else {
					((ViewGroup) views.get(position).getParent())
							.removeView(views.get(position));
					view.addView(views.get(position));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return views.get(position);
		}
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class TabChangeListener implements OnTabChangeListener {
		@Override
		public void onTabChanged(String tabId) {
			if (PAGE1_ID.equals(tabId)) {
				pager.setCurrentItem(0);
			} else if (PAGE2_ID.equals(tabId)) {
				pager.setCurrentItem(1);
			} else if (PAGE3_ID.equals(tabId)) {
				pager.setCurrentItem(2);
			}
		}
	}

	/**
	 * 
	 * @author Administrator
	 * 
	 */
	private class PageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int arg0) {
			tabHost.setCurrentTab(arg0);
		}
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private View createTabView(int stringId) {
		View tabView = getLayoutInflater().inflate(R.layout.tab, null);
		TextView textView = (TextView) tabView.findViewById(R.id.tab_text);
		textView.setText(stringId);
		return tabView;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		switch (item.getItemId()) {
		case R.id.menu_fav:
			switchTo(FavoriteActivity.class);
			return true;
		case R.id.menu_setting:
			switchTo(MoreSettingActivity.class);
			return true;
		case R.id.menu_app:
			OffersManager.getInstance(MainActivity.this).showOffersWallDialog(MainActivity.this);
			return true;
		default:
			return true;
		}
	}

	private void switchTo(Class clazz) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		startActivity(intent);
	}

	private long exitTime = 0;

	public void backTo() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "~@^_^@~ 亲，再按一次才能退出哦！",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			OffersManager.getInstance(this).onAppExit();
			finish();
			System.exit(0);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			backTo();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_MENU) {

		}
		return super.onKeyDown(keyCode, event);
	}
}
