package ru.nsu.ccfit.voytenko.lab6;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) {
	    if (args.length != 3){
	        System.out.println("Usage: [lport] [rhost] [rport]");
	        exit(1);
        }

        int lport = Integer.parseInt(args[0]);
        int rport = Integer.parseInt(args[2]);
	String rhost = args[1];

        PortForwarder portForwarder = new PortForwarder();
	portForwarder.forward(lport, rport, rhost);

    }

}
