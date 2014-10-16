package com.jov.laughter.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jov.laughter.R;
import com.jov.laughter.TextDetailActivity;
import com.jov.laughter.bean.TextBean;
import com.jov.laughter.db.DBOpenHelper;
import com.jov.laughter.utils.Common;

public class TextListAdapter extends BaseAdapter {

	private List<TextBean> list;
	private Context ctx;
	private DBOpenHelper dbOpenHelper;

	public TextListAdapter(Context ctx, List<TextBean> list, DBOpenHelper db) {
		this.list = list;
		this.ctx = ctx;
		this.dbOpenHelper = db;
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
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		final int tid = list.get(position).getTid();
		if (tid != 0 && Common.isLongStr(list.get(position).getContent(), 500)) {
			hold.MainText.setText(Common.cutLongStr(list.get(position)
					.getContent(), 500));
			hold.more.setVisibility(View.VISIBLE);
		} else {
			hold.MainText.setText(list.get(position).getContent());
			hold.more.setVisibility(View.GONE);
		}
		if (!Common.isEmpty(list.get(position).getImagUrl())) {
			hold.more.setVisibility(View.VISIBLE);
		}
		if(tid==0){
			hold.itemIdText.setText(String.valueOf((list.size() - position)));
		}else{
			hold.itemIdText.setText(String.valueOf(tid));
		}
		hold.share.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Toast.makeText(ctx,
						ctx.getResources().getString(R.string.setting_share),
						Toast.LENGTH_SHORT).show();
			}
		});
		hold.fav.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dbOpenHelper.insertFavoriteWithTid(tid);
				Toast.makeText(ctx,
						ctx.getResources().getString(R.string.dofav_str),
						Toast.LENGTH_SHORT).show();
			}
		});

		hold.more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ctx, TextDetailActivity.class);
				intent.putExtra("text.tid", tid);
				intent.putExtra("flag.type", "text");
				ctx.startActivity(intent);
			}
		});
		return arg1;
	}

	static class Holder {
		TextView MainText;
		LinearLayout share;
		TextView itemIdText;
		LinearLayout fav;
		LinearLayout more;
	}
}
