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
    private OnClickItemListener mOnClickItemListener;

    public TopTrackAdapter() {
        tracks = new ArrayList<>();
    }

    public TopTrackAdapter(List<Track> tracks, OnClickItemListener onClickItemListener) {
        this.tracks = tracks;
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
        TextView textView = holder.track_name;
        textView.setText(tracks.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public interface OnClickItemListener {
        void onTrackClick(int position, Track track);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView track_name;
        OnClickItemListener onClickItemListener;

        public ViewHolder(View view, OnClickItemListener onClickItemListener) {
            super(view);
            track_name = view.findViewById(R.id.artist_name);
            this.onClickItemListener = onClickItemListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickItemListener.onTrackClick(getAdapterPosition(), tracks.get(getAdapterPosition()));
        }
    }
}
