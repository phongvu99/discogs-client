package com.naughtybitch.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.naughtybitch.discogsclient.R;


public class GenreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] genres;
    private Integer[] items;
    private Integer[] covers, icons;
    private OnGenreListener mOnGenreListener;
    private Context context;

    public GenreAdapter(@NonNull Context context, String[] genres, Integer[] items, Integer[] covers, Integer[] icons, OnGenreListener onGenreListener) {
        this.context = context;
        this.genres = genres;
        this.items = items;
        this.covers = covers;
        this.icons = icons;
        this.mOnGenreListener = onGenreListener;
    }


    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.explore_genre_row, parent, false);
        return new GenreViewHolder(itemView, mOnGenreListener);
    }

    // Involves populating data into the item through holder
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GenreViewHolder genreViewHolder = (GenreViewHolder) holder;
        TextView genre_title = genreViewHolder.genre;
        TextView genre_items = genreViewHolder.items;
        ImageView genre_icon = genreViewHolder.icon;
        ImageView genre_cover = genreViewHolder.cover;
        genre_title.setText(genres[position]);
        genre_items.setText(Integer.toString(items[position]));
        Glide.with(context).load(covers[position]).override(150, 150).into(genre_cover);
        Glide.with(context).load(icons[position]).override(64, 64).into(genre_icon);

    }

    @Override
    public int getItemCount() {
        return genres.length;
    }

    public interface OnGenreListener {
        void onGenreClick(int position, String genre);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class GenreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView genre, items;
        private ImageView cover, icon;
        private OnGenreListener onGenreListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public GenreViewHolder(View itemView, OnGenreListener onGenreListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any MasterViewHolder instance.
            super(itemView);

            this.onGenreListener = onGenreListener;
            genre = itemView.findViewById(R.id.genre_title);
            items = itemView.findViewById(R.id.genre_items);
            cover = itemView.findViewById(R.id.genre_cover);
            icon = itemView.findViewById(R.id.genre_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onGenreListener.onGenreClick(getAdapterPosition(), genres[getAdapterPosition()]);
        }
    }


}




