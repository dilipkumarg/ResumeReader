package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;

public abstract class Handler {
	protected String[] args;

	Handler(String[] args) {
		this.args = args;
	}

	public abstract void intialize() throws IOException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException;
}
