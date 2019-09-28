package Other;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.ArrayList;

public interface ConnectionInterface {

    @Headers("Content-Type: application/json")
    @POST("login")
    Call<User> postUser(@Body User userName);

    @POST("logout")
    Call<Message> postLogout(@Header("Authorization") String token);

    @GET("users")
    Call<ArrayList<User>> getUsers(@Header("Authorization") String token);

    @Headers("Content-Type: application/json")
    @POST("messages")
    Call<Message> postMessage(@Header("Authorization") String token, @Body Message message);

    @GET("messages")
    Call<ArrayList<Message>> getMessages
            (@Header("Authorization") String token,
             @Query("offset") int currentId, @Query("count") int count);
}
