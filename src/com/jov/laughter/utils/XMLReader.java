package com.jov.laughter.utils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

import com.jov.laughter.bean.GoodBean;
import com.jov.laughter.bean.GoodContentBean;
import com.jov.laughter.bean.TextBean;
import com.jov.laughter.bean.UpdateBean;

public class XMLReader {
	public List<TextBean> parse(InputStream is) throws Exception {
		if (is == null) {
			return null;
		}
		List<TextBean> list = null;
		TextBean obj = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				list = new ArrayList<TextBean>();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("item")) {
					obj = new TextBean();
				} else if (parser.getName().equals("content")) {
					eventType = parser.next();
					obj.setContent(parser.getText());
				} else if (parser.getName().equals("image")) {
					eventType = parser.next();
					obj.setImagUrl(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("item")) {
					list.add(obj);
					obj = null;
				}
				break;
			}
			eventType = parser.next();
		}
		return list;
	}

	public GoodBean parseGood(InputStream is) throws Exception {
		if (is == null) {
			return null;
		}
		GoodBean obj = null;
		List<GoodContentBean> conList = null;
		GoodContentBean content = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("subject")) {
					obj = new GoodBean();
					conList = new ArrayList<GoodContentBean>();
					eventType = parser.next();
					obj.setSubject(parser.getText());
				} else if (parser.getName().equals("ps")) {
					eventType = parser.next();
					obj.setTip(parser.getText());
				} else if (parser.getName().equals("date")) {
					eventType = parser.next();
					obj.setDate(parser.getText());
				} else if (parser.getName().equals("item")) {
					content = new GoodContentBean();
				} else if (parser.getName().equals("image")) {
					eventType = parser.next();
					content.setImage(parser.getText());
				} else if (parser.getName().equals("content")) {
					eventType = parser.next();
					content.setContent(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				if (parser.getName().equals("item")) {
					conList.add(content);
				} else if (parser.getName().equals("items")) {
					obj.setContList(conList);
					return obj;
				}
				break;
			}
			eventType = parser.next();
		}
		return obj;
	}
	public UpdateBean parseUpdate(InputStream is) throws Exception {
		if (is == null) {
			return null;
		}
		UpdateBean obj = null;
		XmlPullParser parser = Xml.newPullParser();
		parser.setInput(is, "UTF-8");
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				obj = new UpdateBean();
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().equals("version")) {
					eventType = parser.next();
					obj.setVersion(parser.getText());
				} else if (parser.getName().equals("name")) {
					eventType = parser.next();
					obj.setName(parser.getText());
				} else if (parser.getName().equals("describe")) {
					eventType = parser.next();
					obj.setDesc(parser.getText());
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		return obj;
	}
}
