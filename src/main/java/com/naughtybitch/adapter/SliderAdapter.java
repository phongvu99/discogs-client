package com.naughtybitch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.naughtybitch.POJO.MasterResponse;
import com.naughtybitch.discogsclient.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private MasterResponse masterResponse;

    public SliderAdapter(Context context, MasterResponse masterResponse) {
        this.context = context;
        this.masterResponse = masterResponse;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_item, null);
        return new SliderAdapterVH(view);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {
        Glide.with(context)
                .load(masterResponse.getImages().get(position).getResourceUrl())
                .into(viewHolder.imageViewBackground);
    }


    @Override
    public int getCount() {
        return masterResponse.getImages().size();
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
