package com.naughtybitch.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJOlastfm.Track;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;

public class TopTrackAdapter extends RecyclerView.Adapter<TopTrackAdapter.ViewHolder> {

    private List<Track> tracks;
    private OnItemClickListener mOnItemClickListener;

    public TopTrackAdapter() {
        tracks = new ArrayList<>();
    }

    public TopTrackAdapter(List<Track> tracks, OnItemClickListener onItemClickListener) {
        this.tracks = tracks;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.artist_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view, mOnItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView track_name = holder.track_name;
        track_name.setText(tracks.get(position).getName());
        TextView track_artist = holder.track_artist;
        TextView track_position = holder.track_position;
        track_artist.setText(tracks.get(position).getArtist().getName());
        track_position.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public interface OnItemClickListener {
        void onTrackClick(int position, Track track);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView track_name, track_position, track_artist;
        OnItemClickListener onItemClickListener;

        public ViewHolder(View view, OnItemClickListener onItemClickListener) {
            super(view);
            track_name = view.findViewById(R.id.track_name);
            track_position = view.findViewById(R.id.artist_position);
            track_artist = view.findViewById(R.id.artist_name);
            this.onItemClickListener = onItemClickListener;

            track_name.setVisibility(View.VISIBLE);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onTrackClick(getAdapterPosition(), tracks.get(getAdapterPosition()));
        }
    }
}
