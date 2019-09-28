package Server;

import Other.Constants;
import Other.Message;
import Other.User;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.UUID;


public class Server {
    public static void main(String[] args) throws IOException {
        server = HttpServer.create();
        server.bind(new InetSocketAddress(8080), 10);

        server.createContext("/login", new LoginHandler());
        server.createContext("/logout", new LogoutHandler());
        server.createContext("/message", new MessageHandler());
        server.createContext("/users", new UsersHandler());

        server.setExecutor(null);
        server.start();
    }

    public static boolean isContainsUserName(User user){
        for(User curUser : users){
            if(user.getName().equals(curUser.getName())){
                return true;
            }
        }
        return false;
    }

    public static boolean isContainsUserId(User user){
        for(User curUser : users){
            if(user.getId() == curUser.getId()){
                return true;
            }
        }
        return false;
    }

    public static boolean isContainsUserToken(User user){
        for(User curUser : users){
            if(user.getToken().equals(curUser.getToken())){
                return true;
            }
        }
        return false;
    }

    public static User putUser(User user){
        String token = UUID.randomUUID().toString();
        boolean isOnline = true;
        userId++;
        User newUser = new User(user.getName(), userId, token, isOnline);
        users.add(newUser);
        return newUser;
    }

    public static Message putMessage(Message message){
        messageId++;
        message.setId(messageId);
        historyOfMessages.add(message);
        return message;
    }

    public static void removeUser(User user){
        users.removeIf(x -> x.getToken().equals(user.getToken()));
    }

    public static User getUser(String userToken){
        for(User curUser : users){
            if (curUser.getToken().equals(userToken)){
                return curUser;
            }
        }
        return null;
    }

    public static User getUser(int userId){
        for(User curUser : users){
            if (curUser.getId() == userId){
                return curUser;
            }
        }
        return null;
    }


    public static ArrayList<User> getUsers(){
        return users;
    }

    private static ArrayList<Message> historyOfMessages = new ArrayList<>();
    private static int userId = Constants.DEFAULT_ID;
    private static int messageId = Constants.DEFAULT_MSG_ID;
    private static ArrayList<User> users = new ArrayList<>();

    private static HttpServer server;
}
