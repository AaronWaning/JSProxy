package com.server;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 版本对象的实现
 */
class VersionImpl implements IVersion {

	final String versionName;
	final String versionType;
	final ArrayList<String> versionDescs = new ArrayList<String>();
	final ArrayList<String> versionErrs = new ArrayList<String>();

	VersionImpl(String versionName, String versionType) {
		this.versionName = versionName;
		this.versionType = versionType;
	}

	public boolean addDescription(String descriptoin) {
		return versionDescs.add(descriptoin);
	}

	public boolean addError(String err) {
		return versionErrs.add(err);
	}

	public String getVersion() {
		return versionName;
	}

	public String[] getVersionDescription() {
		return versionDescs.toArray(new String[0]);
	}

	/**
	 * 按照字符串比较
	 */
	public int compareTo(IVersion v) {
		String me = this.getVersion();
		String he = v.getVersion();
		
		me = normal(me);
		he = normal(he);
		
		return he.compareToIgnoreCase(me);
	}

	/**
	 * 将 1到9 转化为 01到09
	 * @param me
	 * @return
	 */
	protected String normal(String me) {
		Pattern p = Pattern.compile("\\.(\\d)\\.");
		Matcher m = p.matcher(me);
		while( m.find() ){
			me = me.replace("."+m.group(1)+".",".0"+m.group(1)+".");
			m = p.matcher(me);
		}
		return me;
	}


	public String[] getVersionErrors() {
		return versionErrs.toArray(new String[0]);
	}

	public String getVersionType() {
		return this.versionType;
	}

}
