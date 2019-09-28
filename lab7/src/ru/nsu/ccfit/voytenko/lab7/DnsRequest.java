package ru.nsu.ccfit.voytenko.lab7;

import java.util.ArrayList;
import java.util.HashMap;

public class DnsRequest {

    private HashMap<String, ProxyBuffer> requests = new HashMap<>();
    private ArrayList<String> toSend = new ArrayList<>();

    public DnsRequest() {}

    public HashMap<String, ProxyBuffer> getRequests() {
        return requests;
    }

    public ArrayList<String> getToSend() {
        return toSend;
    }

}
