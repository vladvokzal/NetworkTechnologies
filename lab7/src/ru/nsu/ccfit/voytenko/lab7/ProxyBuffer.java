package ru.nsu.ccfit.voytenko.lab7;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class ProxyBuffer {

    public ProxyBuffer(SelectionKey clientsKey, SelectionKey serversKey, boolean setupMode){
        buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);
        this.serversKey = serversKey;
        this.clientsKey = clientsKey;
        this.step = 0;
        this.readyRecv = false;
        this.setupMode = setupMode;
    }

    public boolean isSetupMode(){
        return setupMode;
    }

    public void setSetupMode(boolean mode){
        this.setupMode = mode;
    }

    public SelectionKey getClientsKey(){
        return clientsKey;
    }

    public void setClientsKey(SelectionKey key){
        this.clientsKey = key;
    }

    public SelectionKey getServersKey() {
        return serversKey;
    }

    public void setServersKey(SelectionKey key){
        this.serversKey = key;
    }


    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void increseStep(){
        ++step;
    }

    public boolean isReadyRecv(){
        return readyRecv;
    }

    public void setReadyRecv(boolean state){
        this.readyRecv = state;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public int getServerPort() {
        return serverPort;
    }

    public int getDomainNameLength() {
        return domainNameLength;
    }

    public void setDomainNameLength(int domainNameLength) {
        this.domainNameLength = domainNameLength;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void setBuffer(ByteBuffer byteBuffer){
        this.buffer = byteBuffer;
    }

    public SelectionKey getPair(SelectionKey current){
        return current.equals(serversKey) ? clientsKey : serversKey;
    }

    private ByteBuffer buffer;
    private SelectionKey clientsKey;
    private SelectionKey serversKey;
    private boolean readyRecv;
    private int serverPort;
    private int domainNameLength;
    private int step;
    private boolean setupMode;
}