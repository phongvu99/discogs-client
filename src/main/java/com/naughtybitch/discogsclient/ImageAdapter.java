package com.naughtybitch.discogsclient;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


public class ImageAdapter extends PagerAdapter {
    private Context context;

    private int[] sliderImageId = new int[]
            {R.drawable.weather_icon_002_sunny, R.drawable.weather_icon_004_rainy, R.drawable.weather_icon_005_heavy_rain, R.drawable.weather_icon_006_storm, R.drawable.weather_icon_007_thunder};

    public ImageAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return sliderImageId.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageResource(sliderImageId[position]);
        container.addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}