package Server;

import Other.Constants;
import Other.JsonExecutor;
import Other.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class LoginHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            //System.out.println("HERE 1");
            User user = JsonExecutor.getUser(exchange);
            //System.out.println("Username is : " + user.getName());
            if (Server.isContainsUserName(user)) {
                //System.out.println("HERE 1.5");
                JsonExecutor.setHeader(exchange, "WWW-Authenticate", "Token realm='Username is already in use'");
                exchange.sendResponseHeaders(Constants.UNAUTHORISED_USER, 0);
                JsonExecutor.sendBodyAndClose(exchange, "");
                return;
            }

            String okJson = JsonExecutor.getJson(Server.putUser(user));
//            System.out.println("json value = " + okJson);
            JsonExecutor.setContent(exchange);
            exchange.sendResponseHeaders(Constants.STATUS_OK, (long) okJson.length());
            JsonExecutor.sendBodyAndClose(exchange, okJson);
        } catch (Exception e) {
            exchange.sendResponseHeaders(400, 0);
            exchange.close();
        }
    }
}
