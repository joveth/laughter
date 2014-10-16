package com.jov.laughter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jov.laughter.utils.FileUtiles;

public class ImageDetailActivity extends Activity {
	private ImageDetailActivity activity;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image_detail);
		activity = this;
		loadAddUI();
		ImageView img = (ImageView) this.findViewById(R.id.image_large);
		Bitmap map = null;
		Bundle bundle = this.getIntent().getExtras();
		if (bundle != null) {
			map = getLargeImage(bundle.getString("imgurl"));
			if (map != null) {
				img.setImageBitmap(map);
			}
		} else {
			activity.finish();
		}
		img.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
				activity.finish();
			}
		});
	}

	private void loadAddUI() {
		// AppConnect.getInstance(context).showBannerAd(context,view);
		// String value =
		// AdManager.getInstance(context).syncGetOnlineConfig("ad_a", "false");
		LinearLayout view = (LinearLayout) findViewById(R.id.ad_bar_item);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		view.addView(adView);
	}

	private Bitmap getLargeImage(String imageUrl) {
		File imageFile = new File(FileUtiles.getImagePath(imageUrl));
		if (!imageFile.exists()) {
			downloadImage(imageUrl, false);
		}
		if (imageUrl != null) {
			Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
			if (bitmap != null) {
				return bitmap;
			}
		}
		return null;
	}

	private void downloadImage(String imageUrl, boolean cutFlag) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Log.d("TAG", "monted sdcard");
		} else {
			Log.d("TAG", "has no sdcard");
		}
		HttpURLConnection con = null;
		FileOutputStream fos = null;
		BufferedOutputStream bos = null;
		BufferedInputStream bis = null;
		File imageFile = null;
		try {
			URL url = new URL(imageUrl);
			con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(5 * 1000);
			con.setReadTimeout(15 * 1000);
			int resCode = con.getResponseCode();
			if (resCode == 200) {
				bis = new BufferedInputStream(con.getInputStream());
				imageFile = new File(FileUtiles.getImagePath(imageUrl));
				fos = new FileOutputStream(imageFile);
				bos = new BufferedOutputStream(fos);
				byte[] b = new byte[1024];
				int length;
				while ((length = bis.read(b)) != -1) {
					bos.write(b, 0, length);
					bos.flush();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
				}
				if (bos != null) {
					bos.close();
				}
				if (con != null) {
					con.disconnect();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (imageFile != null) {
			return;
		}
	}
}
