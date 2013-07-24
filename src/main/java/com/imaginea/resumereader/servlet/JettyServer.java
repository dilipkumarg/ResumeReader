package com.imaginea.resumereader.servlet;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {
	private static Server server = new Server();

	public JettyServer() {
		this(8585);
	}
	
	public JettyServer(Integer runningPort) {
		server = new Server(runningPort);
	}

	private void initServer() {
		final SocketConnector connector = new SocketConnector();
		connector.setPort(7502);
		server.setConnectors(new Connector[] { connector });
		final Context root = new Context(server, "/resumereader",
				Context.SESSIONS);
		root.addServlet(new ServletHolder(new SearchServlet()), "/search");
		root.addServlet(new ServletHolder(new DocumentViewerServlet()), "/view");
	}

	public void start() throws Exception {
		initServer();
		final WebAppContext ctx = new WebAppContext();
		ctx.setServer(server);
		ctx.setContextPath("/");
		ctx.setWar("./src/main/webapp");
		server.addHandler(ctx);
		server.start();
	}

	public void stop() throws Exception {
		server.stop();
	}
	
	public boolean isStarted() {
		return server.isStarted();
	}
	
	public boolean isStopped() {
		return server.isStopped();
	}
}
