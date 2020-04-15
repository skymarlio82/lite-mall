
package org.linlinjava.litemall.wx.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="litemall.wxpay")
public class MyWxPayProperties {

	private String appid;
	private String mchid;
	private String mchkey;
	private String notifyurl;
	private String keypath;
	private String ipaddress;

	public MyWxPayProperties() {
		
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	public String getMchkey() {
		return mchkey;
	}

	public void setMchkey(String mchkey) {
		this.mchkey = mchkey;
	}

	public String getNotifyurl() {
		return notifyurl;
	}

	public void setNotifyurl(String notifyurl) {
		this.notifyurl = notifyurl;
	}

	public String getKeypath() {
		return keypath;
	}

	public void setKeypath(String keypath) {
		this.keypath = keypath;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
}