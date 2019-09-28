package Other;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetroFitObj {

    public static ConnectionInterface newInstance(){
        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://localhost:8080/")
                .build();
        return retrofit.create(ConnectionInterface.class);
    }

    private static Retrofit retrofit;
}
