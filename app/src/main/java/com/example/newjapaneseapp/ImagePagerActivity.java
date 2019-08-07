package com.example.newjapaneseapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

public class ImagePagerActivity extends PagerAdapter {

    private ArrayList<Integer> Images;
    private LayoutInflater inflater;
    private Context context;

    public ImagePagerActivity(ArrayList<Integer> images, Context context) {
        Images = images;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return Images.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages, view, false);

        final ImageView imageView = imageLayout.findViewById(R.id.image);

        imageView.setImageResource(Images.get(position));

        view.addView(imageLayout, 0);

        return imageLayout;
    }


}
