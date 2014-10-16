package com.jov.laughter.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jov.laughter.R;
import com.jov.laughter.bean.FavoriteBean;
import com.jov.laughter.utils.Common;
import com.jov.laughter.utils.FileUtiles;

public class FavoriteListAdapter extends BaseAdapter {

	private List<FavoriteBean> list;
	private Context ctx;

	public FavoriteListAdapter(Context ctx, List<FavoriteBean> list) {
		this.list = list;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View arg1, ViewGroup arg2) {
		final Holder hold;
		if (arg1 == null) {
			hold = new Holder();
			arg1 = View.inflate(ctx, R.layout.text_listview_item, null);
			hold.MainText = (TextView) arg1.findViewById(R.id.Item_MainText);
			hold.itemIdText = (TextView) arg1.findViewById(R.id.item_id);
			hold.share = (LinearLayout) arg1.findViewById(R.id.item_share);
			hold.fav = (LinearLayout) arg1.findViewById(R.id.item_fav);
			hold.more = (LinearLayout) arg1.findViewById(R.id.item_more);
			hold.imageView = (ImageView) arg1.findViewById(R.id.Item_MainImg);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		boolean hasImage = false;
		boolean hasText  = false;
		hold.itemIdText.setText(String.valueOf(list.get(position).getFid()));
		if (!Common.isEmpty(list.get(position).getImgurl())) {
			hasImage = true;
			File imageFile = new File(FileUtiles.getImagePath(list
					.get(position).getImgurl()));
			if (imageFile.exists()) {
				Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getPath());
				if (bitmap != null) {
					hold.imageView.setImageBitmap(bitmap);
				}
			}
		} else if (list.get(position).getTextBean() != null) {
			String img = list.get(position).getTextBean().getImagUrl();
			hasText = true;
			if (!Common.isEmpty(img)) {
				hasImage = true;
				File imageFile = new File(FileUtiles.getImagePath(img));
				if (imageFile.exists()) {
					Bitmap bitmap = BitmapFactory.decodeFile(imageFile
							.getPath());
					if (bitmap != null) {
						hold.imageView.setImageBitmap(bitmap);
					}
				}
			}
			hold.MainText
					.setText(list.get(position).getTextBean().getContent());
		}
		hold.more.setVisibility(View.GONE);
		hold.fav.setVisibility(View.GONE);
		hold.share.setVisibility(View.GONE);
		if (hasImage) {
			hold.imageView.setVisibility(View.VISIBLE);
		}else{
			hold.imageView.setVisibility(View.GONE);
		}
		if(hasText){
			hold.MainText.setVisibility(View.VISIBLE);
		}else{
			hold.MainText.setVisibility(View.GONE);
		}
		return arg1;
	}

	static class Holder {
		TextView MainText;
		LinearLayout share;
		TextView itemIdText;
		LinearLayout fav;
		LinearLayout more;
		ImageView imageView;
	}
}
