package ru.nsu.ccfit.voytenko.lab7;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import org.xbill.DNS.*;

import static java.lang.System.exit;


public class ProxyServer {

    public ProxyServer() {}

    public void startProcessing(int lport){
        try {
            parser = new Parser();
            selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);

            InetSocketAddress socketFrom = new InetSocketAddress("localhost", lport);

            serverSocketChannel.bind(socketFrom);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            DatagramChannel asClient = DatagramChannel.open();
            asClient.configureBlocking(false);
            dnsKey = asClient.register(selector, SelectionKey.OP_READ);
            dnsKey.attach(new DnsRequest());
            asClient.connect(new InetSocketAddress(ResolverConfig.getCurrentConfig().server(), 55));

            while (true){
                int counter = selector.select();
                if (counter != 0){
                    execute(selector, serverSocketChannel);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void execute(Selector selector, ServerSocketChannel serverChannel){
        Set<SelectionKey> keySet = selector.selectedKeys();
        Iterator<SelectionKey> iter = keySet.iterator();

        while (iter.hasNext()){
            SelectionKey currentKey = iter.next();
            try {

                if (currentKey.isAcceptable()){
                    accept(serverChannel);
                }

                if (currentKey.isReadable()){
                    if (!read(currentKey)){
                        continue;
                    }
                }

                if (currentKey.isWritable()){
                    write(currentKey);
                }
            } catch (IOException e){
                e.getLocalizedMessage();
                //closeConnection(currentKey);
                //nothing
            }

        }
        selector.selectedKeys().clear();

    }

    private static void accept(ServerSocketChannel socketChannel) throws IOException {
        SocketChannel clientChanel = socketChannel.accept();
        clientChanel.configureBlocking(false);
        SelectionKey clientKey = clientChanel.register(selector, SelectionKey.OP_READ);
        ProxyBuffer proxyBuffer = new ProxyBuffer(clientKey, clientKey, true);
        clientKey.attach(proxyBuffer);
    }

    private static boolean read(SelectionKey key) throws IOException {
        if (key.equals(dnsKey)){
            return readDns();
        }

        SocketChannel channel = (SocketChannel) key.channel();
        ProxyBuffer proxyBuffer = (ProxyBuffer) key.attachment();
        ByteBuffer buffer = proxyBuffer.getBuffer();
        SelectionKey pairKey = proxyBuffer.getPair(key);
        int counter;
        counter = channel.read(buffer);

        if (counter == -1){
            key.interestOps(0);
            return false;
        }

        if (proxyBuffer.isSetupMode()){
            int step = proxyBuffer.getStep();
            if (step == 0){
                initialCommunication(key, proxyBuffer);
            } else if (step == 1){
                communicate(key, proxyBuffer, buffer);
            }
            return true;
        }

        pairKey.interestOps(pairKey.interestOps() | SelectionKey.OP_WRITE);
        if (buffer.hasRemaining()){
            key.interestOps(key.interestOps() & ~SelectionKey.OP_READ);
        }
        return true;
    }

    private static void initialCommunication(SelectionKey key, ProxyBuffer proxyBuffer){
        ByteBuffer buffer;
        byte[] response = new byte[2];
        response[0] = Constants.SOCKS5_VERSION;
        response[1] = Constants.NO_AUTHORISATION;
        buffer = ByteBuffer.wrap(response);
        proxyBuffer.setBuffer(buffer);

        proxyBuffer.increseStep();
        key.interestOps(SelectionKey.OP_WRITE);
    }

    private static void communicate(SelectionKey key, ProxyBuffer proxyBuffer, ByteBuffer byteBuffer) throws IOException {
        if (isIncorrectBufferLength(byteBuffer)){
            return;
        }
        if (byteBuffer.array()[Constants.ADDRESS_TYPE] == Constants.TYPE_IPV4){
            configurateIPv4(proxyBuffer, byteBuffer);
        } else if (byteBuffer.array()[Constants.ADDRESS_TYPE] == Constants.TYPE_DOMAIN_NAME){
            configurateDomainName(key, proxyBuffer, byteBuffer);
        }

    }

    private static void configurateDomainName(SelectionKey key, ProxyBuffer proxyBuffer, ByteBuffer buffer){
        String domainName = parser.parseDomainName(buffer);
        int port = parser.parsePort(((int) buffer.array()[4]) + 5, 2, buffer);

        proxyBuffer.setDomainNameLength((int) buffer.array()[4] + 5 + 2);
        proxyBuffer.setServerPort(port);
        DnsRequest dnsRequest = (DnsRequest) dnsKey.attachment();
        dnsRequest.getRequests().put(domainName + ".", proxyBuffer);
        dnsRequest.getToSend().add(domainName + ".");
        dnsKey.interestOps(SelectionKey.OP_WRITE);
        key.interestOps(0);
    }

    private static boolean isIncorrectBufferLength(ByteBuffer buffer){
        return buffer.array().length < 3;
    }

    private static void configurateIPv4(ProxyBuffer proxyBuffer, ByteBuffer byteBuffer) throws IOException {
        String ipAddress = parser.parseAddress(byteBuffer);
        int port = parser.parsePort(8, 2, byteBuffer);
        proxyBuffer.setServerPort(port);

        byte[] response = new byte[10];
        response[0] = Constants.SOCKS5_VERSION;
        response[1] = Constants.NO_AUTHORISATION;
        response[2] = Constants.RESERVED_FIELD;
        response[3] = Constants.TYPE_IPV4;
        System.arraycopy(byteBuffer.array(), 4, response, 4, 4);
        System.arraycopy(byteBuffer.array(), 8, response, 8, 2);
        finishSetup(proxyBuffer, ipAddress, response);
    }

    private static boolean readDns() throws IOException {
        DatagramChannel datagramChannel = (DatagramChannel) dnsKey.channel();
        DnsRequest dnsRequest = (DnsRequest) dnsKey.attachment();
        ByteBuffer buffer = ByteBuffer.allocate(Constants.BUFFER_SIZE);

        datagramChannel.read(buffer);
        Message message = new Message(buffer.array());
        Record[] answers = message.getSectionArray(Section.ANSWER);
        if (answers.length == 0){
            System.out.println("DNS-resolver fail to get IP-addres");
            exit(1);
        }

        String ipAddress = answers[0].rdataToString();
        String domainName = answers[0].getName().toString();
        ProxyBuffer proxyBuffer = dnsRequest.getRequests().get(domainName);
        byte[] response = new byte[proxyBuffer.getDomainNameLength()];
        response[0] = Constants.SOCKS5_VERSION;
        response[1] = Constants.NO_AUTHORISATION;
        response[2] = Constants.RESERVED_FIELD;
        response[3] = Constants.TYPE_DOMAIN_NAME;
        for (int i = 4; i < proxyBuffer.getDomainNameLength(); i++){
            response[i] = proxyBuffer.getBuffer().array()[i];
        }
        dnsKey.interestOps(SelectionKey.OP_READ);
        finishSetup(proxyBuffer, ipAddress, response);
        return true;
    }

    private static void finishSetup(ProxyBuffer proxyBuffer, String ipAddress, byte[] response) throws IOException {
        SelectionKey key = proxyBuffer.getClientsKey();
        int port = proxyBuffer.getServerPort();
        ByteBuffer buffer = ByteBuffer.wrap(response);
        proxyBuffer.setBuffer(buffer);
        proxyBuffer.increseStep();
        key.interestOps(SelectionKey.OP_WRITE);

        SocketChannel serverSocketChanel = SocketChannel.open(new InetSocketAddress(ipAddress, port));
        serverSocketChanel.configureBlocking(false);
        SelectionKey serverKey = serverSocketChanel.register(selector, SelectionKey.OP_READ);

        proxyBuffer.setServersKey(serverKey);
        serverKey.attach(proxyBuffer);
        proxyBuffer.setReadyRecv(true);

    }

    private static void write(SelectionKey key) throws IOException {
        if (key.equals(dnsKey)){
            writeDns(dnsKey);
            return;
        }

        ProxyBuffer proxyBuffer = (ProxyBuffer) key.attachment();

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer buffer = proxyBuffer.getBuffer();

        if (proxyBuffer.isSetupMode()){

            buffer.clear();
            proxyBuffer.setBuffer(ByteBuffer.allocate(Constants.BUFFER_SIZE));
            if (proxyBuffer.isReadyRecv()){
                proxyBuffer.setSetupMode(false);
            }
            key.interestOps(SelectionKey.OP_READ);
        }else {
            SelectionKey pairKey = proxyBuffer.getPair(key);

            buffer.flip();
            channel.write(buffer);
            buffer.flip();

            pairKey.interestOps(pairKey.interestOps() | SelectionKey.OP_READ);
            if (buffer.position() == 0) {
                key.interestOps(key.interestOps() & ~SelectionKey.OP_WRITE);
            }
        }
    }

    private static void writeDns(SelectionKey key){
        DatagramChannel datagramChannel = (DatagramChannel)key.channel();
        DnsRequest dnsRequest = (DnsRequest) dnsKey.attachment();
        dnsRequest.getToSend().forEach(domainName -> {
            try {
                Message message = Message.newQuery(Record.newRecord(new Name(domainName), Type.A, DClass.ANY));
                datagramChannel.write(ByteBuffer.wrap(message.toWire()));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        dnsRequest.getToSend().clear();
        key.interestOps(SelectionKey.OP_READ);
    }

    private static void closeConnection(SelectionKey key){
        try {
            key.channel().close();
            key.cancel();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static SelectionKey dnsKey;
    private static Selector selector;
    private static Parser parser;
}
