package com.idcq.appserver.dto.heartbeat;

import java.io.Serializable;
import java.util.Date;

public class HeartBeatDto implements Serializable{
	private static final long serialVersionUID = -2767043222846820840L;
	private long id;
	private String gwId;	//网关ID
	private int sysUpTime;	//系统升级次数
	private int sysMemFree;	//系统空闲内存
	private double sysLoad;	//系统负载量
	private int wifidogUpTime;//wifidog升级次数
	private int checkTime;	//检查周期
	private String gwMac;	//网关mac
	private String wanIp;	//wan口IP
	private int clientCount;//连入路由器用户数
	private String gwAddress;//路由器网关地址
	private String router_type;//路由器型号
	private String sv;		//路由器固件版本
	private Date time;		//时间戳
	
	public HeartBeatDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getGwId() {
		return gwId;
	}
	public void setGwId(String gwId) {
		this.gwId = gwId;
	}
	public int getSysUpTime() {
		return sysUpTime;
	}
	public void setSysUpTime(int sysUpTime) {
		this.sysUpTime = sysUpTime;
	}
	public int getSysMemFree() {
		return sysMemFree;
	}
	public void setSysMemFree(int sysMemFree) {
		this.sysMemFree = sysMemFree;
	}
	public double getSysLoad() {
		return sysLoad;
	}
	public void setSysLoad(double sysLoad) {
		this.sysLoad = sysLoad;
	}
	public int getWifidogUpTime() {
		return wifidogUpTime;
	}
	public void setWifidogUpTime(int wifidogUpTime) {
		this.wifidogUpTime = wifidogUpTime;
	}
	public int getCheckTime() {
		return checkTime;
	}
	public void setCheckTime(int checkTime) {
		this.checkTime = checkTime;
	}
	public String getGwMac() {
		return gwMac;
	}
	public void setGwMac(String gwMac) {
		this.gwMac = gwMac;
	}
	public String getWanIp() {
		return wanIp;
	}
	public void setWanIp(String wanIp) {
		this.wanIp = wanIp;
	}
	public int getClientCount() {
		return clientCount;
	}
	public void setClientCount(int clientCount) {
		this.clientCount = clientCount;
	}
	public String getGwAddress() {
		return gwAddress;
	}
	public void setGwAddress(String gwAddress) {
		this.gwAddress = gwAddress;
	}
	public String getRouter_type() {
		return router_type;
	}
	public void setRouter_type(String router_type) {
		this.router_type = router_type;
	}
	public String getSv() {
		return sv;
	}
	public void setSv(String sv) {
		this.sv = sv;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
}
