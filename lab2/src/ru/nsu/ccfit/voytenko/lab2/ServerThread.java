package ru.nsu.ccfit.voytenko.lab2;

import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable{

    ServerThread(int id_, Socket socket_){
        id = id_;
        socket = socket_;
    }

    @Override
    public void run() {
        try(InputStream clientInput = socket.getInputStream();
            DataInputStream in = new DataInputStream(clientInput);
            DataOutputStream out = new DataOutputStream(socket.getOutputStream()))
        {

        long momentTime;
        String msg;
        byte[] buffer = new byte[SharedConstants.PART_SIZE];
        startTime = System.currentTimeMillis();

        this.recievedName = in.readUTF();
        System.out.println("Accepted name: " + recievedName);
        writeData(out, SharedConstants.OK);

        this.recievedLength = in.readLong();
        System.out.println("Accepted length: " + recievedLength);
        writeData(out, SharedConstants.OK);

        File directory = new File(dirName);
        if (!directory.exists()){
            directory.mkdir();
        }
        File file = new File(directory, recievedName);
        file.mkdirs();

        FileOutputStream ous = new FileOutputStream(file);
        momentTime = System.currentTimeMillis();

        int count;
        int prevReaded = 0;
        while ((count = in.read(buffer)) > 0){
            ous.write(buffer, 0, count);
            acceptedBytes += count;
            prevReaded += count;
            if (System.currentTimeMillis() - momentTime > 3000){
                momentTime = System.currentTimeMillis();
                printMomentSpeed(prevReaded);
                prevReaded = 0;
            }
        }

        out.flush();

        endTime = System.currentTimeMillis();
        printAvgSpeed();

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    public void printAvgSpeed(){
        System.out.println("Time elapsed = " + (endTime - startTime));
        System.out.println("Avg speed = " + Double.valueOf(acceptedBytes / (endTime - startTime) )* 1000 / 1024 + " KByte/s");
    }

    public void printMomentSpeed(int count){
        System.out.println("Moment speed = " + Double.valueOf(count / 3000 / 1024) * 1000 + " KByte/s");
    }

    public void writeData(DataOutputStream out, String str) throws IOException {
        out.writeUTF(str);
        out.flush();
    }

    public long startTime;
    public long endTime;
    private long acceptedBytes = 0;
    private final String dirName = "uploads";
    private final String fileName = "output";
    private long recievedLength;
    private String recievedName;
    private int id;
    private Socket socket;

}
