package yjkmust.com.jayfun.Fragments.GankFragments;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;


import com.example.xrecyclerview.XRecyclerView;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yjkmust.com.jayfun.App.Constants;
import yjkmust.com.jayfun.Bean.FrontpageBean;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Fragments.BaseFragment;
import yjkmust.com.jayfun.Interfa.EverdayBannerService;
import yjkmust.com.jayfun.Interfa.GankIoCustomService;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.GlideImageLoader;
import yjkmust.com.jayfun.WebViewActivity;
import yjkmust.com.jayfun.databinding.FragmentEverydayBinding;
import yjkmust.com.jayfun.databinding.HeaderItemEverydayBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class EveryDayFragment extends BaseFragment<FragmentEverydayBinding> {

    private RotateAnimation animation;
    private HeaderItemEverydayBinding mHeaderBinding;
    private View mHeaderView;
    private XRecyclerView xrvEveryday;
    private ArrayList<String> mBannerImages;
    private boolean mIsPrepared = false;
    private boolean mIsFirst = true;
    public static String TAG = "TestLog";

    @Override
    public int setContent() {
        return R.layout.fragment_everyday;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        bindingView.llLoading.setVisibility(View.VISIBLE);
        animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(3000);//设置动画持续时间
        animation.setInterpolator(new LinearInterpolator());//不停顿
        animation.setRepeatCount(10);
        bindingView.ivLoading.setAnimation(animation);
        animation.startNow();
        initRecyclerView();
        mIsPrepared = true;
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();

    }

    @Override
    protected void loadData() {
        Log.d(TAG, "loadData1: ");
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.startAutoPlay();
            mHeaderBinding.banner.setDelayTime(3000);
        }
        if (!mIsVisible || !mIsPrepared) {
            return;
        }
        showLoading(false);
        loadBannerPicture();
        showContentData();
        Log.d(TAG, "loadData2: ");
    }

    /**
     * 加载XReclerView的内容
     */
    private void showContentData() {
    }

    /**
     * 加载轮播图图片
     */
    private void loadBannerPicture() {
        String baseURL = "https://tingapi.ting.baidu.com/v1/restserver/";
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseURL)
                .build();
        EverdayBannerService service = retrofit.create(EverdayBannerService.class);
        service.getFrontpage()
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())//请求完成后再io线程中执行
                .doOnNext(new Action1<FrontpageBean>() {
                    @Override
                    public void call(FrontpageBean frontpageBean) {
                        save();
                    }
                }).observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<FrontpageBean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, "onError: ");
                    }

                    @Override
                    public void onNext(FrontpageBean frontpageBean) {
                        Log.d(TAG, "onNext: ");
                        if (mBannerImages == null) {
                            mBannerImages = new ArrayList<>();
                        } else {
                            mBannerImages.clear();
                        }
                        if (frontpageBean != null && frontpageBean.getResult() != null && frontpageBean.getResult().getFocus() != null && frontpageBean.getResult().getFocus().getResult() != null) {
                            final List<FrontpageBean.ResultBeanXXXXXXXXXXXXXX.FocusBean.ResultBeanX> result = frontpageBean.getResult().getFocus().getResult();
                            if (result != null && result.size() > 0) {
                                for (int i = 0; i < result.size(); i++) {
                                    mBannerImages.add(result.get(i).getRandpic());
                                }
                                mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
                                Log.d(TAG, "setImageLoader: ");
                                mHeaderBinding.banner.setOnBannerClickListener(new OnBannerClickListener() {
                                    @Override
                                    public void OnBannerClick(int position) {
                                        position = position - 1;
                                        //链接没有做缓存，如果轮播图使用缓存则点击图片无效
                                        if (result.get(position) != null && result.get(position).getCode() != null
                                                && result.get(position).getCode().startsWith("http")) {
                                            WebViewActivity.loadUrl(getContext(), result.get(position).getCode(), "加载中");
                                        }
                                    }
                                });

                            }
                        }
                    }
                });
    }

    private void save() {
    }

    /**
     * 初始化Recycleview
     */
    private void initRecyclerView() {
        xrvEveryday = bindingView.xrvEveryday;
        mHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_item_everyday, null, false);
        xrvEveryday.setPullRefreshEnabled(false);//下拉不可刷新
        xrvEveryday.setLoadingMoreEnabled(false);//上拉不可加载更多
        if (mHeaderView == null) {
            mHeaderView = mHeaderBinding.getRoot();
            xrvEveryday.addHeaderView(mHeaderView);
            Log.d(TAG, "initRecyclerView: ");
        }
        xrvEveryday.setLayoutManager(new LinearLayoutManager(getContext()));
        //需加，不然滑动不流畅
        xrvEveryday.setNestedScrollingEnabled(false);
        xrvEveryday.setHasFixedSize(false);
        xrvEveryday.setItemAnimator(new DefaultItemAnimator());
    }

    private void showLoading (boolean isLoading) {
        if (isLoading) {
            bindingView.llLoading.setVisibility(View.VISIBLE);
            bindingView.xrvEveryday.setVisibility(View.GONE);
            animation.startNow();
        } else {
            bindingView.llLoading.setVisibility(View.GONE);
            bindingView.xrvEveryday.setVisibility(View.VISIBLE);
            animation.cancel();
            Log.d("TAG", "showLoading: ");
        }
    }
}
