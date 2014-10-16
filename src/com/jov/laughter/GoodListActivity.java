package com.jov.laughter;

import java.util.ArrayList;
import java.util.List;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jov.laughter.adapter.ContentListAdapter;
import com.jov.laughter.bean.GoodContentBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.utils.Common;
import com.jov.laughter.views.PullDownView;

public class GoodListActivity extends Activity implements
		PullDownView.OnPullDownListener {
	private Intent intent;
	private DBOpenHelper dbHelper;
	private List<GoodContentBean> list;
	private PullDownView mPullDownView;
	private int wonPageNo = 1;
	private int wonTotalPage = 0;
	private ContentListAdapter mainListAdapter;
	private ListView mListView;
	private String subject;
	private String date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_frame);
		list = new ArrayList<GoodContentBean>();
		initData();
		initView();
	}
	private void initData() {
		intent = getIntent();
		date = intent.getStringExtra("good.date");
		subject = intent.getStringExtra("good.subject");
		if (Common.isEmpty(date)) {
			finish();
		}
		dbHelper = new DBOpenHelper(this);
	}

	private void calculatePageNo() {
		int won = dbHelper.getGoodContentCount(date);
		if (won == 0) {
			finish();
		}
		wonTotalPage = won % 10 == 0 ? won / 10 : (won / 10 + 1);
		list.clear();
		list.addAll(0, dbHelper.getGoodContents(0,date));
	}

	private void initView() {
		mPullDownView = (PullDownView) findViewById(R.id.content_listview);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setHideFooter();
		mPullDownView.setHideHeader();
		calculatePageNo();
		mainListAdapter = new ContentListAdapter(this, list,dbHelper);
		mListView = mPullDownView.getListView();
		mListView.setAdapter(mainListAdapter);
		mPullDownView.enableAutoFetchMore(true, 2);
		if(!Common.isEmpty(subject)){
			((TextView)findViewById(R.id.menu_title_id)).setText(Common.cutLongStr(subject, 20));
		}
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.ad_bar_con);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		adlayout.addView(adView);
		adlayout.setVisibility(View.VISIBLE);
	}
	
	@Override
	public void onRefresh() {
		mPullDownView.RefreshComplete();
	}

	@Override
	public void onMore() {
		if (wonTotalPage != 0 && wonPageNo != wonTotalPage) {
			wonPageNo++;
			wonPageNo = wonPageNo > wonTotalPage ? wonTotalPage : wonPageNo;
			list.addAll(dbHelper.getGoodContents((wonPageNo - 1) * 10,date));
			mainListAdapter.notifyDataSetChanged();
			mPullDownView.RefreshComplete();
		}
		mPullDownView.notifyDidMore();
	}
}
