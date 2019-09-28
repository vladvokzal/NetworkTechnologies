package Server;

import Other.Constants;
import Other.JsonExecutor;
import Other.Message;
import Other.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LogoutHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String token = JsonExecutor.getToken(exchange);

        User tmpUser = new User();
        tmpUser.setToken(token);
        if (!Server.isContainsUserToken(tmpUser) || token == null){
            exchange.sendResponseHeaders(Constants.STATUS_ERR, 0);
            JsonExecutor.sendBodyAndClose(exchange, "");
            return;
        }

        JsonExecutor.setContent(exchange);
        Server.removeUser(tmpUser);
        Message goodByeMessage = new Message();
        goodByeMessage.setMsg("---Good Bye---");
        String goodBye = JsonExecutor.getJson(goodByeMessage);
        exchange.sendResponseHeaders(Constants.STATUS_OK, (long)goodBye.length());
        JsonExecutor.sendBodyAndClose(exchange, goodBye);
    }
}
