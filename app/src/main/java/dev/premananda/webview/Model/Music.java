package dev.premananda.webview.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Music {

    @SerializedName("idMusic")
    @Expose
    private String idMusic;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("album")
    @Expose
    private String album;
    @SerializedName("penyanyi")
    @Expose
    private String penyanyi;
    @SerializedName("imgUrl")
    @Expose
    private String imgUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("tahun")
    @Expose
    private String tahun;

    public String getIdMusic() {
        return idMusic;
    }

    public void setIdMusic(String idMusic) {
        this.idMusic = idMusic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getPenyanyi() {
        return penyanyi;
    }

    public void setPenyanyi(String penyanyi) {
        this.penyanyi = penyanyi;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTahun() {
        return tahun;
    }

    public void setTahun(String tahun) {
        this.tahun = tahun;
    }

}
