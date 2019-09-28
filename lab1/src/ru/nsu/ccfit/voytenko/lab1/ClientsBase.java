package ru.nsu.ccfit.voytenko.lab1;

import java.util.Map;
import java.util.TreeMap;

public class ClientsBase {
    public ClientsBase() {}

    public void addClient(String key, Long value) {
        data.merge(key, value, (a, b) -> System.currentTimeMillis());
    }

    public void showUsers(){
        System.out.println("List of active instances:");
        for (String str : data.keySet()) {
            System.out.println("\t" + str);
        }
        //System.out.println("Amount of active instances: " + data.size());
    }

    public Map<String, Long> getData(){
        return data;
    }

    private Map<String, Long> data = new TreeMap<>();
}
