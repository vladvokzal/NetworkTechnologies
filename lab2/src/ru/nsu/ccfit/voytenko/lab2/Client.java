package ru.nsu.ccfit.voytenko.lab2;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;

import static java.lang.System.exit;

public class Client {

    public static void main(String[] args) {
        if(args.length != 3){
            System.out.println("Usage: [file_path] [server_ip] [port]");
            exit(1);
        }

        filePath = args[0];
        checkNameLength();
        serverIP = args[1];
        port = Integer.parseInt(args[2]);

        try {
            socket = new Socket(serverIP, port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try(DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            DataOutputStream out = new DataOutputStream(socket.getOutputStream())
        ) {
            File toSend = new File(filePath);

            writeData(out, toSend.getName());
            checkError(in.readUTF());

            writeSize(out, toSend.length());
            //writeData(out, String.valueOf(toSend.length()));
            checkError(in.readUTF());

            long fileLength = toSend.length();
            long module = fileLength % SharedConstants.PART_SIZE;

            FileInputStream fileIn = new FileInputStream(filePath);

            transfer(fileIn, out, module, fileLength);

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void transfer(FileInputStream fileIn, DataOutputStream out, long module, long fileLength) throws IOException {
        byte[] buffer = new byte[SharedConstants.PART_SIZE];
        byte[] newBuffer = new byte[(int)module];
        int counter = 0;

        while ((counter = fileIn.read(buffer)) > 0){
            try{
                out.write(buffer, 0, counter);
            }
            catch (SocketException ex){
                System.out.println("Sorry, I was interrupted");
                out.close();
                exit(1);
            }

        }

//        while (true){
//            if (fileLength - counter == module){
//                fileIn.read(newBuffer);
//                writeData(out, new String(newBuffer, Charset.forName("UTF-8")));
//                break;
//            }
//            fileIn.read(buffer);
//            writeData(out, new String(buffer, Charset.forName("UTF-8")));
//            counter += SharedConstants.PART_SIZE;
//        }
    }

    private static void writeData(DataOutputStream out, String data){
        try {
            out.writeUTF(data);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeSize(DataOutputStream out, long size){
        try {
            out.writeLong(size);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkError(String str){
        if (!str.equals(SharedConstants.OK)){
            System.out.println("Error in reading...");
            exit(1);
        }
    }

    private static void checkNameLength(){
        if(filePath.getBytes(Charset.forName("UTF-8")).length > SharedConstants.MAX_NAME_SIZE){
            System.out.println("Name out of range in UTF-8 char sequence");
            exit(1);
        }
    }

    private static Socket socket = null;
    private static int port;
    private static String serverIP;
    private static String filePath;
}
