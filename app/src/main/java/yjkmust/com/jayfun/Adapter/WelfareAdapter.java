package yjkmust.com.jayfun.Adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.IllegalFormatCodePointException;

import yjkmust.com.jayfun.BaseAdapter.BaseRecyclerViewAdapter;
import yjkmust.com.jayfun.BaseAdapter.BaseRecyclerViewHolder;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.DensityUtil;
import yjkmust.com.jayfun.databinding.ItemWelfareBinding;

/**
 * Created by GEOFLY on 2017/7/12.
 */

public class WelfareAdapter extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {

    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ViewHolder(viewGroup, R.layout.item_welfare);
    }
    public class ViewHolder extends BaseRecyclerViewHolder<GankIoDataBean.ResultBean , ItemWelfareBinding> {
        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean object, final int position) {
            /**
             * 注意：DensityUtil.setViewMargin(itemView,true,5,3,5,0);
             * 如果这样使用，则每个item的左右边距是不一样的，
             * 这样item不能复用，所以下拉刷新成功后显示会闪一下
             * 换成每个item设置上下左右边距是一样的话，系统就会复用，就消除了图片不能复用 闪跳的情况
             */
            if (position % 2 == 0) {
                DensityUtil.setViewMargin(itemView, false, 12, 6, 12, 0);
            } else {
                DensityUtil.setViewMargin(itemView, false, 6, 12, 12, 0);
            }
            Glide.with(itemView.getContext()).load(object.getUrl()) .crossFade(1000).thumbnail(0.1f)
                    .placeholder(R.drawable.img_default_meizi).error(R.drawable.img_default_meizi)
                    .into(binding.ivWelfare);
            binding.executePendingBindings();
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener!=null){
                        listener.onClick(object,position);
                    }

                }
            });
        }
    }

}
