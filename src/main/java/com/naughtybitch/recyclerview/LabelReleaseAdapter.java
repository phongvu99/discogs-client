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

public class LabelReleaseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    CircularProgressDrawable progressDrawable;
    // Store a member variable for the results
    private List<Release> mReleases;
    private Pagination mPagination;
    private Context context;
    private OnLabelReleaseListener mOnLabelReleaseListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastPosition, totalItem;
    private int visibleThreshold = 5;

    public LabelReleaseAdapter(Context context, List<Release> releases, Pagination pagination, OnLabelReleaseListener onLabelReleaseListener, RecyclerView recyclerView) {
        this.context = context;
        this.mReleases = releases;
        this.mPagination = pagination;
        this.mOnLabelReleaseListener = onLabelReleaseListener;

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

    @Override
    public int getItemViewType(int position) {
        int type = 1;
        if (mReleases.get(position) == null) {
            return 0;
        }
        return type;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 0: // Loading
                itemView = inflater.inflate(R.layout.card_row_loading, parent, false);
                return new LoadingViewHolder(itemView);
            case 1: // Release
            default:
                itemView = inflater.inflate(R.layout.label_release, parent, false);
                return new LabelReleaseViewHolder(itemView, mOnLabelReleaseListener);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                ProgressBar progressBar = loadingViewHolder.progressBar;
                progressBar.setIndeterminate(true);
                break;
            case 1:
                TextView title, formats, cat, released;
                ImageView image;

                LabelReleaseViewHolder labelReleaseViewHolder = (LabelReleaseViewHolder) holder;
                title = labelReleaseViewHolder.release_title;
                formats = labelReleaseViewHolder.release_formats;
                cat = labelReleaseViewHolder.release_cat;
                released = labelReleaseViewHolder.release_released;
                image = labelReleaseViewHolder.release_image;
                title.setText(mReleases.get(position).getTitle() + " - " + mReleases.get(position).getArtist());
                formats.setText("Format: " + mReleases.get(position).getFormat());
                try {
                    if (mReleases.get(position).getCatno().equals("\"none,\"")) {
                        cat.setText("Cat# " + "none");
                    } else {
                        cat.setText("Cat# " + mReleases.get(position).getCatno());
                    }
                } catch (NullPointerException e) {
                    cat.setText("Cat# " + "none");
                }
                try {
                    if (mReleases.get(position).getYear() == 0) {
                        released.setText("Released in " + "unknown");
                    } else {
                        released.setText("Released in " + mReleases.get(position).getYear());
                    }
                } catch (NullPointerException e) {
                    released.setText("Released in " + "unknown");
                }
                Glide.with(context).load(mReleases.get(position).getThumb())
                        .error(R.drawable.discogs_vinyl_record_mark)
                        .placeholder(R.drawable.discogs_vinyl_record_mark)
                        .into(image);
                break;
        }
    }

    public int getItemCount() {
        return mReleases.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading() {
        isLoading = true;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnLabelReleaseListener {
        void OnLabelReleaseClick(int position, Release release);
    }

    public class LabelReleaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView release_title, release_formats, release_cat, release_released;
        private ImageView release_image;
        private OnLabelReleaseListener onLabelReleaseListener;

        public LabelReleaseViewHolder(@NonNull View itemView, OnLabelReleaseListener onLabelReleaseListener) {

            super(itemView);

            this.onLabelReleaseListener = onLabelReleaseListener;
            release_title = itemView.findViewById(R.id.label_title);
            release_formats = itemView.findViewById(R.id.label_formats);
            release_cat = itemView.findViewById(R.id.label_cat);
            release_released = itemView.findViewById(R.id.label_released);
            release_image = itemView.findViewById(R.id.label_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onLabelReleaseListener.OnLabelReleaseClick(getAdapterPosition(), mReleases.get(getAdapterPosition()));
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
