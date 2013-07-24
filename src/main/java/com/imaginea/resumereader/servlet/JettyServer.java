package com.imaginea.resumereader.servlet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.servlet.Context;
import org.mortbay.jetty.servlet.ServletHolder;
import org.mortbay.jetty.webapp.WebAppContext;

public class JettyServer {
	private static Server server = new Server();

	private void initServer() {
		final SocketConnector connector = new SocketConnector();
		connector.setPort(7408);
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
		Thread monitor = new MonitorThread();
		monitor.start();
		server.start();
	}

	// Thread for monitoring and stopping server.
	private static class MonitorThread extends Thread {

		private ServerSocket socket;

		public MonitorThread() {
			setDaemon(true);
			setName("StopMonitor");
			try {
				socket = new ServerSocket(7407, 1,
						InetAddress.getByName("127.0.0.1"));
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void run() {
			System.out.println("*** running jetty 'stop' thread");
			Socket accept;
			try {
				accept = socket.accept();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(accept.getInputStream()));
				reader.readLine();
				System.out.println("*** stopping jetty embedded server");
				server.stop();
				accept.close();
				socket.close();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
