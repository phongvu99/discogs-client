package com.naughtybitch.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naughtybitch.POJO.ParentLabel;
import com.naughtybitch.POJO.Sublabel;
import com.naughtybitch.discogsclient.R;

import java.util.List;

public class LabelCompaniesAdapter extends ArtistMGAdapter {

    private List<Sublabel> mSublabels;
    private ParentLabel mParentLabel;
    private OnSublabelListener mOnSublabelListener; // Parent label
    private OnParentLabelListener mOnParentLabelListener; // Sublabel

    public LabelCompaniesAdapter(List<Sublabel> sublabels
            , OnSublabelListener onSublabelListener) {
        this.mSublabels = sublabels;
        this.mOnSublabelListener = onSublabelListener;

    }

    public LabelCompaniesAdapter(ParentLabel parentLabel
            , OnParentLabelListener onParentLabelListener) {
        this.mParentLabel = parentLabel;
        this.mOnParentLabelListener = onParentLabelListener;

    }

    @Override
    public int getItemViewType(int position) {
        int type = 0;
        try { // Parent label
            if (mSublabels != null) {
                type = 1; // Parent label
                return type;
            }
        } catch (NullPointerException e1) {
            // Do smt
            try {
                if (mParentLabel != null) {
                    Log.i("type2", "type2");
                    return 2;
                }
            } catch (NullPointerException e2) {
                // Do smt
            }
        }
        return 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView;
        switch (viewType) {
            case 1: // Parent label
                itemView = inflater.inflate(R.layout.label_sp, parent, false);
                return new SublabelViewHolder(itemView, mOnSublabelListener);
            case 2: // Sublabel
            default:
                itemView = inflater.inflate(R.layout.label_sp, parent, false);
                return new ParentLabelViewHolder(itemView, mOnParentLabelListener);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TextView title, status;
        switch (getItemViewType(position)) {
            case 1: // Parent label
                SublabelViewHolder sublabelViewHolder = (SublabelViewHolder) holder;
                title = sublabelViewHolder.label_title;
                status = sublabelViewHolder.label_status;
                title.setText(mSublabels.get(position).getName());
                status.setText(R.string.label_status1);
                break;
            case 2: // Sublabel
                ParentLabelViewHolder parentLabelViewHolder = (ParentLabelViewHolder) holder;
                title = parentLabelViewHolder.parent_title;
                status = parentLabelViewHolder.parent_status;
                status.setText(R.string.label_status2);
                try {
                    title.setText(mParentLabel.getName());
                } catch (NullPointerException e) {
                    // Do smt
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        try {
            return mSublabels.size();
        } catch (NullPointerException e1) {
            // Do smt
            try {
                if (mParentLabel != null) {
                    return 1;
                }
            } catch (NullPointerException e2) {
                // Do smt
            }
        }
        return 0;
    }

    public interface OnParentLabelListener {
        void onParentLabelClick(int position, ParentLabel parentLabel);
    }

    public interface OnSublabelListener {
        void onSublabelClick(int position, Sublabel sublabel);
    }

    public class SublabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnSublabelListener onSublabelListener;
        private TextView label_title, label_status;

        public SublabelViewHolder(@NonNull View itemView, OnSublabelListener onSublabelListener) {
            super(itemView);

            this.onSublabelListener = onSublabelListener;
            itemView.setOnClickListener(this);
            label_title = itemView.findViewById(R.id.label_title);
            label_status = itemView.findViewById(R.id.label_status);
        }

        @Override
        public void onClick(View v) {
            onSublabelListener.onSublabelClick(getAdapterPosition(), mSublabels.get(getAdapterPosition()));
        }
    }

    public class ParentLabelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private OnParentLabelListener onParentLabelListener;
        private TextView parent_title, parent_status;

        public ParentLabelViewHolder(@NonNull View itemView, OnParentLabelListener onParentLabelListener) {
            super(itemView);

            this.onParentLabelListener = onParentLabelListener;
            itemView.setOnClickListener(this);
            parent_title = itemView.findViewById(R.id.label_title);
            parent_status = itemView.findViewById(R.id.label_status);
        }

        @Override
        public void onClick(View v) {
            onParentLabelListener.onParentLabelClick(getAdapterPosition(), mParentLabel);
        }
    }
}

