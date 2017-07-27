package yjkmust.com.jayfun.Adapter;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import yjkmust.com.jayfun.BaseAdapter.BaseRecyclerViewAdapter;
import yjkmust.com.jayfun.BaseAdapter.BaseRecyclerViewHolder;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.ImgLoadUtil;
import yjkmust.com.jayfun.WebViewActivity;
import yjkmust.com.jayfun.databinding.ItemAndroidBinding;

/**
 * Created by GEOFLY on 2017/7/5.
 */

public class AndroidAdapter extends BaseRecyclerViewAdapter<GankIoDataBean.ResultBean> {
    private boolean isAll = false;
    public void setAllType(boolean isAll){
        this.isAll = isAll;
    }
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent,R.layout.item_android);
    }
    private class ViewHolder extends BaseRecyclerViewHolder<GankIoDataBean.ResultBean,ItemAndroidBinding> {
        public ViewHolder(ViewGroup viewGroup, int layoutId) {
            super(viewGroup, layoutId);
        }

        @Override
        public void onBindViewHolder(final GankIoDataBean.ResultBean object, int position) {
            if (isAll&&"福利".equals(object.getType())){
                binding.ivAllWelfare.setVisibility(View.VISIBLE);
                binding.llWelfareOther.setVisibility(View.GONE);
                ImgLoadUtil.displayEspImage(object.getUrl(),binding.ivAllWelfare,1);
            }else {
                binding.ivAllWelfare.setVisibility(View.GONE);
                binding.llWelfareOther.setVisibility(View.VISIBLE);
            }
            if (isAll) {
                binding.tvAndroidType.setVisibility(View.VISIBLE);
                binding.tvAndroidType.setText(" · " + object.getType());
            } else {
                binding.tvAndroidType.setVisibility(View.GONE);

            }
            binding.setResultBean(object);
            binding.executePendingBindings();
            // 显示gif图片会很耗内存
            if (object.getImages() != null
                    && object.getImages().size() > 0
                    && !TextUtils.isEmpty(object.getImages().get(0))) {
//                binding.ivAndroidPic.setVisibility(View.GONE);
                binding.ivAndroidPic.setVisibility(View.VISIBLE);
                ImgLoadUtil.displayGif(object.getImages().get(0), binding.ivAndroidPic);
            } else {
                binding.ivAndroidPic.setVisibility(View.GONE);
            }

            binding.llAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    WebViewActivity.loadUrl(v.getContext(), object.getUrl(), "加载中...");
                }
            });

        }
    }
}
