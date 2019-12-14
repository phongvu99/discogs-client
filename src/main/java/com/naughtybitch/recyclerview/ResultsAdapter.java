package com.naughtybitch.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.discogsclient.R;
import com.naughtybitch.discogsclient.Result;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ViewHolder> {

    // Store a member variable for the results
    private List<Result> mResults;

    // Pass in the result list into the constructor
    public ResultsAdapter(List<Result> results) {
        mResults = results;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ResultsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View itemView = inflater.inflate(R.layout.card_row, parent, false);

        // Return a new holder instance
        return new ViewHolder(itemView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        Result result = mResults.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.card_title;
        textView.setText(result.getTitle());
        TextView textView1 = holder.card_genre;
        textView1.setText(result.getCountry());
        CircleImageView imageView = holder.card_image;
//        Glide.with(SearchableActivity.getInstance()).load(result.getCoverImage())
//                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return mResults.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        private TextView card_title;
        private TextView card_genre;
        private CircleImageView card_image;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            card_title = (TextView) itemView.findViewById(R.id.card_title);
            card_genre = (TextView) itemView.findViewById(R.id.card_genre);
            card_image = (CircleImageView) itemView.findViewById(R.id.card_image);

        }
    }
}


