package com.jov.laughter.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jov.laughter.GoodListActivity;
import com.jov.laughter.R;
import com.jov.laughter.bean.GoodBean;
import com.jov.laughter.utils.Common;

public class GoodListAdapter extends BaseAdapter {

	private List<GoodBean> list;
	private Context ctx;

	public GoodListAdapter(Context ctx, List<GoodBean> list) {
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
			arg1 = View.inflate(ctx, R.layout.good_listview_item, null);
			hold.mainText = (TextView) arg1.findViewById(R.id.good_subj);
			hold.tipText = (TextView) arg1.findViewById(R.id.good_tip);
			hold.sumText = (TextView) arg1.findViewById(R.id.good_sum);
			hold.mainCont = (RelativeLayout) arg1
					.findViewById(R.id.main_content);
			arg1.setTag(hold);
		} else {
			hold = (Holder) arg1.getTag();
		}
		hold.mainText.setText(Common.cutLongStr(
				list.get(position).getSubject(), 20));
		hold.tipText
				.setText(Common.cutLongStr(list.get(position).getTip(), 30));
		hold.sumText.setText(String.valueOf(list.get(position).getSum()));
		hold.mainCont.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(ctx, GoodListActivity.class);
				intent.putExtra("good.date", list.get(position).getDate());
				intent.putExtra("good.subject", list.get(position).getSubject());
				ctx.startActivity(intent);
			}
		});
		return arg1;
	}

	static class Holder {
		TextView mainText;
		TextView tipText;
		TextView sumText;
		RelativeLayout mainCont;
	}
}
