package dev.premananda.webview;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;

import dev.premananda.webview.API.APIInterface;
import dev.premananda.webview.API.Config;
import dev.premananda.webview.Model.Music;
import dev.premananda.webview.Utils.PathUtil;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddMusic extends AppCompatActivity {

    EditText fileName;
    Button browse;
    Button save;
    ProgressBar progress;
    File selectedFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);

        fileName = findViewById(R.id.fileName);
        browse = findViewById(R.id.browse);
        save = findViewById(R.id.save);
        progress = findViewById(R.id.progress);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            if (data != null) {
                selectedFile = new File(PathUtil.getPathFromUri(getApplicationContext(), data.getData()));
                String name = selectedFile.getName();
                fileName.setText(name.substring(0, name.lastIndexOf('.')));
            }
        }
    }

    public void browseMusic(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("audio/*");
        startActivityForResult(intent, 1);
    }

    public void postMusic(View view) {
        Toast.makeText(getApplicationContext(), "Uploading...", Toast.LENGTH_SHORT).show();
        save.setEnabled(false);
        progress.setVisibility(View.VISIBLE);

        APIInterface client = Config.getClient();
        String name = fileName.getText().toString();

        RequestBody fileReqBody = RequestBody.create(MediaType.parse("audio/*"), selectedFile);
        MultipartBody.Part part = MultipartBody.Part.createFormData(
                "files",
                selectedFile.getName(),
                fileReqBody
        );

        Call<Music> call = client.postMusic(name, part);
        Log.d("Name", name);
        call.enqueue(new Callback<Music>() {
            @Override
            public void onResponse(Call<Music> call, Response<Music> response) {
                if (response.isSuccessful()) {
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();

                } else {
                    Toast.makeText(getApplicationContext(), "Failed upload music", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                    save.setEnabled(true);
                }
            }

            @Override
            public void onFailure(Call<Music> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Please check your connection", Toast.LENGTH_SHORT).show();
                progress.setVisibility(View.GONE);
                save.setEnabled(true);
            }
        });
    }
}
