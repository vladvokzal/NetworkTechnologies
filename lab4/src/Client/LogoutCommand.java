package Client;

import Other.ConnectionInterface;
import Other.Message;
import Other.RetroFitObj;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.System.exit;

public class LogoutCommand implements AbstractCommand {
    @Override
    public void execute() {
        if (!ClientInfo.isOnline){
            System.out.println("You are not in the chat");
            exit(1);
        }

        Call<Message> response = api.postLogout(ClientInfo.token);

        response.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                if (response.body() == null){
                    return;
                }
                ClientInfo.clearInfo();
                System.out.println(response.message());
            }

            @Override
            public void onFailure(Call<Message> call, Throwable throwable) {
                System.out.println(throwable.getLocalizedMessage());
            }
        });
    }

    private ConnectionInterface api = RetroFitObj.newInstance();
}
