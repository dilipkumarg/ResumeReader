package com.imaginea.resumereader.exceptions;

public class MyPropertyFieldException extends Exception {
	private static final long serialVersionUID = 1L;
	private ErrorCode errorCode;

	public MyPropertyFieldException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public MyPropertyFieldException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public MyPropertyFieldException(Throwable cause, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
	}

	public MyPropertyFieldException(String message, Throwable cause,
			ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public MyPropertyFieldException setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
		return this;
	}

}
