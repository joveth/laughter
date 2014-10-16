package com.jov.laughter;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jov.laughter.adapter.FavoriteListAdapter;
import com.jov.laughter.bean.FavoriteBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.views.PullDownView;

public class FavoriteActivity extends Activity implements
		PullDownView.OnPullDownListener {
	private DBOpenHelper dbHelper;
	private List<FavoriteBean> list;
	private PullDownView mPullDownView;
	private int wonPageNo = 1;
	private int wonTotalPage = 0;
	private FavoriteListAdapter mainListAdapter;
	private ListView mListView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favor);
		dbHelper = new DBOpenHelper(this);
		list = new ArrayList<FavoriteBean>();
		initView();
	}

	private void calculatePageNo() {
		int won = dbHelper.getFavoriteCount();
		wonTotalPage = won % 10 == 0 ? won / 10 : (won / 10 + 1);
		list.clear();
		list.addAll(0, dbHelper.getFavorites(0));
	}

	private void initView() {
		mPullDownView = (PullDownView) findViewById(R.id.myfavlistview);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setHideFooter();
		mPullDownView.setHideHeader();
		calculatePageNo();
		if (wonTotalPage != 0) {
			findViewById(R.id.nofav_tip).setVisibility(View.GONE);
		}
		mainListAdapter = new FavoriteListAdapter(this, list);
		mListView = mPullDownView.getListView();
		mListView.setAdapter(mainListAdapter);
		mPullDownView.enableAutoFetchMore(true, 2);
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.ad_bar_fav);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		adlayout.addView(adView);
		adlayout.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRefresh() {
		mPullDownView.RefreshComplete();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.setting_menu, menu);
		return true;
	}

	@Override
	public void onMore() {
		if (wonTotalPage != 0 && wonPageNo != wonTotalPage) {
			wonPageNo++;
			wonPageNo = wonPageNo > wonTotalPage ? wonTotalPage : wonPageNo;
			list.addAll(dbHelper.getFavorites((wonPageNo - 1) * 10));
			mainListAdapter.notifyDataSetChanged();
			mPullDownView.RefreshComplete();
		}
		mPullDownView.notifyDidMore();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_setting:
			switchTo(MoreSettingActivity.class);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void switchTo(Class clazz) {
		Intent intent = new Intent();
		intent.setClass(this, clazz);
		startActivity(intent);
		dbHelper.close();
		finish();
	}
}
