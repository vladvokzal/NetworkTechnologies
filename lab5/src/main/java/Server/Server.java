package Server;

import Other.*;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.ArrayList;

@ServerEndpoint(value = "/chat/",
        encoders = MessageEncoder.class, decoders = MessageDecoder.class)

public class Server {

    @OnOpen
    public void userConnectedCallBack(Session session){

        this.session = session;
        Message welcomeMessage = new Message("New user joined the chat!", "/none");
        try {
            session.getBasicRemote().sendObject(welcomeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnMessage
    public void messageRecieved(Message message, Session session){
        String type = message.getType();
        String content = message.getMsg();
        switch (type){
            case "/login":{
                User user = new User();
                user.setName(content);
                this.currentUser = user;
                if(isContainsUserName(user.getName())){
                    try {
                        Message errMessage = new Message();
                        errMessage.setMsg("invalid name, alredy in use:" + content);
                        session.getBasicRemote().sendObject(errMessage);
                        session.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                users.add(user);
                break;

            }
            case "/logout": {
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case "/users":{
                final Message answer = new Message();
                answer.setMsg(getUsersNames().getMsg());
                session.getOpenSessions().stream().forEach(s -> s.getAsyncRemote().sendObject(answer));
                break;
            }
            case "/message":{
                putMessage(message);
                session.getOpenSessions().stream()
                        .filter(s -> !session.getId().equals(s.getId()))
                        .forEach((s) -> s.getAsyncRemote().sendObject(message));
                break;
            }
        }

    }

    @OnClose
    public void onCloseCallBack(){
        removeUser(currentUser);
        Message goodByeMessage = new Message("---Good Bye---", "/none");
        session.getOpenSessions().stream()
                .filter(s -> s.isOpen())
                .forEach(sn -> sn.getAsyncRemote().sendObject(goodByeMessage));
    }

    public static boolean isContainsUserName(String username){
        for(User curUser : users){
            if(username.equals(curUser.getName())){
                return true;
            }
        }
        return false;
    }

    public static void putMessage(Message message){
        messageId++;
        message.setId(messageId);
        historyOfMessages.add(message);
    }

    public static void removeUser(User user){
        users.removeIf(x -> x.getName().equals(user.getName()));
    }


    public static ArrayList<User> getUsers(){
        return users;
    }

    public static Message getUsersNames(){
        String allUsers = "";
        for (User tmp : users){
            allUsers = allUsers + tmp.getName() + "\n";
        }
        Message answer = new Message();
        answer.setType("answer_message");
        answer.setMsg(allUsers);
        return answer;
    }


    private static ArrayList<Message> historyOfMessages = new ArrayList<>();
//    private static int userId = Constants.DEFAULT_ID;
    private static int messageId = Constants.DEFAULT_MSG_ID;
    private static ArrayList<User> users = new ArrayList<>();

    private User currentUser;
    private Session session;
}
