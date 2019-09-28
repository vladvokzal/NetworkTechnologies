package Other;

import Server.Server;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class JsonExecutor {

    public static User getUser(HttpExchange exchange) {
        InputStreamReader input = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.fromJson(input, User.class);
    }

    public static String getToken(HttpExchange exchange){
        Headers headers = exchange.getRequestHeaders();
        return headers.getFirst("Authorization");
    }

    public static Message getMessage(HttpExchange exchange) throws IOException {
        InputStreamReader input = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Message message = gson.fromJson(input, Message.class);

        int authorId = Server.getUser(getToken(exchange)).getId();
        message.setAuthorId(authorId);
        return message;
    }

    public static void sendBodyAndClose(HttpExchange exchange, String jsonValue) throws IOException {
        OutputStream os = exchange.getResponseBody();
        os.write(jsonValue.getBytes());
        os.close();
    }

    public static String getJson(Object obj){
        String gson = new GsonBuilder().setPrettyPrinting().create().toJson(obj);
        return gson;
    }

    public static void setHeader(HttpExchange exchange, String k, String v){
        exchange.getResponseHeaders().add(k, v);
    }

    public static void setContent(HttpExchange exchange){
        setHeader(exchange, "Content-Type", "application/json");
    }
}
