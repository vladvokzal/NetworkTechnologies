package ru.nsu.ccfit.voytenko.lab1;

public class SharedData {
    public static String msg = "Hey there";
    public static final int BUF_SIZE = 15;
    public static int WAIT_TIME = 1000;
    public static  String GROUP_ADDR = "224.0.0.3";
    public static int PORT = 5050;

    public static void setPort(int port){
        PORT = port;
    }
}
