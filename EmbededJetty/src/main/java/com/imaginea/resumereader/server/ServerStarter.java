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
		int port = 0;
		if (args.length >= 1) {
			if (args.length == 2) {
				try {
					port = Integer.parseInt(args[1]);
				} catch (NumberFormatException ne) {
					System.out.println("Please Enter proper port number");
					throw ne;
				}
			}
			if (args[0].equalsIgnoreCase("start")) {
				serverStarter.doStart(port);
			} else if (args[0].equalsIgnoreCase("stop")) {
				serverStarter.doStop(port);
			} else {
				System.out
						.println("Please choose correct operations: start/stop");
			}
		} else {
			System.out
					.println("Please specify the operation you want to perform: start/stop");
		}
	}

	private void doStart(int port) throws Exception {
		// if no port specified assigning to default
		port = (port == 0) ? 7408 : port;
		// checking if the ports available or not
		if (PortChecker.portAvailable(port)
				&& PortChecker.portAvailable(port - 1)) {
			ServerStarter serverStarter = new ServerStarter();
			serverStarter.startServer(port);
		} else {
			System.out
					.println("Port is in use, please use different port "
							+ "or if (you think) it is a ResumeSearchengine using this port, "
							+ "stop the server using 'java -jar <war> stop <portnumber-1>'"
							+ "then start again");
		}
	}

	private void doStop(int port) throws UnknownHostException, IOException {
		if (port != 0) {
			ServerStarter serverStarter = new ServerStarter();
			serverStarter.stopServer(port);
		} else {
			System.out
					.println("Please specify the port to stop the server,"
							+ "Default port for stop server is 7407."
							+ "If you given any port number at the time of starting server, "
							+ "then port number is <port number - 1>."
							+ "For example: 7407 for 7408 port");
		}
	}

	private void startServer(int port) throws Exception {
		Server server = new Server();
		SocketConnector connector = new SocketConnector();

		// Set some timeout options to make debugging easier.
		/*
		 * connector.setMaxIdleTime(1000 * 60 * 60);
		 * connector.setSoLingerTime(-1);
		 */
		connector.setPort(port);
		server.setConnectors(new Connector[] { connector });

		WebAppContext context = new WebAppContext();
		context.setServer(server);
		context.setContextPath("/");

		ProtectionDomain protectionDomain = ServerStarter.class
				.getProtectionDomain();
		URL location = protectionDomain.getCodeSource().getLocation();
		context.setWar(location.toExternalForm());
		server.addHandler(context);
		Thread monitor = new MonitorThread(port - 1);
		monitor.start();
		server.start();
	}

	private void stopServer(int port) throws UnknownHostException, IOException {
		Socket s = new Socket(InetAddress.getByName("127.0.0.1"), port);
		OutputStream out = s.getOutputStream();
		System.out.println("*** sending jetty stop request");
		out.write(("\r\n").getBytes());
		out.flush();
		s.close();
	}

	// Thread for monitoring and stopping server.
	private static class MonitorThread extends Thread {

		private ServerSocket socket;

		public MonitorThread(int port) {
			setDaemon(true);
			setName("StopMonitor");
			try {
				socket = new ServerSocket(port, 1,
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
