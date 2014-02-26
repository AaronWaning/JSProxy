package com.server;

import java.util.ArrayList;
import java.util.Collections;

import proguard.annotation.Keep;
import proguard.annotation.KeepName;

/**
 * Proxy�ĵ�ǰ�汾
 */
@Keep
@KeepName

public class Version {

	final private static ArrayList<IVersion> verList;
	
	static {
		verList = new ArrayList<IVersion>();
		
		VersionImpl ver = null; 
		ver = new VersionImpl("V1.0.2.201310151507", "beta");
		ver.addDescription("Proxy����1.0.2�汾");
		verList.add(ver);
		

		// ����
		Collections.sort(verList);
	}

	public static void main(String[] args) {
		System.out.print(getVersion());
	}

	/**
	 * ������ǰ�汾�ķ�����Ϣ
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
			//�����ϰ汾��Ϣ
			int nVerCount = verList.size();
			for (int i = 0; i < nVerCount; i++) {
				IVersion v = verList.get(i);
				content.append(v.getVersion() + " " + v.getVersionType());
				if( i == 0 ){
					content.append("(*��ǰ�汾*)");
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

		// ��������
		return content.toString();
	}
	
	/**
	 * ��ȡ��ǰ�汾��Ϣ
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String getVersion() {
		return verList.get(0).getVersion();
	}

	/**
	 * ��õ�ǰ�汾���޸�����
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
	 * ��ȡ��ǰ�汾��Ϣ
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String getVersion(int index) {
		return verList.get(index).getVersion();
	}

	/**
	 * ��õ�ǰ�汾���޸�����
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String[] getVersionDescription(int index) {
		return verList.get(index).getVersionDescription();
	}

	/**
	 * ��ȡ��ǰ�汾��Ϣ
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String getVersionType(int index) {
		return verList.get(index).getVersionType();
	}

	/**
	 * ��õ�ǰ�汾���޸�����
	 * 
	 * @return
	 */
	@Keep
	@KeepName
	public static String[] getVersionErrors(int index) {
		return verList.get(index).getVersionErrors();
	}
}
