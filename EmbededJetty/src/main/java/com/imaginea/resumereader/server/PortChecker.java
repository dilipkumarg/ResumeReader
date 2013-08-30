package com.imaginea.resumereader.server;

import java.io.IOException;
import java.net.ServerSocket;

public class PortChecker {
	public static void main(String[] args) throws IOException {
		System.out.println(Integer.parseInt("dilip"));
	}

	public static boolean portAvailable(int port) throws IOException {
		boolean portAvailable = true;
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(port);
		} catch (IOException e) {
			portAvailable = false;
		} finally {
			if (socket != null)
				socket.close();
		}
		return portAvailable;
	}
}
