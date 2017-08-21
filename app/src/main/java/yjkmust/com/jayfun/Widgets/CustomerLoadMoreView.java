package yjkmust.com.jayfun.Widgets;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

import yjkmust.com.jayfun.R;


/**
 * Created by GEOFLY on 2017/8/18.
 */

public class CustomerLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return R.layout.view_load_more;
    }

    @Override
    protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override
    protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    @Override
    protected int getLoadEndViewId() {
        return R.id.load_more_load_end_view;
    }
}
