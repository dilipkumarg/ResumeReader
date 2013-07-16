package com.imaginea.resumereader.handlers;

import java.io.IOException;
import java.text.ParseException;

import org.junit.Test;

import com.imaginea.resumereader.helpers.PropertyFileReader;

public class CommandModeHandlerTest {
	PropertyFileReader propReader;

	@Test(expected = IllegalArgumentException.class)
	public void testIntialize() throws IOException, ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String[] args = { "test" };
		CommandModeHandler handler = new CommandModeHandler(args);
		handler.intialize();
	}

	@Test
	public void testUpdateWithValidParams() {
		String[] args = { "update" };
		CommandModeHandler handler = null;
		try {
			handler = new CommandModeHandler(args);
		} catch (IOException e) {
			System.exit(0);
		}
		try {
			handler.intialize();
		} catch (IOException | ParseException
				| org.apache.lucene.queryparser.classic.ParseException e) {
			System.exit(0);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSearchWithInvalidParams() throws IOException,
			ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String[] args = { "search" };
		CommandModeHandler handler = new CommandModeHandler(args);
		handler.intialize();
	}

	@Test
	public void testSearchWithValidParams() {
		String[] args = { "search", "java" };
		CommandModeHandler handler = null;
		try {
			handler = new CommandModeHandler(args);
		} catch (IOException e) {
			System.exit(0);
		}
		try {
			handler.intialize();
		} catch (IOException | ParseException
				| org.apache.lucene.queryparser.classic.ParseException e) {
			System.exit(0);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testIndexDirWithInvalidParams() throws IOException,
			ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String[] args = { "indexdir" };
		CommandModeHandler handler = new CommandModeHandler(args);
		handler.intialize();
	}

	@Test
	public void testIndexDirWithValidParams() {
		String[] args = { "indexdir", "/home/dilip/resume/index" };
		CommandModeHandler handler = null;
		try {
			handler = new CommandModeHandler(args);
		} catch (IOException e) {
			System.exit(0);
		}
		try {
			handler.intialize();
		} catch (IOException | ParseException
				| org.apache.lucene.queryparser.classic.ParseException e) {
			System.exit(0);
		}
	}

	@Test(expected = IllegalArgumentException.class)
	public void testResumeDirWithInvalidParams() throws IOException,
			ParseException,
			org.apache.lucene.queryparser.classic.ParseException {
		String[] args = { "resumedir" };
		CommandModeHandler handler = new CommandModeHandler(args);
		handler.intialize();
	}

	@Test
	public void testResumeDirWithValidParams() {
		String[] args = { "resumedir", "/home/dilip/resume" };
		CommandModeHandler handler = null;
		try {
			handler = new CommandModeHandler(args);
		} catch (IOException e) {
			System.exit(0);
		}
		try {
			handler.intialize();
		} catch (IOException | ParseException
				| org.apache.lucene.queryparser.classic.ParseException e) {
			System.exit(0);
		}
	}
}
