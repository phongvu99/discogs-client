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
import com.naughtybitch.POJO.Want;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;


public class MoreByAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Release> releases;
    private List<Want> wants;
    private OnMoreByListener mOnMoreByListener;
    private CircularProgressDrawable progressDrawable;

    public MoreByAdapter(OnMoreByListener onMoreByListener, Context context, List<Release> releases) {
        this.context = context;
        this.releases = releases;
        this.mOnMoreByListener = onMoreByListener;
    }

    public MoreByAdapter(Context context, List<Want> wants, OnMoreByListener onMoreByListener) {
        this.context = context;
        this.wants = wants;
        this.mOnMoreByListener = onMoreByListener;
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
        try {
            return releases.size();
        } catch (NullPointerException e) {
            return wants.size();
        }
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
        TextView artist = moreByViewHolder.card_artist;
        TextView title = moreByViewHolder.card_title;
        ImageView image_release = moreByViewHolder.card_image;

        // More by this artist in Master and Release Details Activity and Collection and Wishlist
        try {
            title.setText(releases.get(position).getTitle());
            artist.setText(releases.get(position).getArtist());
            Glide.with(context).load(releases.get(position).getThumb())
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .into(image_release);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            title.setText(releases.get(position).getBasicInformation().getTitle());
            artist.setText(releases.get(position).getBasicInformation().getArtists().get(0).getName());
            Glide.with(context).load(releases.get(position).getBasicInformation().getThumb())
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .into(image_release);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            title.setText(wants.get(position).getBasicInformation().getTitle());
            artist.setText(wants.get(position).getBasicInformation().getArtists().get(0).getName());
            Glide.with(context).load(wants.get(position).getBasicInformation().getThumb())
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .into(image_release);
        } catch (NullPointerException e) {
            // Do smt
        }
    }


    public interface OnMoreByListener {
        void onReleaseClick(int position, Release release);

        void onReleaseClick(int position, Want want);
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
            try {
                onMoreByListener.onReleaseClick(getAdapterPosition(), releases.get(getAdapterPosition()));
            } catch (NullPointerException e) {
                // Do smt
            }
            try {
                onMoreByListener.onReleaseClick(getAdapterPosition(), wants.get(getAdapterPosition()));
            } catch (NullPointerException e) {
                // Do smt
            }
        }
    }
}
