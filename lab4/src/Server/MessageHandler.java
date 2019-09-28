package Server;

import Other.Constants;
import Other.JsonExecutor;
import Other.Message;
import Other.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class MessageHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String token = JsonExecutor.getToken(exchange);

        User user = new User();
        user.setToken(token);

        if(!Server.isContainsUserToken(user) || token == null){
            exchange.sendResponseHeaders(Constants.STATUS_ERR, 0);
            JsonExecutor.sendBodyAndClose(exchange, "");
            return;
        }


        String query = exchange.getRequestURI().getQuery();
        if (query != null && !query.isEmpty()){

        }

        Message message = JsonExecutor.getMessage(exchange);
        String okJson = JsonExecutor.getJson(Server.putMessage(message));
        exchange.sendResponseHeaders(Constants.STATUS_OK, okJson.length());
        JsonExecutor.sendBodyAndClose(exchange, okJson);
    }
}
