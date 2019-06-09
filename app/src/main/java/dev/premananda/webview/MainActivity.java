package dev.premananda.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
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
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    APIInterface client = Config.getClient();
    MusicAdapter musicAdapter;
    ArrayList<Music> musicList;
    RecyclerView rcMusic;
    ProgressBar rcProgress;
    MediaPlayer audioPlayer = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addMusic:
                Intent intent = new Intent(getApplicationContext(), AddMusic.class);
                startActivityForResult(intent, 2);
                break;

            case R.id.refreshMusic:
                setMusicAdapter();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rcMusic = findViewById(R.id.rcMusic);
        rcProgress = findViewById(R.id.rcProgress);

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
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                builderSingle.setTitle("Action");

                final CharSequence[] items = { "Edit", "Delete" };

                builderSingle.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handleRcMusicLongPress(i, musicList.get(position));
                    }
                });
                builderSingle.show();
            }
        }));

        setMusicAdapter();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 2) {
            setMusicAdapter();
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
                    Toast.makeText(getApplicationContext(), "Failed get data form server", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MusicResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
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

    private void handleRcMusicLongPress(Integer index, Music music) {
        switch (index) {
            case 0:
                handleUpdateMusic(music);
                break;
            case 1:
                handleDeleteMusic(music);
                break;
        }
    }

    private void handleDeleteMusic(Music music) {
        final Call<ResponseBody> deleteMusic = client.deleteMusic(music.getIdMusic());

        final AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Delete " + music.getName());
        builderSingle.setMessage("Deleted music cant be restored");
        builderSingle.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Deleteing...", Toast.LENGTH_SHORT).show();
                deleteMusic.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Music has deleted", Toast.LENGTH_SHORT).show();
                            setMusicAdapter();
                        } else {
                            Toast.makeText(getApplicationContext(), "Failed to delete music", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builderSingle.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builderSingle.show();
    }

    private void handleUpdateMusic(Music music) {
        Intent intent = new Intent(getApplicationContext(), UpdateMusic.class);
        intent.putExtra("idMusic", music.getIdMusic());
        intent.putExtra("name", music.getName());
        startActivityForResult(intent, 2);
    }
}
