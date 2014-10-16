package com.jov.laughter.bean;

import java.util.List;

public class GoodBean {
	private int gid;
	private String subject;
	private String date;
	private String tip;
	private int sum;
	private List<GoodContentBean> contList;

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public int getSum() {
		return sum;
	}

	public void setSum(int sum) {
		this.sum = sum;
	}

	public List<GoodContentBean> getContList() {
		return contList;
	}

	public void setContList(List<GoodContentBean> contList) {
		this.contList = contList;
	}
}
