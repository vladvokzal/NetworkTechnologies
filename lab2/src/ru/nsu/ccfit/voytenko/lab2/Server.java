package ru.nsu.ccfit.voytenko.lab2;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.System.exit;

public class Server {
    public static void main(String[] args) throws IOException {
        if (args.length != 1){
            System.out.println("Usage: [prog_name] [port_number]");
            exit(1);
        }

        setPort(Integer.parseInt(args[0]));
        startServer();
    }

    public static void startServer() throws IOException {
        ServerSocket socket = new ServerSocket(port);

        while (true){
            Socket connect = socket.accept();
            Thread serverThread = new Thread(new ServerThread(threadId, connect));
            ++threadId;
            serverThread.start();
        }
    }

    public static void setPort(int port_){
        port = port_;
    }

    private static int threadId = 0;
    private static int port;
}
