package ru.nsu.ccfit.voytenko.lab6;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public class ProxyBuffer {

    public ProxyBuffer(SelectionKey clientsKey, SelectionKey serversKey){
        buffer = ByteBuffer.allocate(BUFFER_SIZE);
        this.serversKey = serversKey;
        this.clientsKey = clientsKey;
    }

    public SelectionKey getPair(SelectionKey current){
        return current.equals(serversKey) ? clientsKey : serversKey;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    private ByteBuffer buffer;
    private SelectionKey clientsKey;
    private SelectionKey serversKey;

    private static final int BUFFER_SIZE = 2048;
}
