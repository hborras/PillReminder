package com.plaglabs.pillreminder.app;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plagueis on 10/05/14.
 */
public class ImagesPageAdapter extends PagerAdapter {

    ArrayList<Image> mImages = new ArrayList<Image>();
    Context context;
    public ImagesPageAdapter(Context context,ArrayList<Image> mImages) {
        this.mImages = mImages;
        this.context    = context;
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        int padding = 2;
        imageView.setPadding(padding, padding, padding, padding);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        imageView.setImageResource(mImages.get(position).getrID());
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }
}
