package dev.premananda.webview.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import dev.premananda.webview.API.Config;
import dev.premananda.webview.Model.Music;
import dev.premananda.webview.R;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
    private ArrayList<Music> dataList;

    public MusicAdapter(ArrayList<Music> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.music_llist, parent, false);

        return new MusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MusicViewHolder holder, int position) {
        holder.name.setText(dataList.get(position).getName());
        holder.album.setText(dataList.get(position).getAlbum());
        holder.penyanyi.setText(dataList.get(position).getPenyanyi());

        String url = Config.FILE_URL + dataList.get(position).getImgUrl().replaceAll(" ", "%20");
        Log.d("url", url);
        Picasso
                .get()
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .resize(200, 200)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        private TextView name;
        private TextView penyanyi;
        private TextView album;
        private ImageView cover;

        public MusicViewHolder(View itemView) {
            super(itemView);
            name =  itemView.findViewById(R.id.fileName);
            penyanyi = itemView.findViewById(R.id.penyanyi);
            album = itemView.findViewById(R.id.album);
            cover = itemView.findViewById(R.id.cover);
        }
    }
}