package com.server.proxy;

public class ProxyException extends Exception {
	private static final long serialVersionUID = -7093894807861078364L;
	private final int errorCode;

	public ProxyException(int errorCode) {
		this.errorCode = errorCode;
	}

	public ProxyException(int errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}

	public ProxyException(int errorCode, Throwable cause) {
		super(cause);
		this.errorCode = errorCode;
	}

	public ProxyException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public int getErrorCode() {
		return this.errorCode;
	}
}