package yjkmust.com.jayfun.Fragments.GankFragments;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerClickListener;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yjkmust.com.jayfun.Adapter.AndroidAdapter;
import yjkmust.com.jayfun.App.Constants;
import yjkmust.com.jayfun.Bean.FrontpageBean;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Fragments.BaseFragment;
import yjkmust.com.jayfun.Http.HttpClient;
import yjkmust.com.jayfun.Http.RequestImpl;
import yjkmust.com.jayfun.Interfa.EverdayBannerService;
import yjkmust.com.jayfun.Interfa.GankIoAndroidService;
import yjkmust.com.jayfun.Interfa.GankIoCustomService;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.ACache;
import yjkmust.com.jayfun.Util.GlideImageLoader;
import yjkmust.com.jayfun.Util.MySPUtils;
import yjkmust.com.jayfun.Util.SPUtils;
import yjkmust.com.jayfun.Util.ToastUtil;
import yjkmust.com.jayfun.ViewModel.GankOtherModel;
import yjkmust.com.jayfun.WebViewActivity;
import yjkmust.com.jayfun.databinding.FragmentCustomBinding;
import yjkmust.com.jayfun.databinding.HeaderItemEverydayBinding;
import yjkmust.com.jayfun.databinding.HeaderItemGankCustomBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class CustombysSelfFragment extends BaseFragment<FragmentCustomBinding> {
    private boolean mIsPrepared;//准备
    private boolean mIsFirst = true;//Fragment第一次创建
    private GankIoDataBean mAllBean;
    private ACache mAcache;
    private AndroidAdapter adapter;
    //    private Hea mHeaderBinding;
    private View mHeaderView;//recycleView的Header
    private int mPage = 1;
    private GankOtherModel mModel;
    private String mType = "all";
    private int per_page = 20;
    private static String Tag = "Test";
    private ArrayList<String> mBannerImages;
    private HeaderItemGankCustomBinding mHeaderBinding;
    private String TAG = "CustombysSelfFragment";

    @Override
    public int setContent() {
        return R.layout.fragment_custom;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mAcache = ACache.get(getContext());
        mIsPrepared = true;//准备就绪
        mModel = new GankOtherModel();
        mHeaderBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.header_item_gank_custom, null, false);
        initBanner();
        bindingView.xrvCustom.setPullRefreshEnabled(false);//禁止下拉刷新
        bindingView.xrvCustom.setLoadingMoreEnabled(true);
        adapter = new AndroidAdapter();
        bindingView.xrvCustom.setLoadingListener(new com.example.xrecyclerview.XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                Log.d(Tag, "onLoadMore: 1");
                loadCustomData();
            }
        });
        loadData();
        initBanner();
    }

    @Override
    protected void loadData() {
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.startAutoPlay();
            mHeaderBinding.banner.setDelayTime(3000);
        }
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }

        if (mAllBean != null && mAllBean.getResults() != null && mAllBean.getResults().size() > 0) {
            showContentView();//完成加载状态
            mAllBean = (GankIoDataBean) mAcache.getAsObject(Constants.GANK_CUSTOM);//读取缓存
            initAdapter(mAllBean);
        } else {
            loadCustomData();
//            loadBannerData();
        }

    }

    @Override
    protected void onInVisible() {
        super.onInVisible();
        // 不可见时轮播图停止滚动
        if (mHeaderBinding != null && mHeaderBinding.banner != null) {
            mHeaderBinding.banner.stopAutoPlay();
        }
    }

    private void initBanner(){
        mBannerImages = new ArrayList<>();
        mBannerImages.add("http://img.article.pchome.net/01/75/83/04/lollipop.jpg");
        mBannerImages.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501214418907&di=7ccbf9710ef8a4f423ea79ce317c261f&imgtype=0&src=http%3A%2F%2Fupload.techweb.com.cn%2F2017%2F0622%2F1498111301668.jpg");
        mBannerImages.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501214511032&di=1aa577aa72ed432c9aecf6c5e22f5972&imgtype=0&src=http%3A%2F%2Fwww.onlinecq.com%2Fuploads%2Fallimg%2F170704%2F1546153321_0.jpeg");
        mBannerImages.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501214830277&di=8b3caa6ca1a678c9062618fce8d29e97&imgtype=0&src=http%3A%2F%2Fwww.baizhi360.com%2Fuploads%2Farticle%2F20151106%2F2774d77f36db643dc3e7f6fce74c5023.jpg");
        mBannerImages.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501214915370&di=25ca02cf0dce661fe9ffc20e3e0c49b7&imgtype=0&src=http%3A%2F%2Fimg.leikeji.com%2Fresource%2Fimg%2F2efc3f3372574212838cbc818f4e7251.jpg");
    }

    private void loadBannerData() {
        HttpClient.Builder.getTingServer().getFrontpage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FrontpageBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(FrontpageBean frontpageBean) {
                        if (mBannerImages == null) {
                            mBannerImages = new ArrayList<String>();
                        } else {
                            mBannerImages.clear();
                        }
                        FrontpageBean bean = frontpageBean;
                        if (bean != null && bean.getResult() != null && bean.getResult().getFocus() != null && bean.getResult().getFocus().getResult() != null) {
                            final List<FrontpageBean.ResultBeanXXXXXXXXXXXXXX.FocusBean.ResultBeanX> result = bean.getResult().getFocus().getResult();
                            if (result != null && result.size() > 0) {
                                for (int i = 0; i < result.size(); i++) {
                                    //获取所有图片
                                    mBannerImages.add(result.get(i).getRandpic());
                                }
                                mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
                                mHeaderBinding.banner.setOnBannerClickListener(new OnBannerClickListener() {
                                    @Override
                                    public void OnBannerClick(int position) {
                                        position = position - 1;
                                        // 链接没有做缓存，如果轮播图使用的缓存则点击图片无效
                                        if (result.get(position) != null && result.get(position).getCode() != null
                                                && result.get(position).getCode().startsWith("http")) {
                                            WebViewActivity.loadUrl(getContext(), result.get(position).getCode(), "加载中...");
                                        }
                                    }
                                });
//                                maCache.remove(Constants.BANNER_PIC);
//                                maCache.put(Constants.BANNER_PIC, mBannerImages, 30000);
                            }
                        }

                    }
                });
    }


