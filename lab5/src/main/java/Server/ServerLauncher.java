package Server;

import javax.websocket.DeploymentException;
import java.util.Scanner;

public final class ServerLauncher {

    public void runServer() throws Exception {
        server = new org.glassfish.tyrus.server.Server("localhost",
                8080, "/ws", null, Server.class);
        try{
            server.start();
            System.out.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();

        }catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            stopServer();
        }

    }

    public void stopServer(){
        server.stop();
    }

    public static void main(String[] args) throws Exception {
        new ServerLauncher().runServer();
    }

    private org.glassfish.tyrus.server.Server server;
}
