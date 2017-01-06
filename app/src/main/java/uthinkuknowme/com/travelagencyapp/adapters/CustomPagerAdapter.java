package uthinkuknowme.com.travelagencyapp.adapters;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.makeramen.roundedimageview.RoundedImageView;

import uthinkuknowme.com.travelagencyapp.DetailsActivity;
import uthinkuknowme.com.travelagencyapp.R;


/**
 * Created by Antonio on 29.12.2016.
 */

public class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;

    public CustomPagerAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return DetailsActivity.mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        RoundedImageView imageView = (RoundedImageView) itemView.findViewById(R.id.imageView);
        imageView.setImageResource(DetailsActivity.mResources[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
