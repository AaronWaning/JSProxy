package com.server;

import java.util.ArrayList;
import java.util.Collections;

import proguard.annotation.Keep;
import proguard.annotation.KeepName;

/**
 * Proxy的当前版本
 */
@Keep
@KeepName

public class Version {

	final private static ArrayList<IVersion> verList;
	
	static {
		verList = new ArrayList<IVersion>();
		
		VersionImpl ver = null; 
		ver = new VersionImpl("V1.0.2.201310151507", "beta");
		ver.addDescription("Proxy发布1.0.2版本");
		verList.add(ver);
		

		// 排序
		Collections.sort(verList);
	}

	public static void main(String[] args) {
		System.out.print(getVersion());
	}

	/**
	 * 产生当前版本的发布信息
	 * @param releaseFile
	 * @return
	 */
	@Keep
	@KeepName
	public static String getReleaseInfo(Boolean onlyCurrentVersion) {
		StringBuffer content = new StringBuffer("");

		if( onlyCurrentVersion ){
			IVersion v = verList.get(0);
			content.append(v.getVersion() + " " + v.getVersionType());
			
			int nVerDespCount = v.getVersionDescription().length;
			for (int j = 0; j < nVerDespCount ; j++) {
				content.append("[").append(j + 1).append("]");
				content.append(v.getVersionDescription()[j]);
			}
		}else{
			//附加上版本信息
			int nVerCount = verList.size();
			for (int i = 0; i < nVerCount; i++) {
				IVersion v = verList.get(i);
				content.append(v.getVersion() + " " + v.getVersionType());
				if( i == 0 ){
					content.append("(*当前版本*)");
				}
				content.append("\r\n");
				
				int nVerDespCount = v.getVersionDescription().length;
				for (int j = 0; j < nVerDespCount ; j++) {
					content.append("[").append(j + 1).append("]");
					content.append(v.getVersionDescription()[j]);
					content.append("\r\n");
				}
				if(i != nVerCount-1)content.append("\r\n\r\n");
			}
		}

		// 返回数据
		return content.toString();
	}
	
	/**
	 * 获取当前版本信息
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String getVersion() {
		return verList.get(0).getVersion();
	}

	/**
	 * 获得当前版本的修改内容
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String[] getVersionDescription() {
		return verList.get(0).getVersionDescription();
	}
	@Keep
	@KeepName
	public static int getVersionCount() {
		return verList.size();
	}

	/**
	 * 获取当前版本信息
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String getVersion(int index) {
		return verList.get(index).getVersion();
	}

	/**
	 * 获得当前版本的修改内容
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String[] getVersionDescription(int index) {
		return verList.get(index).getVersionDescription();
	}

	/**
	 * 获取当前版本信息
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String getVersionType(int index) {
		return verList.get(index).getVersionType();
	}

	/**
	 * 获得当前版本的修改内容
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String[] getVersionErrors(int index) {
		return verList.get(index).getVersionErrors();
	}
}
