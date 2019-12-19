package com.naughtybitch.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;


public class MoreByAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Release> releases;
    private OnMoreByListener mOnMoreByListener;
    private CircularProgressDrawable progressDrawable;
    private int artist_id;

    public MoreByAdapter(Context context, List<Release> releases, int artist_id, OnMoreByListener onMoreByListener) {
        this.context = context;
        this.releases = releases;
        this.mOnMoreByListener = onMoreByListener;
        this.artist_id = artist_id;
    }

    public MoreByAdapter() {
        releases = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return releases.size();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.moreby_artist, parent, false);
        return new MoreByViewHolder(itemView, mOnMoreByListener);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        progressDrawable = new CircularProgressDrawable(context);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();
        MoreByViewHolder moreByViewHolder = (MoreByViewHolder) holder;
        TextView title = moreByViewHolder.card_title;
        title.setText(releases.get(position).getTitle());
        TextView artist = moreByViewHolder.card_artist;
        artist.setText(releases.get(position).getArtist());
        ImageView image_release = moreByViewHolder.card_image;
        Glide.with(context).load(releases.get(position).getThumb())
                .placeholder(R.drawable.discogs_vinyl_record_mark)
                .error(R.drawable.discogs_vinyl_record_mark)
                .into(image_release);
    }


    public interface OnMoreByListener {
        void onReleaseClick(int position, Release release, List<Release> releases, int artist_id);
    }

    public class MoreByViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnMoreByListener onMoreByListener;
        private TextView card_title, card_artist;
        private ImageView card_image;

        public MoreByViewHolder(View itemView, OnMoreByListener onMoreByListener) {
            super(itemView);
            this.onMoreByListener = onMoreByListener;
            itemView.setOnClickListener(this);
            card_title = itemView.findViewById(R.id.card_title);
            card_artist = itemView.findViewById(R.id.card_artist);
            card_image = itemView.findViewById(R.id.card_image);
        }

        @Override
        public void onClick(View v) {
            onMoreByListener.onReleaseClick(getAdapterPosition(), releases.get(getAdapterPosition()), releases, artist_id);
        }
    }
}
