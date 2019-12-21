package com.naughtybitch.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJO.Tracklist;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;

public class TracklistAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Tracklist> tracklists;

    public TracklistAdapter(Context context, List<Tracklist> tracklists) {
        this.context = context;
        this.tracklists = tracklists;
    }

    public TracklistAdapter() {
        tracklists = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return tracklists.size();
    }

    @Override
    public int getItemViewType(int position) {
        int type;
        switch (tracklists.get(position).getType()) {
            case "heading":
                type = 1;
                break;
            case "track":
                type = 2;
                break;
            case "index":
                type = 3;
                break;
            default:
                type = 0;
                break;
        }
        return type;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 1: // Header
                itemView = inflater.inflate(R.layout.tracklist_track_header, parent, false);
                return new HeaderViewHolder(itemView);
            case 2: // Body
            case 3:
            default:
                itemView = inflater.inflate(R.layout.tracklist_track_title, parent, false);
                return new BodyViewHolder(itemView);

        }
    }

    private void updateHeader(HeaderViewHolder headerViewHolder, Tracklist tracklist) {
        TextView track_header = headerViewHolder.track_header;
        track_header.setText(tracklist.getTitle());
    }

    private void updateBody(BodyViewHolder bodyViewHolder, Tracklist tracklist) {
        TextView track_number = bodyViewHolder.track_number;
        track_number.setText(tracklist.getPosition());
        TextView track_title = bodyViewHolder.track_title;
        if (tracklist.getType().equals("index")) {
            track_number.setText(String.valueOf(getItemCount()));
            track_title.setText(tracklist.getTitle());
        }
        try {
            String track_artist = tracklist.getArtists().get(0).getName();
            track_title.setText(track_artist + " - " + tracklist.getTitle());
        } catch (NullPointerException e) {
            track_title.setText(tracklist.getTitle());
        }
        TextView track_length = bodyViewHolder.track_length;
        try {
            track_length.setText(tracklist.getDuration());
        } catch (NullPointerException e) {
            track_length.setText(" ");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Tracklist tracklist = tracklists.get(position);
        switch (getItemViewType(position)) {
            case 1: // Header
                HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
                updateHeader(headerViewHolder, tracklist);
                break;
            case 2: // Body
            case 3:
                BodyViewHolder bodyViewHolder = (BodyViewHolder) holder;
                updateBody(bodyViewHolder, tracklist);
                break;
        }
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {

        private TextView track_title, track_number, track_length;

        public BodyViewHolder(View itemView) {
            super(itemView);

            track_title = itemView.findViewById(R.id.track_title);
            track_number = itemView.findViewById(R.id.track_number);
            track_length = itemView.findViewById(R.id.track_length);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        private TextView track_header;

        public HeaderViewHolder(View itemView) {
            super(itemView);

            track_header = itemView.findViewById(R.id.track_header);

        }
    }

}
