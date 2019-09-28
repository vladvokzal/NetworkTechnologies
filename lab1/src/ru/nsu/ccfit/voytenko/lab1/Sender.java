package ru.nsu.ccfit.voytenko.lab1;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Sender implements Runnable{

    @Override
    public void run() {
        InetAddress group = null;
        try {
            group = InetAddress.getByName(SharedData.GROUP_ADDR);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try(DatagramSocket s2 = new DatagramSocket()){
            DatagramPacket sendPacket = new DatagramPacket(SharedData.msg.getBytes(),
                    SharedData.msg.getBytes().length, group, SharedData.PORT);
            while (true){
                s2.send(sendPacket);
                Thread.sleep(SharedData.WAIT_TIME);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
