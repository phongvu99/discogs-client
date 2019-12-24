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
import com.naughtybitch.POJO.Format;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Want;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    CircularProgressDrawable progressDrawable;
    // Store a member variable for the results
    private List<Want> mWants;
    private Pagination mPagination;
    private Context context;
    private OnWishlistListener mOnWishlistListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastPosition, totalItem;
    private int visibleThreshold = 5;

    public WishlistAdapter() {
        mWants = new ArrayList<>();
    }

    public WishlistAdapter(Context context, List<Want> wants,
                           Pagination pagination, OnWishlistListener onWishlistListener,
                           RecyclerView recyclerView) {
        this.context = context;
        this.mWants = wants;
        this.mPagination = pagination;
        this.mOnWishlistListener = onWishlistListener;

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

    public void setLoaded() {
        isLoading = false;
    }

    public void setLoading() {
        isLoading = true;
    }

    @Override
    public int getItemViewType(int position) {
        if (mWants.get(position) == null) {
            return 0;
        }
        return 1;
    }

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
            default: // Wishlist
                itemView = inflater.inflate(R.layout.card_row_master, parent, false);
                return new WishlistViewHolder(itemView, mOnWishlistListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        progressDrawable = new CircularProgressDrawable(context);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();
        Want want = mWants.get(position);
        switch (getItemViewType(position)) {
            case 0: // Loading
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                ProgressBar progressBar = loadingViewHolder.progressBar;
                progressBar.setIndeterminate(true);
                break;
            default:
                WishlistViewHolder wishlistViewHolder = (WishlistViewHolder) holder;
                updateWishlist(wishlistViewHolder, want);
        }
    }

    @SuppressLint("SetTextI18n")
    public void updateWishlist(WishlistViewHolder wishlistViewHolder, Want want) {
        TextView title = wishlistViewHolder.card_title;
        TextView artist = wishlistViewHolder.card_artist;
        TextView format = wishlistViewHolder.card_formats;
        TextView released = wishlistViewHolder.card_released;
        ImageView image = wishlistViewHolder.card_image;
        title.setText(want.getBasicInformation().getTitle());
        artist.setText(want.getBasicInformation().getArtists().get(0).getName());
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder temp = new StringBuilder();
        try {
            for (Format f : want.getBasicInformation().getFormats()) {
                temp.append(" (");
                for (String s : f.getDescriptions()) {
                    temp.append(s);
                    if (f.getDescriptions().indexOf(s) == (f.getDescriptions().size() - 1)) {
                        temp.append(")");
                        break;
                    }
                    temp.append(" , ");
                }
                stringBuilder.append(f.getName());
                stringBuilder.append(temp);
                if (want.getBasicInformation().getFormats().indexOf(f) == (want.getBasicInformation().getFormats().size() - 1)) {
                    break;
                }
                stringBuilder.append(" , ");
                temp = new StringBuilder();
            }
            format.setText("Format: " + stringBuilder);
        } catch (NullPointerException e) {
            format.setText("Format: Unknown");
        }
        try {
            released.setText("Released in " + want.getBasicInformation().getYear());
            if (want.getBasicInformation().getYear() == 0) {
                released.setText("Unknown release date");
            }
        } catch (NullPointerException e) {
            // Do smt
            released.setText("Unknown release date");
        }
        Glide.with(context).load(want.getBasicInformation().getThumb()).
                error(R.drawable.discogs_vinyl_record_mark).placeholder(R.drawable.discogs_vinyl_record_mark)
                .into(image);
    }

    @Override
    public int getItemCount() {
        return mWants.size();
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnWishlistListener {
        void onWishlistClick(int position, Want want);
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView card_title, card_artist, card_released, card_formats;
        private ImageView card_image;
        private OnWishlistListener onWishlistListener;

        public WishlistViewHolder(@NonNull View itemView, OnWishlistListener onWishlistListener) {
            super(itemView);

            this.onWishlistListener = onWishlistListener;
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            card_formats = itemView.findViewById(R.id.card_formats);
            card_formats.setVisibility(View.VISIBLE);
            card_artist = itemView.findViewById(R.id.card_type);
            card_released = itemView.findViewById(R.id.show_all);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onWishlistListener.onWishlistClick(getAdapterPosition(), mWants.get(getAdapterPosition()));
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
