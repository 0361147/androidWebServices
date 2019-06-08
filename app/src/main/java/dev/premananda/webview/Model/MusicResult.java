package dev.premananda.webview.Model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MusicResult {

    @SerializedName("music")
    @Expose
    private ArrayList<Music> music = null;

    public ArrayList<Music> getMusic() {
        return music;
    }

    public void setMusic(ArrayList<Music> music) {
        this.music = music;
    }

}
