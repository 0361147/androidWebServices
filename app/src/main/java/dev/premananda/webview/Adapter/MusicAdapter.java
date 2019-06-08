package dev.premananda.webview.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

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
        holder.idMusic.setText(dataList.get(position).getIdMusic());
        holder.name.setText(dataList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return (dataList != null) ? dataList.size() : 0;
    }

    public class MusicViewHolder extends RecyclerView.ViewHolder{
        private TextView idMusic, name;

        public MusicViewHolder(View itemView) {
            super(itemView);
            idMusic = itemView.findViewById(R.id.idMusic);
            name =  itemView.findViewById(R.id.name);
        }
    }
}