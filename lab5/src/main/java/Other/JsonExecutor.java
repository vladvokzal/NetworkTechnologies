package Other;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.StringReader;

public class JsonExecutor {


    public static String getJson(Object obj){
        String gson = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
        return gson;
    }

    public static Message getFromJson(String jsonObj){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(jsonObj, Message.class);
    }

}
