package com.jov.laughter.frame;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.jov.laughter.R;
import com.jov.laughter.adapter.GoodListAdapter;
import com.jov.laughter.bean.GoodBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.net.HttpGetAndInsertThread;
import com.jov.laughter.net.HttpGetThread;
import com.jov.laughter.utils.Common;
import com.jov.laughter.utils.ThreadPoolUtils;
import com.jov.laughter.views.PullDownView;

public class BothFrame extends Fragment implements
		PullDownView.OnPullDownListener {
	private View view;
	private static DBOpenHelper dao;
	private PullDownView mPullDownView;
	private List<GoodBean> list;
	private GoodListAdapter mainListAdapter;
	private ListView mListView;
	private Context ctx;
	private int totalPage;
	private int wonPageNo = 1;
	private TextView nogoodTip;
	private static boolean isDoingUpdate = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.both_frame, container, false);
		ctx = view.getContext();
		dao = new DBOpenHelper(ctx);
		list = new ArrayList<GoodBean>();
		nogoodTip = (TextView) view.findViewById(R.id.nogood_tip);
		caculPage();
		initView();
		return view;
	}

	private void caculPage() {
		int won = dao.getGoodTotalCount();
		if (won != 0) {
			nogoodTip.setVisibility(View.GONE);
		}
		totalPage = won % 10 == 0 ? won / 10 : (won / 10 + 1);
		list.clear();
		list.addAll(0, dao.getGoods(0));
	}

	private void initView() {
		mPullDownView = (PullDownView) view.findViewById(R.id.good_listview);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setHideFooter();
		mPullDownView.setShowHeader();
		mainListAdapter = new GoodListAdapter(ctx, list);
		mListView = mPullDownView.getListView();
		mListView.setAdapter(mainListAdapter);
		mPullDownView.enableAutoFetchMore(true, 2);
		ThreadPoolUtils.execute(new HttpGetThread(resourceHand, Common.HTTPURL
				+ "good_resource.txt"));
		isDoingUpdate = true;
	}

	@Override
	public void onRefresh() {
		if (!isDoingUpdate) {
			ThreadPoolUtils.execute(new HttpGetThread(resourceHand,
					Common.HTTPURL + "good_resource.txt"));
		}
	}

	@Override
	public void onMore() {
		if (totalPage != 0 && wonPageNo != totalPage) {
			wonPageNo++;
			wonPageNo = wonPageNo > totalPage ? totalPage : wonPageNo;
			list.addAll(dao.getGoods((wonPageNo - 1) * 10));
			mainListAdapter.notifyDataSetChanged();
			mPullDownView.RefreshComplete();
		}
		mPullDownView.notifyDidMore();
	}

	private Handler resourceHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				isDoingUpdate = false;
				mPullDownView.RefreshComplete();
			} else if (msg.what == 100) {
				isDoingUpdate = false;
				mPullDownView.RefreshComplete();
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				if (!Common.isEmpty(result)) {
					ThreadPoolUtils.execute(new HttpGetAndInsertThread(
							contentHand, result, dao, 2));
				}
			} else {
				isDoingUpdate = false;
				mPullDownView.RefreshComplete();
			}
		};
	};
	private Handler contentHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
			} else if (msg.what == 100) {
			} else if (msg.what == 200) {
				boolean result = (boolean) msg.obj;
				if (result) {
					caculPage();
					mainListAdapter.notifyDataSetChanged();
					isDoingUpdate = false;
					mPullDownView.RefreshComplete();
					wonPageNo = 1;
				}
			}
			isDoingUpdate = false;
			mPullDownView.RefreshComplete();
		};
	};
}
