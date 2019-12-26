package com.naughtybitch.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJOlastfm.Artist;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;

public class TopArtistAdapter extends RecyclerView.Adapter<TopArtistAdapter.ViewHolder> {

    private List<Artist> artists;
    private OnClickItemListener mOnClickItemListener;

    public TopArtistAdapter() {
        artists = new ArrayList<>();
    }

    public TopArtistAdapter(List<Artist> artists, OnClickItemListener onClickItemListener) {
        this.artists = artists;
        this.mOnClickItemListener = onClickItemListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.artist_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, mOnClickItemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView artist_name = holder.artist_name;
        artist_name.setText(artists.get(position).getName());
        TextView artist_position = holder.artist_position;
        artist_position.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public interface OnClickItemListener {
        void onArtistClick(int position, Artist artist);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView artist_name, artist_position;

        OnClickItemListener onClickItemListener;

        public ViewHolder(View view, OnClickItemListener onClickItemListener) {
            super(view);
            artist_position = view.findViewById(R.id.artist_position);
            artist_name = view.findViewById(R.id.artist_name);
            this.onClickItemListener = onClickItemListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickItemListener.onArtistClick(getAdapterPosition(), artists.get(getAdapterPosition()));
        }
    }
}
