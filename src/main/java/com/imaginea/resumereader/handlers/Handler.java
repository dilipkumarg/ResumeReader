package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;

public interface Handler {
	void intialize(String[] args) throws IOException, MyPropertyFieldException,
			ParseException,
			org.apache.lucene.queryparser.classic.ParseException;
}
