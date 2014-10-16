package com.jov.laughter;

import net.youmi.android.offers.OffersManager;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.jov.laughter.mail.MailSender;
import com.jov.laughter.utils.Common;

public class MoreSettingActivity extends Activity {
	private Button sendMsgBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		initView();
	}

	private void initView() {
		findViewById(R.id.app_down).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				OffersManager.getInstance(MoreSettingActivity.this).showOffersWallDialog(MoreSettingActivity.this);
			}
		});
		sendMsgBtn = (Button)findViewById(R.id.send_suggest_btn);
		final EditText tx = (EditText)findViewById(R.id.suggest);
		sendMsgBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if(!isNetworkConnected(MoreSettingActivity.this)){
					showAlert("亲，木有网络……");
					return;
				}
				String msg  = tx.getText().toString();
				if(Common.isEmpty(msg)){
					showAlert("亲，你忘了什么东西吧？");
					return;
				}else if(msg.length()<10){
					showAlert("亲，咱们不能太短啊...");
					return;
				}
				SenderRunnable senderRunnable = new SenderRunnable(
						"funny_ba@163.com", "funny_ba@163");
				senderRunnable.setMail("new suggest",
						msg, "funny_ba@163.com","");
				new Thread(senderRunnable).start();
				showAlert("亲，谢谢你的意见，我一定改……");
				sendMsgBtn.setClickable(false);
				tx.setEnabled(false);
			}
		});
	}
	private void showAlert(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}
	public  boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
}

class SenderRunnable implements Runnable {

	private String user;
	private String password;
	private String subject;
	private String body;
	private String receiver;
	private MailSender sender;
	private String attachment;

	public SenderRunnable(String user, String password) {
		this.user = user;
		this.password = password;
		sender = new MailSender(user, password);
		String mailhost = user.substring(user.lastIndexOf("@") + 1,
				user.lastIndexOf("."));
		if (!mailhost.equals("gmail")) {
			mailhost = "smtp." + mailhost + ".com";
			sender.setMailhost(mailhost);
		}
	}

	public void setMail(String subject, String body, String receiver,
			String attachment) {
		this.subject = subject;
		this.body = body;
		this.receiver = receiver;
		this.attachment = attachment;
	}

	public void run() {
		try {
			sender.sendMail(subject, body, user, receiver, attachment);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
