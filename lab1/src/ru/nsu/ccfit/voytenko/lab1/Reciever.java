package ru.nsu.ccfit.voytenko.lab1;

import java.io.IOException;
import java.net.*;

public class Reciever implements Runnable{

    @Override
    public void run() {
        ClientsBase clientsBase = new ClientsBase();
        Validator validator = new Validator();
        InetAddress group = null;
        try {
            group = InetAddress.getByName(SharedData.GROUP_ADDR);
        } catch (UnknownHostException ignored) { }

        byte[] buffer = new byte[SharedData.BUF_SIZE];

        try(MulticastSocket s1 = new MulticastSocket(SharedData.PORT)) {
            s1.joinGroup(group);
            while (true){
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                s1.receive(packet);
                clientsBase.addClient(packet.getSocketAddress().toString(), System.currentTimeMillis());
                validator.validate(clientsBase);
                clientsBase.showUsers();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
