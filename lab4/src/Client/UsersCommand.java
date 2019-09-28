package Client;

import Other.ConnectionInterface;
import Other.RetroFitObj;
import Other.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

import static java.lang.System.exit;

public class UsersCommand implements AbstractCommand {
    @Override
    public void execute() {
        if (ClientInfo.token.isEmpty()){
            System.out.println("You need to authorise");
            return;
        }

        Call<ArrayList<User>> response = api.getUsers(ClientInfo.token);

        response.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.body() == null){
                    return;
                }
                ClientInfo.clearAllUsers();
                for(User user : response.body()){
                    ClientInfo.addUser(user);
                    System.out.println(user.getName() + user.isOnline() + " id = " + user.getId());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable throwable) {
                System.out.println(throwable.getLocalizedMessage());
            }
        });
    }


    private ConnectionInterface api = RetroFitObj.newInstance();
}
