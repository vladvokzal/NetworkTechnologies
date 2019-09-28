package Client;

import Other.Message;
import Other.MessageDecoder;
import Other.MessageEncoder;

import javax.websocket.OnMessage;

@javax.websocket.ClientEndpoint(encoders = MessageEncoder.class, decoders = MessageDecoder.class)


public class ClientEndpoint {

    @OnMessage
    public void onMessage(Message message){
        System.out.println(message.getMsg());
    }
}
