package ru.nsu.ccfit.voytenko.lab6;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PortForwarder {

    public PortForwarder() {}

    public void forward(int lport, int rport, String host){
        try {
            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            InetSocketAddress socketFrom = new InetSocketAddress("localhost", lport);
            InetSocketAddress socketTo = new InetSocketAddress(host, rport);

            serverSocketChannel.bind(socketFrom);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true){
                int counter = selector.select();
                if (counter != 0){
                    execute(selector, socketTo, serverSocketChannel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execute(Selector selector, InetSocketAddress address, ServerSocketChannel socketChannel) throws IOException {
        Set<SelectionKey> keySet = selector.selectedKeys();
        Iterator<SelectionKey> iter = keySet.iterator();

        while (iter.hasNext()){
            SelectionKey currentKey = iter.next();

            if (currentKey.isAcceptable()){
                accept(selector, socketChannel, address);
            }

            if (currentKey.isReadable()){
                read(currentKey);
            }

            if (currentKey.isWritable()){
                write(currentKey);
            }
        }
        selector.selectedKeys().clear();
    }

    private static void accept(Selector selector, ServerSocketChannel socketChannel, InetSocketAddress address) throws IOException {
        SocketChannel clientChanel = socketChannel.accept();
        clientChanel.configureBlocking(false);
        SelectionKey clientKey = clientChanel.register(selector, SelectionKey.OP_READ);
        SocketChannel serverChanel = SocketChannel.open(address);
        serverChanel.configureBlocking(false);
        SelectionKey serverKey = serverChanel.register(selector, SelectionKey.OP_READ);
        ProxyBuffer proxyBuffer = new ProxyBuffer(clientKey, serverKey);
        clientKey.attach(proxyBuffer);
        serverKey.attach(proxyBuffer);
    }

    private static void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ((ProxyBuffer)key.attachment()).getBuffer();
        SelectionKey pairKey = ((ProxyBuffer)key.attachment()).getPair(key);

        int counter = channel.read(buffer);
        if (counter == -1){
            System.out.println("Кончились единички и нулики");
            key.interestOps(0);
            return;
        }

        pairKey.interestOps(pairKey.interestOps() | SelectionKey.OP_WRITE);
        if (buffer.hasRemaining()){
            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
        }
    }

    private static void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = ((ProxyBuffer)key.attachment()).getBuffer();
        SelectionKey pairKey = ((ProxyBuffer)key.attachment()).getPair(key);

        buffer.flip();
        channel.write(buffer);
        buffer.flip();

        pairKey.interestOps(pairKey.interestOps() | SelectionKey.OP_READ);
        if (buffer.position() == 0){
            key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
        }
    }

}
