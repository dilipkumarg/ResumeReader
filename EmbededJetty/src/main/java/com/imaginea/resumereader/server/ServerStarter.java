package com.imaginea.resumereader.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.ProtectionDomain;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class ServerStarter {
	private static Server server = new Server();

	public static void main(String[] args) throws Exception {
		ServerStarter serverStarter = new ServerStarter();
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("start")) {
				serverStarter.startServer();
			} else if (args[0].equalsIgnoreCase("stop")) {
				serverStarter.stopServer();
			} else {
				System.out
						.println("Please choose correct operations: start/stop");
			}
		} else {
			System.out
					.println("Please specify the operation you want to perform: start/stop");
		}
	}

	private void startServer() throws Exception {
		Server server = new Server();
		SocketConnector connector = new SocketConnector();

		// Set some timeout options to make debugging easier.
		/*
		 * connector.setMaxIdleTime(1000 * 60 * 60);
		 * connector.setSoLingerTime(-1);
		 */
		connector.setPort(7408);
		server.setConnectors(new Connector[] { connector });

		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/");

		ProtectionDomain protectionDomain = ServerStarter.class
				.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		context.setWar(location.toExternalForm());
		server.addHandler(context);
		Thread monitor = new MonitorThread();
		monitor.start();
		server.start();
	}

	private void stopServer() throws UnknownHostException, IOException {
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), 7407);
		OutputStream out = s.getOutputStream();
		System.out.println("*** sending jetty stop request");
		out.write(("\r\n").getBytes());
		out.flush();
		s.close();
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
				System.out.println("*** jetty embedded server stopped");
				accept.close();
				socket.close();
				System.exit(0);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
