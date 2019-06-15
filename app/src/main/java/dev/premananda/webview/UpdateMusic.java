package dev.premananda.webview;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import dev.premananda.webview.API.APIInterface;
import dev.premananda.webview.API.Config;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateMusic extends AppCompatActivity {

    EditText fileName, album, penyanyi, tahun;
    ProgressBar progress;
    Button save;
    String idMusic;
    Intent intentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_music);

        fileName = findViewById(R.id.fileName);
        progress = findViewById(R.id.progress);
        save = findViewById(R.id.save);
        album = findViewById(R.id.album);
        penyanyi = findViewById(R.id.penyanyi);
        tahun = findViewById(R.id.tahun);
        intentData = getIntent();
        fileName.setText(intentData.getStringExtra("name"));
        album.setText(intentData.getStringExtra("album"));
        penyanyi.setText(intentData.getStringExtra("penyanyi"));
        tahun.setText(intentData.getStringExtra("tahun"));
        idMusic = intentData.getStringExtra("idMusic");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Update Music");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void handleUpdateMusic(View view) {
        Toast.makeText(getApplicationContext(), "Updateing...", Toast.LENGTH_SHORT).show();
        save.setEnabled(false);
        progress.setVisibility(View.VISIBLE);

        String idMusic = intentData.getStringExtra("idMusic");
        String newName = fileName.getText().toString();
        String newAlbum = album.getText().toString();
        String newPenyanyi = penyanyi.getText().toString();
        String newTahun = tahun.getText().toString();


        APIInterface client = Config.getClient();
        Call<ResponseBody> updateMusic = client.updateMusic(idMusic, newName, newAlbum, newPenyanyi, newTahun);

        updateMusic.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Failed update", Toast.LENGTH_SHORT).show();
                    save.setEnabled(true);
                    progress.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                save.setEnabled(true);
                progress.setVisibility(View.GONE);
            }
        });
    }
}
