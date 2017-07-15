package com.jpay.demo.entity;

public class Order {
	private String appId;
	private String body;
	private String subject;
	private String deviceInfo;//可选 设备ID
	private String nofityUrl;
	private String paraTradeNo;
	private String paraId;
	private int totalFee;//单位 元
	private String attach;
	private String channelId;
	
	
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDeviceInfo() {
		return deviceInfo;
	}
	public void setDeviceInfo(String deviceInfo) {
		this.deviceInfo = deviceInfo;
	}
	public String getNofityUrl() {
		return nofityUrl;
	}
	public void setNofityUrl(String nofityUrl) {
		this.nofityUrl = nofityUrl;
	}
	public String getParaTradeNo() {
		return paraTradeNo;
	}
	public void setParaTradeNo(String paraTradeNo) {
		this.paraTradeNo = paraTradeNo;
	}
	public String getParaId() {
		return paraId;
	}
	public void setParaId(String paraId) {
		this.paraId = paraId;
	}
	public int getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(int totalFee) {
		this.totalFee = totalFee;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
}