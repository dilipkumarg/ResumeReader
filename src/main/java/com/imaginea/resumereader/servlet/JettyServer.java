package com.imaginea.resumereader.servlet;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {
	private static Server server = new Server();
	static Logger logger = Logger.getLogger(JettyServer.class.getName());

	public JettyServer() {

	}

	private void initServer() {
		final SocketConnector connector = new SocketConnector();
		connector.setPort(7501);
		server.setConnectors(new Connector[] { connector });
		final Context root = new Context(server, "/resumereader",
				Context.SESSIONS);
		root.addServlet(new ServletHolder(new SearchServlet()), "/search");
	}

	public void start() {
		initServer();
		final WebAppContext ctx = new WebAppContext();
		ctx.setServer(server);
		ctx.setContextPath("/");
		ctx.setWar("./src/main/webapp");
		server.addHandler(ctx);
		try {
			server.start();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "error in starting server", e);
		}
	}

	public void stop() {
		// TODO Auto-generated method stub
		
	}
}
