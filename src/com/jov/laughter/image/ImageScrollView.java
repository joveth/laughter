package com.jov.laughter.image;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.jov.laughter.ImageDetailActivity;
import com.jov.laughter.R;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.net.HttpGetThread;
import com.jov.laughter.utils.Common;
import com.jov.laughter.utils.FileUtiles;
import com.jov.laughter.utils.ThreadPoolUtils;

/**
 * 自定义的ScrollView，在其中动态地对图片进行添加。
 * 
 * @author guolin
 */
public class ImageScrollView extends ScrollView implements OnTouchListener {
	private Context context;
	/**
	 * 每页要加载的图片数量
	 */
	public static final int PAGE_SIZE = 15;

	/**
	 * 记录当前已加载到第几页
	 */
	private int page;

	/**
	 * 每一列的宽度
	 */
	private int columnWidth;

	/**
	 * 当前第一列的高度
	 */
	private int firstColumnHeight;

	/**
	 * 当前第二列的高度
	 */
	private int secondColumnHeight;

	/**
	 * 当前第三列的高度
	 */
	// private int thirdColumnHeight;

	/**
	 * 是否已加载过一次layout，这里onLayout中的初始化只需加载一次
	 */
	private boolean loadOnce;

	/**
	 * 对图片进行管理的工具类
	 */
	private ImageLoader imageLoader;

	/**
	 * 第一列的布局
	 */
	private LinearLayout firstColumn;

	/**
	 * 第二列的布局
	 */
	private LinearLayout secondColumn;

	/**
	 * 第三列的布局
	 */

	/**
	 * 记录所有正在下载或等待下载的任务。
	 */
	private static Set<LoadImageTask> taskCollection;

	/**
	 * MyScrollView下的直接子布局。
	 */
	private static View scrollLayout;
	private static View progress;
	/**
	 * MyScrollView布局的高度。
	 */
	private static int scrollViewHeight;

	/**
	 * 记录上垂直方向的滚动距离。
	 */
	private static int lastScrollY = -1;

