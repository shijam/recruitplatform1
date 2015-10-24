package flytv.run.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class AppInfo implements Serializable {

	private String gmtVersionString;
	private String leastSystem;
	private String leastVersionCode;
	private String programName;
	private String programUrl;
	private String versionDescription;
	private String versionName;

	private String name;
	private String appName;
	private int versionCode;
	private int major;
	private int minor;
	private int revision;
	private String versionType;
	private String deviceType;
	private String changeLogs;
	private String publishTime;
	private String appUrl;
	private String info;
	private String forced;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getRevision() {
		return revision;
	}

	public void setRevision(int revision) {
		this.revision = revision;
	}

	public String getVersionType() {
		return versionType;
	}

	public void setVersionType(String versionType) {
		this.versionType = versionType;
	}

	public String getChangeLogs() {
		return changeLogs;
	}

	public void setChangeLogs(String changeLogs) {
		this.changeLogs = changeLogs;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getForced() {
		return forced;
	}

	public void setForced(String forced) {
		this.forced = forced;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getGmtVersionString() {
		return gmtVersionString;
	}

	public void setGmtVersionString(String gmtVersionString) {
		this.gmtVersionString = gmtVersionString;
	}

	public String getLeastSystem() {
		return leastSystem;
	}

	public void setLeastSystem(String leastSystem) {
		this.leastSystem = leastSystem;
	}

	public String getLeastVersionCode() {
		return leastVersionCode;
	}

	public void setLeastVersionCode(String leastVersionCode) {
		this.leastVersionCode = leastVersionCode;
	}

	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getProgramUrl() {
		return programUrl;
	}

	public void setProgramUrl(String programUrl) {
		this.programUrl = programUrl;
	}

	public String getVersionDescription() {
		return versionDescription;
	}

	public void setVersionDescription(String versionDescription) {
		this.versionDescription = versionDescription;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

}
