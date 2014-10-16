package com.jov.laughter.net;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.utils.Common;
import com.jov.laughter.utils.XMLReader;

public class HttpGetAndInsertThread implements Runnable {

	private Handler hand;
	private String url;
	private String res;
	private GetResource myGet = new GetResource();
	private XMLReader reader;
	private DBOpenHelper dao;
	private int type = 1;

	public HttpGetAndInsertThread(Handler hand, String res, DBOpenHelper dao,
			int type) {
		this.hand = hand;
		this.res = res;
		reader = new XMLReader();
		this.dao = dao;
		this.type = type;
	}

	@Override
	public void run() {
		Message msg = hand.obtainMessage();
		try {
			Log.v("res", res);
			boolean flag = false;
			if (type == 1) {
				flag = textProcess();
			} else if(type == 2) {
				flag = goodProcess();
			}
			msg.what = 200;
			msg.obj = flag;
		} catch (ClientProtocolException e) {
			msg.what = 404;
		} catch (IOException e) {
			msg.what = 100;
		}
		hand.sendMessage(msg);
	}

	private boolean textProcess() throws ClientProtocolException, IOException {
		boolean flag = false;
		String[] dates = res.split(",");
		for (String date : dates) {
			if (!dao.hasTextDate(date)) {
				url = Common.HTTPURL + "text_" + date + ".xml";
				Boolean result = myGet.doGetAndInsertData(url, date, reader,
						dao, 1);
				if (result) {
					flag = true;
				}
			}
		}
		return flag;
	}

	private boolean goodProcess() throws ClientProtocolException, IOException {
		boolean flag = false;
		String[] dates = res.split(",");
		for (String date : dates) {
			if (!dao.hasGoodDate(date.trim())) {
				url = Common.HTTPURL + "good_" + date.trim() + ".xml";
				Boolean result = myGet.doGetAndInsertData(url, date.trim(), reader,
						dao, 2);
				if (result) {
					flag = true;
				}
			}
		}
		return flag;
	}
}
