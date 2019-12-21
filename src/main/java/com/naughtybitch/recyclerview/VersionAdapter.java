package com.naughtybitch.recyclerview;

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
import com.naughtybitch.POJO.Version;
import com.naughtybitch.discogsclient.R;

import java.util.List;

public class VersionAdapter extends ResultsAdapter {

    CircularProgressDrawable progressDrawable;
    // Store a member variable for the results
    private List<Version> mVersions;
    private Pagination mPagination;
    private Context context;
    private OnLoadMoreListener mOnLoadMoreListener;
    private OnVersionListener mOnVersionListener;
    private boolean isLoading;
    private int lastPosition, totalItem;
    private int visibleThreshold = 5;

    public VersionAdapter(Context context, List<Version> versions, Pagination pagination, OnVersionListener onVersionListener, RecyclerView recyclerView) {
        this.context = context;
        this.mVersions = versions;
        this.mPagination = pagination;
        this.mOnVersionListener = onVersionListener;

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
        if (mVersions.get(position) == null) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return mVersions.size();
    }

    @Override
    public void setLoaded() {
        isLoading = false;
    }

    @Override
    public void setLoading() {
        isLoading = true;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 1: // Loading
                itemView = inflater.inflate(R.layout.card_row_loading, parent, false);
                return new LoadingViewHolder(itemView);
            default: // Default
                itemView = inflater.inflate(R.layout.card_row_release, parent, false);
                return new ReleaseViewHolder(itemView, mOnVersionListener);
        }
    }

    private void updateRelease(ReleaseViewHolder releaseViewHolder, Version version) {
        ImageView image_release = releaseViewHolder.card_image;
        TextView formats = releaseViewHolder.card_formats;
        formats.setVisibility(View.GONE);
        TextView title = releaseViewHolder.card_title;
        title.setText(version.getMajorFormats().get(0) + " (" + version.getFormat() + ")");
        TextView label = releaseViewHolder.card_labels;
        label.setText("Label: " + version.getLabel() + " (#" + version.getCatno() + ")");
        TextView released = releaseViewHolder.card_released;
        if (version.getReleased().equals("0") && !version.getCountry().equals("")) {
            released.setText("Released in " + version.getCountry());
        } else if (version.getCountry().equals("") && version.getReleased().equals("0")) {
            released.setText("Released in " + "Unknown country " + "(Unknown year)");
        } else {
            released.setText("Released in " + version.getCountry() + " (" + version.getReleased() + ")");
        }
        Glide.with(context).load(version.getThumb()).placeholder(R.drawable.discogs_vinyl_record_mark)
                .error(R.drawable.discogs_vinyl_record_mark)
                .into(image_release);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Version version = mVersions.get(position);
        // Get the data model based on position
        switch (getItemViewType(position)) {
            case 1: // Loading
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                ProgressBar progressBar = loadingViewHolder.progressBar;
                progressBar.setIndeterminate(true);
                break;
            default:
                ReleaseViewHolder releaseViewHolder = (ReleaseViewHolder) holder;
                updateRelease(releaseViewHolder, version);
        }

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnVersionListener {
        void onVersionClick(int position, Version version);
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);

            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public class ReleaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView card_title, card_labels, card_released, card_formats;
        private ImageView card_image;
        private OnVersionListener onVersionListener;

        public ReleaseViewHolder(View itemView, OnVersionListener onVersionListener) {

            super(itemView);
            this.onVersionListener = onVersionListener;
            card_formats = itemView.findViewById(R.id.card_formats);
            card_title = itemView.findViewById(R.id.card_title);
            card_labels = itemView.findViewById(R.id.card_labels);
            card_released = itemView.findViewById(R.id.card_country);
            card_image = itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onVersionListener.onVersionClick(getAdapterPosition(), mVersions.get(getAdapterPosition()));
        }
    }

}
