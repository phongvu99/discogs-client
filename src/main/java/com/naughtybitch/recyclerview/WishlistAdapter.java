package com.naughtybitch.recyclerview;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.naughtybitch.POJO.Pagination;
import com.naughtybitch.POJO.Want;

import java.util.List;

public class WishlistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    CircularProgressDrawable progressDrawable;
    // Store a member variable for the results
    private List<Want> mWants;
    private Pagination mPagination;
    private Context context;
    private ArtistReleaseAdapter.OnLoadMoreListener mOnLoadMoreListener;
    private boolean isLoading;
    private int lastPosition, totalItem;
    private int visibleThreshold = 5;


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
