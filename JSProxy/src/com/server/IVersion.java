package com.server;

public interface IVersion extends Comparable<IVersion> {
	/**
	 * 获取当前版本信息
	 * 
	 * @return
	 */
	public String getVersion();

	/**
	 * 版本类型
	 * 
	 * @return
	 */
	public String getVersionType();

	/**
	 * 获得当前版本的修改内容
	 * 
	 * @return
	 */
	public String[] getVersionDescription();

	/**
	 * 遗留问题
	 */
	public String[] getVersionErrors();
}
