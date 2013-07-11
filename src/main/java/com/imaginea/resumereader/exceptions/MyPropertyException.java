package com.imaginea.resumereader.exceptions;

public class MyPropertyException extends Exception {
	private static final long serialVersionUID = 1L;
	private ErrorCode errorCode;

	public MyPropertyException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public MyPropertyException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public MyPropertyException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public MyPropertyException(String message, Throwable cause,
			ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public MyPropertyException setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
		return this;
	}

}
