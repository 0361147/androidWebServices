package dev.premananda.webview.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Config {

    public  static String FILE_URL = "http://192.168.1.67/musicFull/";
    public static String BASE_URL = "http://192.168.1.67/musicFull/api/";
    private static Retrofit retrofit;

    public static Retrofit initReftofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static APIInterface getClient() {
        return  initReftofit().create(APIInterface.class);
    }

}
