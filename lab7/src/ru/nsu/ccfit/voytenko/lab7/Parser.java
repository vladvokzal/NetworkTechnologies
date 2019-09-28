package ru.nsu.ccfit.voytenko.lab7;

import java.nio.ByteBuffer;

public class Parser {

    public Parser() { }

    public int parsePort(int offset, int length, ByteBuffer buffer) {
        int port;
        StringBuilder portBuilder = new StringBuilder();
        for(int i = offset; i < offset + length; i++) {
            String pl;
            if(buffer.array()[i] < 0) {
                pl = getExtraCode(buffer.array()[i]);
            } else {
                StringBuilder plBuilder = new StringBuilder(Integer.toString(buffer.array()[i], 2));
                while(plBuilder.length() < 8) {
                    plBuilder.insert(0, "0");
                }
                pl = plBuilder.toString();
            }
            portBuilder.append(pl);
        }
        port = Integer.parseInt(portBuilder.toString(), 2);
        System.out.println("port: " + port);
        return port;
    }

    public String parseAddress(ByteBuffer byteBuffer){
        StringBuilder ipAddress = new StringBuilder();
        for(int i = 4; i < 8; i++) {
            if(byteBuffer.array()[i] < 0) {
                String extraCode = getExtraCode(byteBuffer.array()[i]);
                int ipItem = Integer.parseInt(extraCode, 2);
                ipAddress.append(ipItem);
            } else {
                ipAddress.append(byteBuffer.array()[i]);
            }
            if(7 != i) {
                ipAddress.append(".");
            }
        }
        System.out.println("host: " + ipAddress.toString());
        return ipAddress.toString();
    }

    public String parseDomainName(ByteBuffer buffer){
        StringBuilder domainName = new StringBuilder();
        for(int i = 5; i < buffer.array()[4] + 5; i++) {
            domainName.append((char) buffer.array()[i]);
        }
        return domainName.toString();
    }

    public String getExtraCode(int number){
        int plusBin = number * (-1);
        plusBin -= 1;
        StringBuilder plBuilder = new StringBuilder(Integer.toString(plusBin, 2));
        while(plBuilder.length() < 8) {
            plBuilder.insert(0, "0");
        }
        String pl = plBuilder.toString();
        pl = pl.replace('0', '2');
        pl = pl.replace('1', '0');
        pl = pl.replace('2', '1');
        return pl;
    }
}
