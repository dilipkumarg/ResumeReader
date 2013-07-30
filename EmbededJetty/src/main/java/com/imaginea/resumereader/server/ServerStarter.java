package com.imaginea.resumereader.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.security.ProtectionDomain;

import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.bio.SocketConnector;
import org.mortbay.jetty.webapp.WebAppContext;

public class ServerStarter {
    private static Server server = new Server();

    public static void main(String[] args) throws Exception {
        Server server = new Server();
        SocketConnector connector = new SocketConnector();

        // Set some timeout options to make debugging easier.
        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setSoLingerTime(-1);
        connector.setPort(7408);
        server.setConnectors(new Connector[]{connector});

        WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/");

        ProtectionDomain protectionDomain = ServerStarter.class.getProtectionDomain();
        URL location = protectionDomain.getCodeSource().getLocation();
        context.setWar(location.toExternalForm());

        server.addHandler(context);
        server.start();
        System.in.read();
        server.stop();
        server.join();
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
