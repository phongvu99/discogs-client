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
import com.naughtybitch.POJO.Format;
import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Result;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom MasterViewHolder which gives us access to our views
public class ResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the results
    private List<Result> mResults;
    private Pagination mPagination;
    CircularProgressDrawable progressDrawable;
    private Context context;
    private OnResultListener mOnResultListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastPosition, totalItem;
    private int visibleThreshold = 5;

    // Pass in the result list into the constructor
    public ResultsAdapter(Context context, List<Result> results, final Pagination pagination, OnResultListener onResultListener, RecyclerView recyclerView) {
        this.context = context;
        this.mResults = results;
        this.mPagination = pagination;
        this.mOnResultListener = onResultListener;

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

    public ResultsAdapter() {
        mResults = new ArrayList<>();
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        if (mResults.get(position) == null) {
            return 5;
        }
        String viewType = mResults.get(position).getType();
        switch (viewType) {
            case "master":
                type = 1;
                break;
            case "label":
                type = 2;
                break;
            case "release":
                type = 3;
                break;
            case "artist":
                type = 4;
                break;
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return mResults.size();
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
                itemView = inflater.inflate(R.layout.card_row_master, parent, false);
                return new MasterViewHolder(itemView, mOnResultListener);
            case 2: // Label
                itemView = inflater.inflate(R.layout.card_row_label, parent, false);
                return new LabelViewHolder(itemView, mOnResultListener);
            case 3: // Release
                itemView = inflater.inflate(R.layout.card_row_release, parent, false);
                return new ReleaseViewHolder(itemView, mOnResultListener);
            case 4: // Artist
                itemView = inflater.inflate(R.layout.card_row_artist, parent, false);
                return new ArtistViewHolder(itemView, mOnResultListener);
            case 5: // Loading
                itemView = inflater.inflate(R.layout.card_row_loading, parent, false);
                return new LoadingViewHolder(itemView);
            default: // Default
                itemView = inflater.inflate(R.layout.card_row_default, parent, false);
                return new EmptyViewHolder(itemView);
        }
    }

    public void updateMaster(MasterViewHolder masterViewHolder, Result result) {
        TextView title_master = masterViewHolder.card_title;
        title_master.setText(result.getTitle());
        ImageView image_master = masterViewHolder.card_image;
        Glide.with(context).load(result.getCoverImage()).placeholder(progressDrawable)
                .error(progressDrawable)
                .into(image_master);
    }

    public void updateLabel(LabelViewHolder labelViewHolder, Result result) {
        TextView title_label = labelViewHolder.card_title;
        title_label.setText(result.getTitle());
        TextView profile_label = labelViewHolder.card_profile;
        profile_label.setText(result.getTitle());
        ImageView image_label = labelViewHolder.card_image;
        Glide.with(context).load(result.getCoverImage()).placeholder(R.drawable.record)
                .error(R.drawable.record)
                .into(image_label);
    }

    public void updateRelease(ReleaseViewHolder releaseViewHolder, Result result) {
        StringBuilder stringBuilder;
        List<Format> temp = result.getFormats();
        String quantity = temp.get(0).getQty();
        int qty = Integer.parseInt(quantity);
        stringBuilder = new StringBuilder();
        for (String desc : result.getFormat()) {
            stringBuilder.append(desc);
            if (result.getFormat().indexOf(desc) == result.getFormat().size() - 1) {
                break;
            }
            stringBuilder.append(", ");
        }
        TextView title_release = releaseViewHolder.card_title;
        title_release.setText(result.getTitle());
        TextView formats = releaseViewHolder.card_formats;
        if (qty > 1) {
            formats.setText("Formats: " + quantity + "x" + stringBuilder);
        } else {
            formats.setText("Formats: " + stringBuilder);
        }
        TextView labels = releaseViewHolder.card_labels;
        if (result.getLabel().size() > 1) {
            labels.setText("Labels: " + result.getLabel().get(0) + " +" + (result.getLabel().size() - 1) + " more");
        } else {
            labels.setText("Labels: " + result.getLabel().get(0));
        }
        TextView country = releaseViewHolder.card_country;
        if (result.getYear() == null) {
            country.setText(result.getCountry());
        } else {
            country.setText(result.getCountry() + " (" + result.getYear() + ")");
        }
        ImageView image_release = releaseViewHolder.card_image;
        Glide.with(context).load(result.getCoverImage()).placeholder(R.drawable.discogs_vinyl_record_mark)
                .error(R.drawable.discogs_vinyl_record_mark)
                .into(image_release);
    }

    public void updateArtist(ArtistViewHolder artistViewHolder, Result result) {
        TextView title_artist = artistViewHolder.card_title;
        title_artist.setText(result.getTitle());
        TextView profile_artist = artistViewHolder.card_profile;
        profile_artist.setText(result.getTitle());
        CircleImageView image_artist = artistViewHolder.card_image;
        Glide.with(context).load(result.getCoverImage()).placeholder(R.drawable.microphone)
                .error(R.drawable.microphone)
                .dontAnimate().into(image_artist);
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        progressDrawable = new CircularProgressDrawable(context);
        progressDrawable.setStrokeWidth(5f);
        progressDrawable.setCenterRadius(30f);
        progressDrawable.start();
        Result result = mResults.get(position);
        // Get the data model based on position
        switch (getItemViewType(position)) {
            case 1: // Master
                // Set item views based on your views and data model
                MasterViewHolder masterViewHolder = (MasterViewHolder) holder;
                updateMaster(masterViewHolder, result);
                break;
            case 2: // Label
                LabelViewHolder labelViewHolder = (LabelViewHolder) holder;
                updateLabel(labelViewHolder, result);
                break;
            case 3: // Release
                ReleaseViewHolder releaseViewHolder = (ReleaseViewHolder) holder;
                updateRelease(releaseViewHolder, result);
                break;
            case 4: // Artist
                ArtistViewHolder artistViewHolder = (ArtistViewHolder) holder;
                updateArtist(artistViewHolder, result);
                break;
            case 5: // Loading
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
                ProgressBar progressBar = loadingViewHolder.progressBar;
                progressBar.setIndeterminate(true);
                break;
        }
    }

    public interface OnResultListener {
        void onResultClick(int position, Result result);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class MasterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView card_title;
        private ImageView card_image;
        private OnResultListener onResultListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public MasterViewHolder(View itemView, OnResultListener onResultListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any MasterViewHolder instance.
            super(itemView);

            this.onResultListener = onResultListener;
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onResultListener.onResultClick(getAdapterPosition(), mResults.get(getAdapterPosition()));
        }
    }

    public class LabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView card_title, card_profile;
        private ImageView card_image;
        private OnResultListener onResultListener;

        public LabelViewHolder(View itemView, OnResultListener onResultListener) {

            super(itemView);

            this.onResultListener = onResultListener;
            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_profile = (TextView) itemView.findViewById(R.id.card_profile);
            card_image = (ImageView) itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onResultListener.onResultClick(getAdapterPosition(), mResults.get(getAdapterPosition()));
        }
    }

    public class ReleaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView card_title, card_formats, card_labels, card_country;
        private ImageView card_image;
        private OnResultListener onResultListener;

        public ReleaseViewHolder(View itemView, OnResultListener onResultListener) {

            super(itemView);
            this.onResultListener = onResultListener;
            card_title = itemView.findViewById(R.id.card_title);
            card_formats = itemView.findViewById(R.id.card_formats);
            card_labels = itemView.findViewById(R.id.card_labels);
            card_country = itemView.findViewById(R.id.card_country);
            card_image = itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onResultListener.onResultClick(getAdapterPosition(), mResults.get(getAdapterPosition()));
        }
    }

    public class EmptyViewHolder extends RecyclerView.ViewHolder {
        private TextView card_empty;

        public EmptyViewHolder(View itemView) {
            super(itemView);

            card_empty = itemView.findViewById(R.id.card_empty);
        }
    }

    public class ArtistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView card_title, card_profile;
        private CircleImageView card_image;
        private OnResultListener onResultListener;

        public ArtistViewHolder(View itemView, OnResultListener onResultListener) {
            super(itemView);

            this.onResultListener = onResultListener;
            card_title = itemView.findViewById(R.id.card_title);
            card_profile = itemView.findViewById(R.id.card_profile);
            card_image = itemView.findViewById(R.id.card_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onResultListener.onResultClick(getAdapterPosition(), mResults.get(getAdapterPosition()));
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


