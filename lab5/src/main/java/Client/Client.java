package Client;

import Other.Message;
import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.Session;
import java.net.URI;
import java.util.ArrayList;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws Exception {
        ClientManager client = ClientManager.createClient();

        addCommand("/users");
        addCommand("/logout");
        addCommand("/login");
        addCommand("/message");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Write your name:");
        Session session = client.connectToServer(ClientEndpoint.class, new URI(SERVER));
        do {
            String input = scanner.nextLine();
            String type = input.substring(0, input.indexOf(" "));
            String content = input.substring(input.indexOf(" ") + 1, input.length());
            Message userMsg = new Message();
            if (commands.contains(type)){
                userMsg.setType(type);
            }
            userMsg.setMsg(content);
            session.getBasicRemote().sendObject(userMsg);
        } while (true);

    }

    private static void addCommand(String command){
        if (!commands.contains(command)){
            commands.add(command);
        }
    }

    public static final String SERVER = "ws://localhost:8080/ws/chat/";
    private static ArrayList<String> commands = new ArrayList<>();
}
