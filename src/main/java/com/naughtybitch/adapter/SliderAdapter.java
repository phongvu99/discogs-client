package com.naughtybitch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.ArtistResponse;
import com.naughtybitch.POJO.LabelResponse;
import com.naughtybitch.POJO.MasterReleaseResponse;
import com.naughtybitch.POJO.ReleaseResponse;
import com.naughtybitch.discogsclient.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private MasterReleaseResponse masterResponse;
    private ReleaseResponse releaseResponse;
    private ArtistResponse artistResponse;
    private LabelResponse labelResponse;

    public SliderAdapter(Context context, MasterReleaseResponse masterResponse) {
        this.context = context;
        this.masterResponse = masterResponse;
    }

    public SliderAdapter(Context context, ReleaseResponse releaseResponse) {
        this.context = context;
        this.releaseResponse = releaseResponse;
    }

    public SliderAdapter(Context context, ArtistResponse artistResponse) {
        this.context = context;
        this.artistResponse = artistResponse;
    }

    public SliderAdapter(Context context, LabelResponse labelResponse) {
        this.context = context;
        this.labelResponse = labelResponse;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, null);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        try {
            Glide.with(context)
                    .load(masterResponse.getImages().get(position).getResourceUrl())
                    .override(300, 300)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .into(viewHolder.imageViewBackground);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            Glide.with(context)
                    .load(releaseResponse.getImages().get(position).getResourceUrl())
                    .override(300, 300)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .into(viewHolder.imageViewBackground);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            Glide.with(context)
                    .load(artistResponse.getImages().get(position).getResourceUrl())
                    .override(300, 300)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .into(viewHolder.imageViewBackground);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            Glide.with(context)
                    .load(labelResponse.getImages().get(position).getResourceUrl())
                    .override(300, 300)
                    .error(R.drawable.discogs_vinyl_record_mark)
                    .placeholder(R.drawable.discogs_vinyl_record_mark)
                    .into(viewHolder.imageViewBackground);
        } catch (NullPointerException e) {
            // Do smt
        }
    }


    @Override
    public int getCount() {
        try {
            return masterResponse.getImages().size();
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            return releaseResponse.getImages().size();
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            return artistResponse.getImages().size();
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            return labelResponse.getImages().size();
        } catch (NullPointerException e) {
            // Do smt
        }
        return 1;
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {
        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            this.itemView = itemView;
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
        }
    }
}
