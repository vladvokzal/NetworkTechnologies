package Client;

import Other.ConnectionInterface;
import Other.Message;
import Other.RetroFitObj;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.exit;

public class MessageCommand{

    public void execute(String message){
        if (ClientInfo.token.isEmpty()){
            System.out.println("You need to authorise");
            exit(1);
        }

        Message msg = new Message();
        msg.setMsg(message);
        Call<Message> response = api.postMessage(ClientInfo.token, msg);

        response.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.body() == null){
                    return;
                }
                ClientInfo.addMyMsgId(response.body().getId());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable throwable) {
                System.out.println(throwable.getLocalizedMessage());
            }
        });
    }

    private ConnectionInterface api = RetroFitObj.newInstance();
}
