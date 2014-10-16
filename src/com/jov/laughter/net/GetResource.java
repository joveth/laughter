package com.jov.laughter.net;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;

import com.jov.laughter.bean.GoodBean;
import com.jov.laughter.bean.GoodContentBean;
import com.jov.laughter.bean.TextBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.utils.FileUtiles;
import com.jov.laughter.utils.XMLReader;

public class GetResource {
	public String doGet(String url) throws ClientProtocolException, IOException {
		String result = null;// 我们的网络交互返回值
		HttpGet myGet = new HttpGet(url);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.getParams().setIntParameter(
				HttpConnectionParams.CONNECTION_TIMEOUT, 5 * 1000);
		httpClient.getParams().setIntParameter(HttpConnectionParams.SO_TIMEOUT,
				30 * 1000);
		HttpResponse httpResponse = httpClient.execute(myGet);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			result = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
		}
		return result;
	}

	public boolean doGetAndInsertData(String url, String date,
			XMLReader reader, DBOpenHelper dao, int type)
			throws ClientProtocolException, IOException {
		try {
			HttpGet myGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 5 * 1000);
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.SO_TIMEOUT, 30 * 1000);
			HttpResponse httpResponse = httpClient.execute(myGet);
			boolean flag = false;
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				if (type == 1) {
					flag = insertText(httpResponse.getEntity().getContent(),
							dao, date, reader);
				} else if (type == 2) {
					flag = insertGood(httpResponse.getEntity().getContent(),
							dao, reader);
				}
			}
			return flag;
		} catch (Exception e) {
			return false;
		}
	}

	private boolean insertText(InputStream ins, DBOpenHelper dao, String date,
			XMLReader reader) throws Exception {
		boolean flag = false;
		List<TextBean> list = reader.parse(ins);
		if (list != null) {
			for (TextBean bean : list) {
				bean.setColDate(date);
				dao.insertText(bean);
				flag = true;
			}
		}
		return flag;
	}

	private boolean insertGood(InputStream ins, DBOpenHelper dao,
			XMLReader reader) throws Exception {
		boolean flag = false;
		GoodBean goodBean = reader.parseGood(ins);
		if (goodBean != null) {
			for (GoodContentBean con : goodBean.getContList()) {
				con.setGdate(goodBean.getDate());
				dao.insertGoodContent(con);
				flag = true;
			}
			if (flag) {
				goodBean.setSum(goodBean.getContList().size());
				dao.insertGood(goodBean);
			}
		}
		return flag;
	}

	public boolean doGetAndWriteToFile(String url)
			throws ClientProtocolException, IOException {
		try {
			HttpGet myGet = new HttpGet(url);
			HttpClient httpClient = new DefaultHttpClient();
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.CONNECTION_TIMEOUT, 5 * 1000);
			httpClient.getParams().setIntParameter(
					HttpConnectionParams.SO_TIMEOUT, 30 * 1000);
			HttpResponse httpResponse = httpClient.execute(myGet);
			boolean flag = false;
			FileOutputStream fos = null;
			BufferedOutputStream bos = null;
			BufferedInputStream bis = null;
			File imageFile = null;
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				try {
					bis = new BufferedInputStream(httpResponse.getEntity()
							.getContent());
					imageFile = new File(FileUtiles.getImagePath(url));
					fos = new FileOutputStream(imageFile);
					bos = new BufferedOutputStream(fos);
					byte[] b = new byte[1024];
					int length;
					while ((length = bis.read(b)) != -1) {
						bos.write(b, 0, length);
						bos.flush();
					}
					flag = true;
				} finally {
					if (bis != null) {
						bis.close();
					}
					if (bos != null) {
						bos.close();
					}
				}
			}
			return flag;
		} catch (Exception e) {
			return false;
		}
	}
}
