package Client;

import Other.ConnectionInterface;
import Other.Message;
import Other.RetroFitObj;
import Other.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class MessageListCommand extends TimerTask {

    private ConnectionInterface api = RetroFitObj.newInstance();

    @Override
    public void run() {
        if (ClientInfo.token.isEmpty()){
            return;
        }

        Call<ArrayList<Message>> response = api.getMessages(ClientInfo.token, ClientInfo.getLastMsgId(), 12);
        response.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ArrayList<Message>> call, Response<ArrayList<Message>> response) {
                ArrayList<Message> msgList = response.body();
                if (msgList != null){
                    for (Message msg : msgList){
                        if (!ClientInfo.containsId(msg.getAuthorId())){
                            try {
                                ArrayList<User> resp = api.getUsers(ClientInfo.token).execute().body();
                                ClientInfo.clearAllUsers();
                                resp.forEach(user -> ClientInfo.addUser(user));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        ClientInfo.addMessage(msg);
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Message>> call, Throwable throwable) {
                System.out.println(throwable.getLocalizedMessage());
            }
        });
    }
}
