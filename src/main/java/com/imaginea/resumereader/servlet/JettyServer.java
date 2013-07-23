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

	}

	private void initServer() {
		final SocketConnector connector = new SocketConnector();
		connector.setPort(7501);
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
}
