package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

public class Client {
    public static void main(String[] args) throws IOException {
        Timer msgListTimer = new Timer("MSG LIST TIMER");
        msgListTimer.schedule(new MessageListCommand(), 1000L, 1000L);

        addCommand("/login", new LoginCommand());
        addCommand("/logout", new LogoutCommand());
        addCommand("/users", new UsersCommand());

        Map<String, AbstractCommand> map = getCommands();
        while (Thread.currentThread().isAlive()){
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String str = br.readLine();
            if (map.containsKey(str)){
                map.get(str).execute();
            }
            else {
                messageCommand.execute(str);
            }
        }
        msgListTimer.cancel();
    }

    private static void addCommand(String commandName, AbstractCommand command){
        if (!commands.containsKey(commandName)){
            commands.put(commandName, command);
        }
    }

    public static Map<String, AbstractCommand> getCommands(){
        return commands;
    }

    private static Map<String, AbstractCommand> commands = new HashMap<>();
    private static MessageCommand messageCommand = new MessageCommand();
}
