package com.imaginea.resumereader.exceptions;

public enum ErrorCode {
	RESUME_DIR_EMPTY(201), INDEX_DIR_EMPTY(202);

	private final int number;

	ErrorCode(int number) {
		this.number = number;
	}

	public int getNumber() {
		return this.number;
	}

}
