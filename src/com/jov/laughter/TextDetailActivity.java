package com.jov.laughter;

import java.io.File;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.offers.OffersManager;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jov.laughter.bean.TextBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.net.HttpGetAndWriteToFileThread;
import com.jov.laughter.utils.Common;
import com.jov.laughter.utils.FileUtiles;
import com.jov.laughter.utils.ThreadPoolUtils;

public class TextDetailActivity extends Activity {
	private Intent intent;
	private TextView mainText;
	private ImageView imageView;
	private DBOpenHelper dbHelper;
	private TextBean bean;
	private int tid;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.text_listview_item);
		initData();
		initView();
	}

	private void initData() {
		intent = getIntent();
		type = intent.getStringExtra("flag.type");
		tid = intent.getIntExtra("text.tid", 0);
		if (tid == 0) {
			finish();
		}
		dbHelper = new DBOpenHelper(this);
		if ("good".equals(type)) {
			bean = dbHelper.getGoodContent(tid);
			if (bean == null) {
				finish();
			}
		} else {
			bean = dbHelper.getText(tid);
			if (bean == null) {
				finish();
			}
		}

	}

	private void initView() {
		mainText = (TextView) findViewById(R.id.Item_MainText);
		imageView = (ImageView) findViewById(R.id.Item_MainImg);
		imageView.setVisibility(View.VISIBLE);
		findViewById(R.id.Item_ctn).setVisibility(View.GONE);
		mainText.setText(bean.getContent());
		loadImage();
		findViewById(R.id.item_fav).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if ("good".equals(type)) {
					dbHelper.insertFavoriteWithCid(tid);
				} else {
					dbHelper.insertFavoriteWithTid(tid);
				}
				Toast.makeText(TextDetailActivity.this,
						getResources().getString(R.string.dofav_str),
						Toast.LENGTH_SHORT).show();
			}
		});
		LinearLayout adlayout = (LinearLayout) findViewById(R.id.ad_bar_item);
		AdView adView = new AdView(this, AdSize.FIT_SCREEN);
		adlayout.addView(adView);
		adlayout.setVisibility(View.VISIBLE);
		findViewById(R.id.item_share).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Toast.makeText(TextDetailActivity.this,
						getResources().getString(R.string.setting_share),
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void loadImage() {
		if (!Common.isEmpty(bean.getImagUrl())) {
			File imageFile = new File(
					FileUtiles.getImagePath(bean.getImagUrl()));
			if (!imageFile.exists()) {
				ThreadPoolUtils.execute(new HttpGetAndWriteToFileThread(
						resHand, bean.getImagUrl()));
			} else {
				Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				}
			}
		}else{
			imageView.setVisibility(View.GONE);
		}
	}

	private Handler resHand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
			} else if (msg.what == 100) {
			} else if (msg.what == 200) {
				boolean result = (boolean) msg.obj;
				if (result) {
					File imageFile = new File(FileUtiles.getImagePath(bean
							.getImagUrl()));
					if (imageFile.exists()) {
						Bitmap bitmap = BitmapFactory.decodeFile(imageFile
								.getPath());
						if (bitmap != null) {
							imageView.setImageBitmap(bitmap);
						}
					}
				}
			}
		};
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_fav:
			switchTo(FavoriteActivity.class);
			return true;
		case R.id.menu_setting:
			switchTo(MoreSettingActivity.class);
			return true;
		case R.id.menu_app:
			OffersManager.getInstance(TextDetailActivity.this).showOffersWallDialog(TextDetailActivity.this);
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