//    private void loadBannerData() {
//        OkHttpClient builder = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
//                .readTimeout(2, TimeUnit.SECONDS)
//                .writeTimeout(2, TimeUnit.SECONDS)
//                .build();
//        String BaseUrl = " https://tingapi.ting.baidu.com/v1/restserver/";
//        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .client(builder)
//                .build();
//        EverdayBannerService service = retrofit.create(EverdayBannerService.class);
//        Call<FrontpageBean> call = (Call<FrontpageBean>) service.getFrontpage();
//        call.enqueue(new Callback<FrontpageBean>() {
//            @Override
//            public void onResponse(Call<FrontpageBean> call, Response<FrontpageBean> response) {
//                FrontpageBean frontpageBean = response.body();
//                if (mBannerImages == null) {
//                    mBannerImages = new ArrayList<>();
//                } else {
//                    mBannerImages.clear();
//                }
//                if (frontpageBean != null && frontpageBean.getResult() != null && frontpageBean.getResult().getFocus() != null && frontpageBean.getResult().getFocus().getResult() != null) {
//                    final List<FrontpageBean.ResultBeanXXXXXXXXXXXXXX.FocusBean.ResultBeanX> result = frontpageBean.getResult().getFocus().getResult();
//                    if (result != null && result.size() > 0) {
//                        for (int i = 0; i < result.size(); i++) {
//                            mBannerImages.add(result.get(i).getRandpic());
//                        }
//                        mHeaderBinding.banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
//                        mHeaderBinding.banner.setOnBannerClickListener(new OnBannerClickListener() {
//                            @Override
//                            public void OnBannerClick(int position) {
//                                position = position - 1;
//                                //链接没有做缓存，如果轮播图使用缓存则点击图片无效
//                                if (result.get(position) != null && result.get(position).getCode() != null
//                                        && result.get(position).getCode().startsWith("http")) {
//                                    WebViewActivity.loadUrl(getContext(), result.get(position).getCode(), "加载中");
//                                }
//                            }
//                        });
//
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<FrontpageBean> call, Throwable t) {
//
//            }
//        });
//    }

    private void loadCustomData() {
        String baseURL = " https://gank.io/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseURL)
                .build();
        GankIoCustomService service = retrofit.create(GankIoCustomService.class);
        service.getGankIoData(mType, mPage, per_page)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())//请求完成后再io线程中执行
                .doOnNext(new Action1<GankIoDataBean>() {
                    @Override
                    public void call(GankIoDataBean gankIoDataBean) {
                        save();
                    }
                }).observeOn(AndroidSchedulers.mainThread())//最后在主线程中执行
                .subscribe(new Subscriber<GankIoDataBean>() {
                    @Override
                    public void onCompleted() {
                        Log.d(Tag, "onCompleted: ");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(Tag, "onError: " + 1);
                        showContentView();
                        bindingView.xrvCustom.refreshComplete();
                        if (adapter.getItemCount() == 0) {
                            showError();
                        }
                        if (mPage > 1) {
                            mPage--;
                        }
                    }

                    @Override
                    public void onNext(GankIoDataBean gankIoDataBean) {
                        Log.d(Tag, "onNext: " + 1);
                        showContentView();//加载完成切换页面
                        if (mPage == 1) {
                            if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                                initAdapter(gankIoDataBean);
                                mAcache.remove(Constants.GANK_CUSTOM);
                                mAcache.put(Constants.GANK_CUSTOM, gankIoDataBean, 3000);//缓存50分钟
                                Log.d(Tag, "onNext: " + 2);
                            }
                        } else {
                            if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                                adapter.addAll(gankIoDataBean.getResults());
                                Log.d(Tag, "onNext: " + 3);
                                adapter.notifyDataSetChanged();
                            } else {
                                bindingView.xrvCustom.noMoreLoading();
                                Log.d(Tag, "onNext: " + 4);
                            }
                        }
                    }
                });

    }

    private void save() {
    }

    private void initAdapter(GankIoDataBean mCustomBean) {
        if (mHeaderView == null) {
            mHeaderView = View.inflate(getContext(), R.layout.header_item_gank_custom, null);
            Banner banner = (Banner) mHeaderView.findViewById(R.id.banner);
            banner.setDelayTime(5000);
            banner.setImages(mBannerImages).setImageLoader(new GlideImageLoader()).start();
            bindingView.xrvCustom.addHeaderView(mHeaderView);
        }
        initHeader(mHeaderView);
        boolean isAll = MySPUtils.get(getContext(), "gank_cala", "全部").equals("全部");
        adapter.clear();
        adapter.setAllType(isAll);
        adapter.addAll(mCustomBean.getResults());
        bindingView.xrvCustom.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvCustom.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mIsFirst = false;

    }

    private void initHeader(View mHeaderView) {
        //选择分类
        final TextView tvType = (TextView) mHeaderView.findViewById(R.id.tx_name);
        String gankCala = (String) MySPUtils.get(getContext(), "gank_cala", "全部");
        tvType.setText(gankCala);
        final View mTypeView = mHeaderView.findViewById(R.id.ll_choose_catalogue);
        mTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(getContext(), R.style.BottomSheet_Dialog).title("选择分类").sheet(R.menu.gank_bottomsheet).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case R.id.gank_all:
                                if (isOtherType("全部")) {
                                    tvType.setText("全部");
                                    mType = "all";
                                    mPage = 1;
                                    adapter.clear();
//                                    SPUtils.putString("gank_cala", "全部");
                                    MySPUtils.put(getContext(), "gank_cala", "全部");
                                    showLoading();//显示加载中的状态
                                    loadCustomData();
                                }
                                break;
                            case R.id.gank_ios:
                                if (isOtherType("IOS")) {
                                    tvType.setText("IOS");
                                    mType = "ios";
                                    mPage = 1;
                                    adapter.clear();
//                                    SPUtils.putString("gank_cala", "IOS");
                                    MySPUtils.put(getContext(), "gank_cala", "IOS");
                                    showLoading();
                                    loadCustomData();
                                }
                                break;
                            case R.id.gank_qian:
                                if (isOtherType("前端")) {
                                    changeContent(tvType, "前端");
                                }
                                break;
                            case R.id.gank_app:
                                if (isOtherType("App")) {
                                    changeContent(tvType, "App");
                                }
                                break;
                            case R.id.gank_movie:
                                if (isOtherType("休息视频")) {
                                    changeContent(tvType, "休息视频");
                                }
                                break;
                            case R.id.gank_resouce:
                                if (isOtherType("拓展资源")) {
                                    changeContent(tvType, "拓展资源");
                                }
                                break;
                        }

                    }
                }).show();
            }
        });

    }

    private void changeContent(TextView tvType, String content) {
        tvType.setText(content);
        mType = content;
        mPage = 1;
        adapter.clear();
        MySPUtils.put(getContext(), "gank_cala", content);
        showLoading();
        loadCustomData();
    }

    /**
     * 判断选择类型
     *
     * @param string
     * @return
     */
    private boolean isOtherType(String string) {
        MySPUtils.get(getContext(), "gank_cala", "全部");
//        String mType = SPUtils.getString("gank_cala", "全部");
        if (mType.equals(string)) {
            ToastUtil.showToast("选择类型相同！");
            return false;
        } else {
//             重置XRecyclerView状态，解决 如出现刷新到底无内容再切换其他类别后，无法上拉加载的情况
            bindingView.xrvCustom.reset();
            return true;
        }

    }

    /**
     * 加载失败后点击重新加载的回调方法
     */
    @Override
    protected void onRefresh() {
        loadCustomData();
    }
}
