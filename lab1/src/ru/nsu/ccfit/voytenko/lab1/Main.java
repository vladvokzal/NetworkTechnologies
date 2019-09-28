package ru.nsu.ccfit.voytenko.lab1;


import java.net.*;

import static java.lang.System.exit;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        if(args.length != 2){
            System.out.println("Usage: [prog_name] [multicast_addr] [port]");
            exit(1);
        }
        String multicastAddress = args[0];
        SharedData.setPort(Integer.parseInt(args[1]));

        if(!InetAddress.getByName(multicastAddress).isMulticastAddress()){
            System.out.println("Incorrect IP address, see list of possible IPs in manual...");
        }

        Thread senderThread = new Thread(new Sender());
        Thread recieverThread = new Thread(new Reciever());
        senderThread.start();
        recieverThread.start();

    }

}
