package com.naughtybitch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.MasterReleaseResponse;
import com.naughtybitch.POJO.ReleaseResponse;
import com.naughtybitch.discogsclient.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private MasterReleaseResponse masterResponse;
    private ReleaseResponse releaseResponse;

    public SliderAdapter(Context context, MasterReleaseResponse masterResponse) {
        this.context = context;
        this.masterResponse = masterResponse;
    }

    public SliderAdapter(Context context, ReleaseResponse releaseResponse) {
        this.context = context;
        this.releaseResponse = releaseResponse;
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
                    .override(600, 600)
                    .error(R.color.light_black)
                    .placeholder(R.color.light_black)
                    .into(viewHolder.imageViewBackground);
        } catch (NullPointerException e) {
            // Do smt
        }
        try {
            Glide.with(context)
                    .load(releaseResponse.getImages().get(position).getResourceUrl())
                    .override(600, 600)
                    .error(R.color.light_black)
                    .placeholder(R.color.light_black)
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
        return 0;
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
