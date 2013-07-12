package com.imaginea.resumereader;

import java.io.IOException;
import java.text.ParseException;

import com.imaginea.resumereader.exceptions.MyPropertyFieldException;
import com.imaginea.resumereader.handlers.CommandModeHandler;
import com.imaginea.resumereader.handlers.Handler;
import com.imaginea.resumereader.handlers.NormalModeHandler;

public class MainClass {

	public static void main(String[] args) throws IOException,
			MyPropertyFieldException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		Handler handler;
		if (args.length < 1) {
			handler = new NormalModeHandler();
		} else {
			handler = new CommandModeHandler();
		}
		handler.intialize(args);
	}
}
