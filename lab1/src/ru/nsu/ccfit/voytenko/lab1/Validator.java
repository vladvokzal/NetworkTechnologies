package ru.nsu.ccfit.voytenko.lab1;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Validator {

    public Validator() {}

    public void validate(ClientsBase base){
        Map<String, Long> map = base.getData();

        for (Map.Entry<String, Long> entry : new TreeMap<String, Long>(map).entrySet()) {
            long curValue = entry.getValue();

            if (System.currentTimeMillis() - curValue > 2 * SharedData.WAIT_TIME){
                System.out.println("Loose connection");
                map.remove(entry.getKey());
            }
        }
    }
}
