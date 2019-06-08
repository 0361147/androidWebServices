package dev.premananda.webview.API;

import dev.premananda.webview.Model.Music;
import dev.premananda.webview.Model.MusicResult;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    @GET("music.php")
    Call<MusicResult> getMusics();

    @Multipart
    @POST("music.php")
    Call<Music> postMusic(
            @Part("name") String name,
            @Part MultipartBody.Part files
    );

}
