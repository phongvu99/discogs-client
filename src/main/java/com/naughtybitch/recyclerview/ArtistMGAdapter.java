package com.naughtybitch.recyclerview;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.Group;
import com.naughtybitch.POJO.Member;
import com.naughtybitch.discogsclient.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ArtistMGAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Member> mMembers;
    private List<Group> mGroups;
    private OnArtistGroupListener mOnArtistGroupListener; // Member
    private OnArtistMemberListener mOnArtistMemberListener; // Group

    // Pass in the result list into the constructor
    public ArtistMGAdapter(Context context, List<Group> groups
            , OnArtistGroupListener onArtistGroupListener) {
        this.context = context;
        this.mGroups = groups;
        this.mOnArtistGroupListener = onArtistGroupListener;

    }

    public ArtistMGAdapter(Context context, List<Member> members
            , OnArtistMemberListener onArtistMemberListener) {
        this.context = context;
        this.mMembers = members;
        this.mOnArtistMemberListener = onArtistMemberListener;

    }

    public ArtistMGAdapter() {
        mMembers = new ArrayList<>();
        mGroups = new ArrayList<>();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 1: // Group
                itemView = inflater.inflate(R.layout.artist_mg, parent, false);
                return new ArtistGroupHolder(itemView, mOnArtistGroupListener);
            case 2: // Member
            default: // Default
                itemView = inflater.inflate(R.layout.artist_mg, parent, false);
                return new ArtistMemberHolder(itemView, mOnArtistMemberListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CircleImageView image;
        TextView title, status;
        switch (getItemViewType(position)) {
            case 1: // Group
                Group group = mGroups.get(position);
                ArtistGroupHolder artistGroupHolder = (ArtistGroupHolder) holder;
                image = artistGroupHolder.group_image;
                title = artistGroupHolder.group_title;
                status = artistGroupHolder.group_status;
                Glide.with(context).load(group.getThumbnailUrl()).error(R.drawable.discogs_vinyl_record_mark)
                        .placeholder(R.drawable.discogs_vinyl_record_mark).into(image);
                title.setText(group.getName());
                if (group.getActive()) {
                    status.setText("Active member");
                } else {
                    title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    status.setText("No longer an active member");
                }
                break;
            case 2: // Member
                Member member = mMembers.get(position);
                ArtistMemberHolder artistMemberHolder = (ArtistMemberHolder) holder;
                image = artistMemberHolder.member_image;
                title = artistMemberHolder.member_title;
                status = artistMemberHolder.member_status;
                Glide.with(context).load(member.getThumbnailUrl()).error(R.drawable.discogs_vinyl_record_mark)
                        .placeholder(R.drawable.discogs_vinyl_record_mark).into(image);
                title.setText(member.getName());
                if (member.getActive()) {
                    status.setText("Active member");
                } else {
                    title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    status.setText("No longer an active member");
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int type = 0, size;
        try {
            size = mGroups.size();
            type = 1; // Group
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            size = mMembers.size();
            type = 2; // Member
        } catch (NullPointerException e) {
            // Do smt
        }
        return type;
    }

    @Override
    public int getItemCount() {
        try {
            return mMembers.size();
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            return mGroups.size();
        } catch (NullPointerException e) {
            // Do smt
        }
        return 0;
    }

    public interface OnArtistMemberListener {
        void onArtistMemberClick(int position, Member member);
    }

    public interface OnArtistGroupListener {
        void onArtistGroupClick(int position, Group group);
    }

    public class ArtistMemberHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnArtistMemberListener onArtistMemberListener;
        private CircleImageView member_image;
        private TextView member_title, member_status;

        public ArtistMemberHolder(@NonNull View itemView, OnArtistMemberListener onArtistMemberListener) {
            super(itemView);

            this.onArtistMemberListener = onArtistMemberListener;
            itemView.setOnClickListener(this);
            member_image = itemView.findViewById(R.id.artist_image);
            member_title = itemView.findViewById(R.id.artist_title);
            member_status = itemView.findViewById(R.id.artist_status);
        }

        @Override
        public void onClick(View v) {
            onArtistMemberListener.onArtistMemberClick(getAdapterPosition(), mMembers.get(getAdapterPosition()));
        }
    }

    public class ArtistGroupHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnArtistGroupListener onArtistGroupListener;
        private CircleImageView group_image;
        private TextView group_title, group_status;

        public ArtistGroupHolder(@NonNull View itemView, OnArtistGroupListener onArtistGroupListener) {
            super(itemView);

            this.onArtistGroupListener = onArtistGroupListener;
            itemView.setOnClickListener(this);
            group_image = itemView.findViewById(R.id.artist_image);
            group_title = itemView.findViewById(R.id.artist_title);
            group_status = itemView.findViewById(R.id.artist_status);
        }

        @Override
        public void onClick(View v) {
            onArtistGroupListener.onArtistGroupClick(getAdapterPosition(), mGroups.get(getAdapterPosition()));
        }
    }
}
