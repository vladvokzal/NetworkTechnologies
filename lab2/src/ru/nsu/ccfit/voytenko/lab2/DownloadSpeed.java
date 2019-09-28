package ru.nsu.ccfit.voytenko.lab2;

public class DownloadSpeed implements Runnable {
    @Override
    public void run() {
        if (storedTime - System.nanoTime() > 3000){

        }
    }

    public void acceptTime(long time){
        storedTime = time;
    }

    private long storedTime;
    private long full;
}
