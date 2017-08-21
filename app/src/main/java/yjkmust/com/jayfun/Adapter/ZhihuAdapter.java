package yjkmust.com.jayfun.Adapter;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import java.util.List;
import yjkmust.com.jayfun.Bean.ZhiHuStoryBean;
import yjkmust.com.jayfun.R;

/**
 * Created by GEOFLY on 2017/8/17.
 */

public class ZhihuAdapter extends BaseQuickAdapter<ZhiHuStoryBean,BaseViewHolder> {
    public ZhihuAdapter(List<ZhiHuStoryBean> data) {
        super( R.layout.item_rv, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, ZhiHuStoryBean bean) {
        helper.setText(R.id.tv_title, bean.getBean().getTitle());
        helper.setText(R.id.tv_date, bean.getDate());
        Glide.with(mContext).load(bean.getBean().getImages().get(0)).crossFade().into((ImageView) helper.getView(R.id.iv_img));
    }
}
