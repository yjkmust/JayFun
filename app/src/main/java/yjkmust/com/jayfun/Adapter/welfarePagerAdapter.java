package yjkmust.com.jayfun.Adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import yjkmust.com.jayfun.App.MyApplication;
import yjkmust.com.jayfun.R;

/**
 * Created by GEOFLY on 2017/7/12.
 */

public class welfarePagerAdapter extends PagerAdapter {
    List<String> data;
    LayoutInflater inflater;
    Activity mActivity;

    public welfarePagerAdapter(List<String> data,LayoutInflater inflater,Activity activity) {
        this.data = data;
        this.inflater = inflater;
        mActivity = activity;

    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.item_wealfarepic_viewpager, container, false);
        final PhotoView photoView = (PhotoView) view.findViewById(R.id.pv_image);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        //照片网址
        String imgUrl = (String) getItem(position);
        progressBar.setClickable(false);
        Glide.with(container.getContext()).load(imgUrl).placeholder(R.drawable.img_default_meizi).error(R.drawable.img_default_meizi)
                .crossFade(1000).thumbnail(0.1f).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                Toast.makeText(container.getContext(), "资源加载异常", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.GONE);

                /**这里应该是加载成功后图片的高*/
                int height = photoView.getHeight();


                int wHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
                if (height > wHeight) {
                    photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } else {
                    photoView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
                return false;
            }
        }).into(photoView);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        if (data==null||data.size()==0){
            return 0;
        }else {
            return data.size();
        }

    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
    Object getItem(int position){
        return data.get(position);
    }


}
