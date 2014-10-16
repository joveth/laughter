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

import com.jov.laughter.R;
import com.jov.laughter.adapter.TextListAdapter;
import com.jov.laughter.bean.TextBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.net.HttpGetAndInsertThread;
import com.jov.laughter.net.HttpGetThread;
import com.jov.laughter.utils.Common;
import com.jov.laughter.utils.ThreadPoolUtils;
import com.jov.laughter.views.PullDownView;

public class TextFrame extends Fragment implements
		PullDownView.OnPullDownListener {
	private View view;
	private List<TextBean> list = new ArrayList<TextBean>();
	private Context ctx;
	private static DBOpenHelper dao;
	private PullDownView mPullDownView;
	private int wonPageNo = 1;
	private int wonTotalPage = 0;
	private TextListAdapter mainListAdapter;
	private ListView mListView;
	private TextBean textBean;
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
		view = inflater.inflate(R.layout.text_frame, container, false);
		ctx = view.getContext();
		dao = new DBOpenHelper(ctx);
		textBean = new TextBean();
		calculatePageNo();
		initView();
		return view;
	}

	private void initLocalResource() {
		list.addAll(textBean.initSelf());
	}

	private void calculatePageNo() {
		int won = dao.getTextTotalCount();
		wonTotalPage = won % 10 == 0 ? won / 10 : (won / 10 + 1);
		list.clear();
		if(wonTotalPage==0){
			initLocalResource();
		}else{
			list.addAll(0,dao.getTexts(0));
		}
	}

	private void initView() {
		mPullDownView = (PullDownView) view.findViewById(R.id.main_listview);
		mPullDownView.setOnPullDownListener(this);
		mPullDownView.setHideFooter();
		mPullDownView.setShowHeader();
		mainListAdapter = new TextListAdapter(ctx, list,dao);
		mListView = mPullDownView.getListView();
		mListView.setAdapter(mainListAdapter);
		mPullDownView.enableAutoFetchMore(true, 2);
		ThreadPoolUtils.execute(new HttpGetThread(resourceHand, Common.HTTPURL
				+ "text_resource.txt"));
		isDoingUpdate = true;
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
							contentHand, result, dao,1));
				}
			}else{
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
					calculatePageNo();
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

	@Override
	public void onRefresh() {
		if(!isDoingUpdate){
			ThreadPoolUtils.execute(new HttpGetThread(resourceHand, Common.HTTPURL
					+ "text_resource.txt"));
		}
	}

	@Override
	public void onMore() {
		if (wonTotalPage!=0&&wonPageNo != wonTotalPage) {
			wonPageNo++;
			wonPageNo = wonPageNo > wonTotalPage ? wonTotalPage : wonPageNo;
			list.addAll(dao.getTexts((wonPageNo - 1) * 10));
			mainListAdapter.notifyDataSetChanged();
			mPullDownView.RefreshComplete();
		}
		mPullDownView.notifyDidMore();
	}
}
