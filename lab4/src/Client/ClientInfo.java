package Client;

import Other.Constants;
import Other.Message;
import Other.User;

import java.util.ArrayList;

public class ClientInfo {

    public static String token = "";
    public static String name = "";
    public static int id = Constants.DEFAULT_ID;
    public static boolean isOnline = false;

    public static boolean containsId(int id){
        return findUser(id) != null;
    }

    public static void addMyMsgId(int id){
        myMsgId.add(id);
    }

    public static void addUser(User user){
        users.add(user);
    }

    public static void clearInfo(){
        name = "";
        token = "";
        id = Constants.DEFAULT_ID;
        isOnline = false;
    }

    public static void clearAllUsers(){
        users.clear();
    }

    public static void setInfo(User user){
        name = user.getName();
        token = user.getToken();
        id = user.getId();
        isOnline = user.isOnline();
    }

    public static int getLastMsgId(){
        return msgId;
    }

    public static void addMessage(Message message){
        ++msgId;
        if(!myMsgId.contains(message.getId())){
            String userName = findUserName(message.getAuthorId());
            String actualMessage = message.getMsg();
            System.out.println(userName + " : " + actualMessage);
        }
    }

    private static User findUser(int idUser){
        for (User tmp : users){
            if (tmp.getId() == idUser){
                return  tmp;
            }
        }
        return null;
    }

    private static String findUserName(int id){
        User unknownUser = new User();
        unknownUser.setName("Unknown...");
        return findUser(id) != null ? findUser(id).getName() : unknownUser.getName();
    }

    private static int msgId = 0;
    private static ArrayList<User> users = new ArrayList<>();
    private static ArrayList<Integer> myMsgId = new ArrayList<>();
}
