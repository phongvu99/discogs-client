package com.naughtybitch.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Release;
import com.naughtybitch.discogsclient.R;

import java.util.List;


public class ArtistReleaseAdapter extends MoreByAdapter {

    CircularProgressDrawable progressDrawable;
    // Store a member variable for the results
    private List<Release> mReleases;
    private Pagination mPagination;
    private Context context;
    private OnArtistReleaseListener mOnArtistReleaseListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastPosition, totalItem;
    private int visibleThreshold = 5;

    // Pass in the result list into the constructor
    public ArtistReleaseAdapter(Context context, List<Release> releases, Pagination pagination
            , OnArtistReleaseListener onArtistReleaseListener, RecyclerView recyclerView) {
        this.context = context;
        this.mReleases = releases;
        this.mPagination = pagination;
        this.mOnArtistReleaseListener = onArtistReleaseListener;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItem = linearLayoutManager.getItemCount();
                lastPosition = linearLayoutManager.findLastVisibleItemPosition();
                Log.i("total_item", "Total item" + totalItem);
                if (totalItem != mPagination.getItems() && !isLoading) {
                    if (totalItem <= (lastPosition + visibleThreshold)) {
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                        setLoading();
                    }
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (mReleases.get(position) == null) {
            return 0;
        }
        String viewType = mReleases.get(position).getType();
        switch (viewType) {
            case "master":
                type = 1;
                break;
            case "release":
                type = 2;
                break;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return mReleases.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading() {
        isLoading = true;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 1: // Master
            case 2: // Release
                itemView = inflater.inflate(R.layout.card_row_master, parent, false);
                return new MasterViewHolder(itemView, mOnArtistReleaseListener);
            default: // Default
                itemView = inflater.inflate(R.layout.card_row_loading, parent, false);
                return new LoadingViewHolder(itemView);
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateMaster(MasterViewHolder masterViewHolder, Release release) {
        TextView title_master = masterViewHolder.card_title;
        title_master.setText(release.getTitle());
        TextView artist = masterViewHolder.card_artist;
        artist.setText(release.getArtist());
        TextView released = masterViewHolder.card_released;
        try {
            released.setText("First released in " + release.getYear());
        } catch (NullPointerException e) {
            released.setText("First released in " + "unknown");
        }
        ImageView image_master = masterViewHolder.card_image;
        Glide.with(context).load(release.getThumb()).placeholder(R.drawable.discogs_vinyl_record_mark)
                .error(R.drawable.discogs_vinyl_record_mark)
                .into(image_master);
    }

    @SuppressLint("SetTextI18n")
    private void updateRelease(MasterViewHolder masterViewHolder, Release release) {
        TextView title_master = masterViewHolder.card_title;
        title_master.setText(release.getTitle() + " (" + release.getFormat() + ")");
        TextView artist = masterViewHolder.card_artist;
        artist.setText(release.getArtist());
        TextView released = masterViewHolder.card_released;
        try {
            released.setText("Released in " + release.getYear());
        } catch (NullPointerException e) {
            released.setText("Released in " + "unknown");
        }
        ImageView image_master = masterViewHolder.card_image;
        Glide.with(context).load(release.getThumb()).placeholder(R.drawable.discogs_vinyl_record_mark)
                .error(R.drawable.discogs_vinyl_record_mark)
                .into(image_master);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        progressDrawable = new CircularProgressDrawable(context);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();
        Release release = mReleases.get(position);
        // Get the data model based on position
        switch (getItemViewType(position)) {
            case 0: // Loading
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                ProgressBar progressBar = loadingViewHolder.progressBar;
                progressBar.setIndeterminate(true);
                break;
            case 1: // Master
                MasterViewHolder masterViewHolder = (MasterViewHolder) holder;
                updateMaster(masterViewHolder, release);
                break;
            case 2: // Release
                // Set item views based on your views and data model
                masterViewHolder = (MasterViewHolder) holder;
                updateRelease(masterViewHolder, release);
                break;
        }
    }

    public interface OnArtistReleaseListener {
        void onArtistReleaseClick(int position, Release release);
    }


    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class MasterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView card_title, card_artist, card_released;
        private ImageView card_image;
        private OnArtistReleaseListener onArtistReleaseListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public MasterViewHolder(View itemView, OnArtistReleaseListener onArtistReleaseListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any MasterViewHolder instance.
            super(itemView);

            this.onArtistReleaseListener = onArtistReleaseListener;
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            card_artist = itemView.findViewById(R.id.card_type);
            card_released = itemView.findViewById(R.id.show_all);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onArtistReleaseListener.onArtistReleaseClick(getAdapterPosition(), mReleases.get(getAdapterPosition()));
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

}



