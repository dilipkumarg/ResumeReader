package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;

import com.imaginea.resumereader.exceptions.MyPropertyException;

public interface Handler {
	void intialize(String[] args) throws IOException, MyPropertyException,
			ParseException,
			org.apache.lucene.queryparser.classic.ParseException;
}
