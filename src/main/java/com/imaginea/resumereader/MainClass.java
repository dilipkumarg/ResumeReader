package com.imaginea.resumereader;

import java.io.IOException;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imaginea.resumereader.exceptions.FileDirectoryEmptyException;
import com.imaginea.resumereader.exceptions.IndexDirectoryEmptyException;
import com.imaginea.resumereader.handlers.CommandModeHandler;
import com.imaginea.resumereader.handlers.Handler;
import com.imaginea.resumereader.handlers.NormalModeHandler;

public class MainClass {
	private static final Logger LOGGER = Logger.getLogger(MainClass.class
			.getName());

	public static void main(String[] args) throws Exception {
		Handler handler;
		if (args.length < 1) {
			handler = new NormalModeHandler(args);
		} else {
			handler = new CommandModeHandler(args);
		}
		try {
			handler.intialize();
		} catch (IllegalArgumentException iae) {
			LOGGER.log(
					Level.SEVERE,
					"IllegalArgumentException occured,\n Error:"
							+ iae.getMessage());
			throw iae;
		} catch (FileDirectoryEmptyException fde) {
			LOGGER.log(
					Level.SEVERE,
					"FileDirectoryEmptyException occured,\n Error:"
							+ fde.getMessage());
			throw fde;
		} catch (IndexDirectoryEmptyException ide) {
			LOGGER.log(
					Level.SEVERE,
					"IndexDirectoryEmptyException occured,\n Error:"
							+ ide.getMessage());
			throw ide;
		} catch (org.apache.lucene.queryparser.classic.ParseException pe) {
			LOGGER.log(Level.SEVERE,
					"ParseException occured,\n Error:" + pe.getMessage());
			throw pe;
		}
	}
}
