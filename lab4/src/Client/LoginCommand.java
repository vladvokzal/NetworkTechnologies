package Client;

import Other.ConnectionInterface;
import Other.RetroFitObj;
import Other.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static java.lang.System.exit;

public class LoginCommand implements AbstractCommand {
    @Override
    public void execute() {
        if (ClientInfo.isOnline){
            System.out.println("You are already online");
            exit(1);
        }

        System.out.println("Write your name:");
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            String userName = br.readLine();

            User user = new User();
            user.setName(userName);
            Call<User> response = api.postUser(user);

            response.enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() == null){
                        //System.out.println("response is null");
                        return;
                    }
                    ClientInfo.setInfo(response.body());
                    System.out.println("You are in the chat!");
                }

                @Override
                public void onFailure(Call<User> call, Throwable throwable) {
                    System.out.println("Fail: " + throwable.getLocalizedMessage());
                }
            });

        } catch (IOException e) {
            System.out.println("IOExeption" + e.getLocalizedMessage());
        }

    }

    private ConnectionInterface api = RetroFitObj.newInstance();
}
