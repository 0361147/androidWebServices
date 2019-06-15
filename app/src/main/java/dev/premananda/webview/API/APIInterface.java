package dev.premananda.webview.API;

import dev.premananda.webview.Model.Music;
import dev.premananda.webview.Model.MusicResult;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;

public interface APIInterface {

    @GET("music.php")
    Call<MusicResult> getMusics();

    @Multipart
    @POST("music.php")
    Call<Music> postMusic(
            @Part("name") String name,
            @Part("album") String album,
            @Part("penyanyi") String penyanyi,
            @Part("tahun") String tahun,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part files
    );

    @PUT("music.php")
    @FormUrlEncoded
    Call<ResponseBody> updateMusic(
            @Field("idMusic") String idMusic,
            @Field("name") String name,
            @Field("album") String album,
            @Field("penyanyi") String penyanyi,
            @Field("tahun") String tahun
    );

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "music.php", hasBody = true)
    Call<ResponseBody> deleteMusic(@Field("idMusic") String idMusic);

}
