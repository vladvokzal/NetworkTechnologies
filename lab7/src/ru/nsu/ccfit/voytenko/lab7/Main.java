package ru.nsu.ccfit.voytenko.lab7;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
	    if(args.length != 1){
	        System.out.println("Usage: [port]");
	        exit(1);
        }

        int port = Integer.parseInt(args[0]);

        ProxyServer proxyServer = new ProxyServer();
        proxyServer.startProcessing(port);
    }
}
