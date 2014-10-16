package com.jov.laughter.net;


import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 网络Get请求的线程
 * */
public class HttpGetThread implements Runnable {

	private Handler hand;
	private String url;
	private GetResource myGet = new GetResource();

	public HttpGetThread(Handler hand, String res) {
		this.hand = hand;
		// 拼接访问服务器完整的地址
		url =  res;
	}

	@Override
	public void run() {
		// 获取我们回调主ui的message
		Message msg = hand.obtainMessage();
		Log.e("jov", url);
		try {
			String result = myGet.doGet(url);
			msg.what = 200;
			msg.obj = result;
		} catch (ClientProtocolException e) {
			msg.what = 404;
		} catch (IOException e) {
			msg.what = 100;
		}
		// 给主ui发送消息传递数据
		hand.sendMessage(msg);
	}
}
