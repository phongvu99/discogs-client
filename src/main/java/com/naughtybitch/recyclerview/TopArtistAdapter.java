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
        TextView textView = holder.artist_name;
        textView.setText(artists.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public interface OnClickItemListener {
        void onArtistClick(int position, Artist artist);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView artist_name;
        OnClickItemListener onClickItemListener;

        public ViewHolder(View view, OnClickItemListener onClickItemListener) {
            super(view);
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
