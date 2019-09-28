package Server;

import Other.Constants;
import Other.JsonExecutor;
import Other.User;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

public class UsersHandler implements HttpHandler {
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

        String path = exchange.getRequestURI().getPath();
        if (path.length() == "/users".length()){
            String allUsers = JsonExecutor.getJson(Server.getUsers());
            exchange.sendResponseHeaders(Constants.STATUS_OK, allUsers.length());
            JsonExecutor.setContent(exchange);
            JsonExecutor.sendBodyAndClose(exchange, allUsers);
            return;
        }

        int userId = Integer.parseInt(path.substring(path.lastIndexOf("/"), path.length()));
        User tmpUser = new User();
        tmpUser.setId(userId);
        if (!Server.isContainsUserId(tmpUser)){
            exchange.sendResponseHeaders(Constants.STATUS_ERR, 0);
            JsonExecutor.sendBodyAndClose(exchange, "");
            return;
        }

        String okJson = JsonExecutor.getJson(Server.getUser(userId));
        exchange.sendResponseHeaders(Constants.STATUS_OK, okJson.length());
        JsonExecutor.sendBodyAndClose(exchange, okJson);
    }
}
