package dev.premananda.webview;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import dev.premananda.webview.API.APIInterface;
import dev.premananda.webview.API.Config;
import dev.premananda.webview.Adapter.MusicAdapter;
import dev.premananda.webview.Model.Music;
import dev.premananda.webview.Model.MusicResult;
import dev.premananda.webview.Utils.PathUtil;
import dev.premananda.webview.Utils.RecyclerItemClickListener;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    APIInterface client = Config.getClient();
    MusicAdapter musicAdapter;
    ArrayList<Music> musicList;
    Button browse;
    RecyclerView rcMusic;
    ProgressBar rcProgress;
    MediaPlayer audioPlayer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcMusic = findViewById(R.id.rcMusic);
        rcProgress = findViewById(R.id.rcProgress);
        browse = findViewById(R.id.browse);

        musicAdapter = new MusicAdapter(musicList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rcMusic.setLayoutManager(layoutManager);
        rcMusic.setAdapter(musicAdapter);

        rcMusic.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), rcMusic, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // do whatever
                String audioUrl = musicList.get(position).getUrl().replaceAll(" ", "%20");
                initAudioPlayer(audioUrl);
                audioPlayer.start();
            }

            @Override
            public void onLongItemClick(View view, final int position) {

            }
        }));

        setMusicAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            if (data != null) {
                Uri uri = data.getData();
                System.out.println(PathUtil.getPathFromUri(getApplicationContext(), uri));

                File file = new File(PathUtil.getPathFromUri(getApplicationContext(), uri));

                RequestBody fileReqBody = RequestBody.create(MediaType.parse("audio/*"), file);
                MultipartBody.Part part = MultipartBody.Part.createFormData("files", file.getName(), fileReqBody);

                Call<Music> call = client.postMusic("test up form android", part);
                call.enqueue(new Callback<Music>() {
                    @Override
                    public void onResponse(Call<Music> call, Response<Music> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "berhasil", Toast.LENGTH_LONG).show();
                            setMusicAdapter();
                        } else {
                            Toast.makeText(getApplicationContext(), "gagal", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Music> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "cek koneksi", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }
    }

    @Override
    protected void onDestroy() {
        stopCurrentPlayAudio();
        super.onDestroy();
    }

    private void setMusicAdapter() {
        Call<MusicResult> musicResultCall = client.getMusics();
        musicResultCall.enqueue(new Callback<MusicResult>() {
            @Override
            public void onResponse(Call<MusicResult> call, Response<MusicResult> response) {
                if (response.isSuccessful()) {
                    musicList = response.body().getMusic();
                    rcProgress.setVisibility(View.GONE);
                    rcMusic.setAdapter(new MusicAdapter(response.body().getMusic()));
                } else {
                    Toast.makeText(getApplicationContext(), "Gagal mengambil data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MusicResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Tidak dapat mengakses server", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void stopCurrentPlayAudio() {
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    private void initAudioPlayer(String url) {
        try {
            stopCurrentPlayAudio();
            audioPlayer = new MediaPlayer();
            audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            audioPlayer.setDataSource(Config.FILE_URL + url);
            System.out.println(Config.FILE_URL + url);
            audioPlayer.prepare();
        } catch (IOException ex) {
            Log.e("Init palyer", ex.getMessage(), ex);
        }
    }

    public void browseFile(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, 1);
    }
}