	/**
	 * 记录所有界面上的图片，用以可以随时控制对图片的释放。
	 */
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	private static List<String> imageUrlResource = new ArrayList<String>();
	private static DBOpenHelper dao;
	/**
	 * 在Handler中进行图片可见性检查的判断，以及加载更多图片的操作。
	 */
	private static Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			ImageScrollView myScrollView = (ImageScrollView) msg.obj;
			int scrollY = myScrollView.getScrollY();
			// 如果当前的滚动位置和上次相同，表示已停止滚动
			if (scrollY == lastScrollY) {
				// 当滚动的最底部，并且当前没有正在下载的任务时，开始加载下一页的图片
				if (scrollViewHeight + scrollY >= scrollLayout.getHeight()
						&& taskCollection.isEmpty()) {
					myScrollView.loadMoreImages();
				}
				myScrollView.checkVisibility();
			} else {
				lastScrollY = scrollY;
				Message message = new Message();
				message.obj = myScrollView;
				// 5毫秒后再次对滚动位置进行判断
				handler.sendMessageDelayed(message, 5);
			}
		};

	};

	/**
	 * MyScrollView的构造函数。
	 * 
	 * @param context
	 * @param attrs
	 */
	public ImageScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		imageLoader = ImageLoader.getInstance();
		taskCollection = new HashSet<LoadImageTask>();
		dao = new DBOpenHelper(context);
		setOnTouchListener(this);
	}

	/**
	 * 进行一些关键性的初始化操作，获取MyScrollView的高度，以及得到第一列的宽度值。并在这里开始加载第一页的图片。
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		if (changed && !loadOnce) {
			scrollViewHeight = getHeight();
			scrollLayout = getChildAt(0);
			firstColumn = (LinearLayout) findViewById(R.id.first_column);
			secondColumn = (LinearLayout) findViewById(R.id.second_column);
			progress = findViewById(R.id.img_progress);
			// thirdColumn = (LinearLayout) findViewById(R.id.third_column);
			columnWidth = firstColumn.getWidth();
			loadOnce = true;
			List<String> localDB = dao.getImageUrls();
			imageUrlResource.addAll(localDB);
			imageUrlResource.addAll(Arrays.asList(Images.imageUrls));
			loadImageResource();
		}
	}

	/**
	 * 网络资源处理
	 */
	private Handler hand = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
			} else if (msg.what == 100) {
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				String[] imgs = result.split("\n");
				for (String str : imgs) {
					if (!Common.isEmpty(str)&&!imageUrlResource.contains(str)) {
						imageUrlResource.add(0, str);
						dao.insertImage(str);
					}
				}
			}
			loadMoreImages();
		}
	};

	/**
	 * 网络资源同步
	 */
	private void loadImageResource() {
		ThreadPoolUtils.execute(new HttpGetThread(hand, Common.HTTPURL
				+ "resource.txt"));
	}

	/**
	 * 监听用户的触屏事件，如果用户手指离开屏幕则开始进行滚动检测。
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_UP) {
			Message message = new Message();
			message.obj = this;
			handler.sendMessageDelayed(message, 5);
		}
		return false;
	}

	/**
	 * 开始加载下一页的图片，每张图片都会开启一个异步线程去下载。
	 */
	public void loadMoreImages() {
		progress.setVisibility(View.GONE);
		if (hasSDCard()) {
			int startIndex = page * PAGE_SIZE;
			int endIndex = page * PAGE_SIZE + PAGE_SIZE;
			if (startIndex < imageUrlResource.size()) {
				if (endIndex > imageUrlResource.size()) {
					endIndex = imageUrlResource.size();
				}
				for (int i = startIndex; i < endIndex; i++) {
					LoadImageTask task = new LoadImageTask();
					taskCollection.add(task);
					task.execute(imageUrlResource.get(i));
				}
				page++;
			} else {

			}
		} else {
			Toast.makeText(getContext(), "未发现SD卡", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 遍历imageViewList中的每张图片，对图片的可见性进行检查，如果图片已经离开屏幕可见范围，则将图片替换成一张空图。
	 */
	public void checkVisibility() {
		for (int i = 0; i < imageViewList.size(); i++) {
			ImageView imageView = imageViewList.get(i);
			int borderTop = (Integer) imageView.getTag(R.string.border_top);
			int borderBottom = (Integer) imageView
					.getTag(R.string.border_bottom);
			if (borderBottom > getScrollY()
					&& borderTop < getScrollY() + scrollViewHeight) {
				String imageUrl = (String) imageView.getTag(R.string.image_url);
				Bitmap bitmap = imageLoader.getBitmapFromMemoryCache(imageUrl);
				if (bitmap != null) {
					imageView.setImageBitmap(bitmap);
				} else {
					LoadImageTask task = new LoadImageTask(imageView);
					task.execute(imageUrl);
				}
			} else {
				imageView.setImageResource(R.drawable.empty_photo);
			}
		}
	}

	/**
	 * 判断手机是否有SD卡。
	 * 
	 * @return 有SD卡返回true，没有返回false。
	 */
	private boolean hasSDCard() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}
	private void loadAddUI(Context context,LinearLayout view){
		//AppConnect.getInstance(context).showBannerAd(context,view);
		//String value = AdManager.getInstance(context).syncGetOnlineConfig("ad_a", "false");
			AdView adView = new AdView(context, AdSize.FIT_SCREEN);
			view.addView(adView);
	}
	/**
	 * 异步下载图片的任务。
	 * 
	 * @author guolin
	 */
	class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

		/**
		 * 图片的URL地址
		 */
		private String mImageUrl;

		/**
		 * 可重复使用的ImageView
		 */
		private ImageView mImageView;

		public LoadImageTask() {
		}

		/**
		 * 将可重复使用的ImageView传入
		 * 
		 * @param imageView
		 */
		public LoadImageTask(ImageView imageView) {
			mImageView = imageView;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			mImageUrl = params[0];
			Bitmap imageBitmap = imageLoader
					.getBitmapFromMemoryCache(mImageUrl);
			if (imageBitmap == null) {
				imageBitmap = loadImage(mImageUrl);
			}
			return imageBitmap;
		}

		@Override
		protected void onPostExecute(Bitmap bitmap) {
			if (bitmap != null) {
				double ratio = bitmap.getWidth() / (columnWidth * 1.0);
				int scaledHeight = (int) (bitmap.getHeight() / ratio);
				addImage(bitmap, columnWidth, scaledHeight);
			}
			taskCollection.remove(this);
		}

		/**
		 * 根据传入的URL，对图片进行加载。如果这张图片已经存在于SD卡中，则直接从SD卡里读取，否则就从网络上下载。
		 * 
		 * @param imageUrl
		 *            图片的URL地址
		 * @return 加载到内存的图片。
		 */
		private Bitmap loadImage(String imageUrl) {
			File imageFile = new File(FileUtiles.getImagePath(imageUrl));
			if (!imageFile.exists()) {
				downloadImage(imageUrl, true);
			}
			if (imageUrl != null) {
				Bitmap bitmap = ImageLoader.decodeSampledBitmapFromResource(
						imageFile.getPath(), columnWidth);
				if (bitmap != null) {
					imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					return bitmap;
				}
			}
			return null;
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

		/**
		 * 向ImageView中添加一张图片
		 * 
		 * @param bitmap
		 *            待添加的图片
		 * @param imageWidth
		 *            图片的宽度
		 * @param imageHeight
		 *            图片的高度
		 */
		private void addImage(final Bitmap bitmap, int imageWidth, int imageHeight) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					imageWidth, imageHeight);
			if (mImageView != null) {
				mImageView.setImageBitmap(bitmap);
			} else {
				ImageView imageView = new ImageView(getContext());
				imageView.setLayoutParams(params);
				imageView.setImageBitmap(bitmap);
				imageView.setScaleType(ScaleType.FIT_XY);
				imageView.setPadding(5, 5, 5, 5);
				imageView.setTag(R.string.image_url, mImageUrl);
				findColumnToAdd(imageView, imageHeight).addView(imageView);
				imageView.setOnLongClickListener(new OnLongClickListener() {
					@Override
					public boolean onLongClick(View arg0) {/*
						LayoutInflater inflater = LayoutInflater.from(getContext());
						View imgEntryView = inflater.inflate(
								R.layout.dialog_photo, null); // 加载自定义的布局文件
						final AlertDialog dialog = new AlertDialog.Builder(
								context).create();
						LinearLayout adlayout = (LinearLayout)imgEntryView.findViewById(R.id.ad_bar_da);
						loadAddUI(imgEntryView.getContext(),adlayout);
						ImageView img = (ImageView) imgEntryView
								.findViewById(R.id.large_image);
						Bitmap map = getLargeImage(mImageUrl);
						if(map!=null){
							img.setImageBitmap(map);
							img.setScaleType(ScaleType.FIT_XY);
						}
						WindowManager.LayoutParams lp=dialog.getWindow().getAttributes();
						lp.dimAmount=0.85f;
						dialog.getWindow().setAttributes(lp);
						dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
						dialog.setView(imgEntryView); // 自定义dialog
						dialog.show();
						// 点击布局文件（也可以理解为点击大图）后关闭dialog，这里的dialog不需要按钮
						imgEntryView.setOnClickListener(new OnClickListener() {
							public void onClick(View paramView) {
								dialog.cancel();
							}
						});
						imgEntryView.findViewById(R.id.to_fav).setOnClickListener(new OnClickListener() {
							public void onClick(View paramView) {
								dao.insertFavoriteWithImage(mImageUrl);
								Toast.makeText(context, getResources().getString(R.string.dofav_str), Toast.LENGTH_SHORT).show();
								dialog.cancel();
							}
						} );
						imgEntryView.findViewById(R.id.to_close).setOnClickListener(new OnClickListener() {
							public void onClick(View paramView) {
								dialog.cancel();
							}
						} );
						return true;*/
						Intent intent = new Intent(context,ImageDetailActivity.class);
						intent.putExtra("imgurl", mImageUrl);
						context.startActivity(intent);
						return true;
					}
				});
				imageViewList.add(imageView);
			}
		}

		/**
		 * 找到此时应该添加图片的一列。原则就是对三列的高度进行判断，当前高度最小的一列就是应该添加的一列。
		 * 
		 * @param imageView
		 * @param imageHeight
		 * @return 应该添加图片的一列
		 */
		private LinearLayout findColumnToAdd(ImageView imageView,
				int imageHeight) {
			if (firstColumnHeight <= secondColumnHeight) {
				imageView.setTag(R.string.border_top, firstColumnHeight);
				firstColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, firstColumnHeight);
				return firstColumn;
				/*
				 * imageView.setTag(R.string.border_top, thirdColumnHeight);
				 * thirdColumnHeight += imageHeight;
				 * imageView.setTag(R.string.border_bottom, thirdColumnHeight);
				 * return thirdColumn;
				 */
			} else {
				imageView.setTag(R.string.border_top, secondColumnHeight);
				secondColumnHeight += imageHeight;
				imageView.setTag(R.string.border_bottom, secondColumnHeight);
				return secondColumn;
				/*
				 * imageView.setTag(R.string.border_top, thirdColumnHeight);
				 * thirdColumnHeight += imageHeight;
				 * imageView.setTag(R.string.border_bottom, thirdColumnHeight);
				 * return thirdColumn;
				 */
			}
		}

		/**
		 * 将图片下载到SD卡缓存起来。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 */
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
				if (cutFlag) {
					Bitmap bitmap = ImageLoader
							.decodeSampledBitmapFromResource(
									imageFile.getPath(), columnWidth);
					if (bitmap != null) {
						imageLoader.addBitmapToMemoryCache(imageUrl, bitmap);
					}
				} else {
					return;
				}
			}
		}

		/**
		 * 获取图片的本地存储路径。
		 * 
		 * @param imageUrl
		 *            图片的URL地址。
		 * @return 图片的本地存储路径。
		 */
		
	}
}