package com.jov.laughter.bean;

public class FavoriteBean {
	private int fid;
	private String imgurl;
	private int tid;
	private TextBean textBean;
	private int cid;

	public int getFid() {
		return fid;
	}

	public void setFid(int fid) {
		this.fid = fid;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public TextBean getTextBean() {
		return textBean;
	}

	public void setTextBean(TextBean textBean) {
		this.textBean = textBean;
	}
}
