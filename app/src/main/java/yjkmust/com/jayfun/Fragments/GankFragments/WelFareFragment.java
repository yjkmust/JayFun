package yjkmust.com.jayfun.Fragments.GankFragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import com.example.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import yjkmust.com.jayfun.Adapter.WelfareAdapter;
import yjkmust.com.jayfun.App.Constants;
import yjkmust.com.jayfun.BaseAdapter.OnItemClickListener;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Fragments.BaseFragment;
import yjkmust.com.jayfun.Interfa.GankIoCustomService;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.ACache;
import yjkmust.com.jayfun.ViewBigImageActivity;
import yjkmust.com.jayfun.databinding.FragmentWelfareBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class WelFareFragment extends BaseFragment<FragmentWelfareBinding> {

    private WelfareAdapter adapter;
    private int mPage = 1;
    private boolean isFirst = true;
    private boolean isPrepared = false;
    private GankIoDataBean girlBean;
    private ACache mACache;
    private static String Tag = "Test";

    @Override
    public int setContent() {
        return R.layout.fragment_welfare;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindingView.xrvWelfare.setPullRefreshEnabled(false);//禁止下拉刷新
        bindingView.xrvWelfare.clearHeader();
        adapter = new WelfareAdapter();
        bindingView.xrvWelfare.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadWelfareData();

            }
        });
        isPrepared = true;
    }

    ArrayList<String> imgList = new ArrayList<>();

    @Override
    protected void loadData() {
       if (!mIsVisible||!isPrepared||!isFirst){
           return;
       }
       if (girlBean!=null&&girlBean.getResults()!=null&&girlBean.getResults().size()>0){
           showContentView();
           imgList.clear();
           for (GankIoDataBean.ResultBean bean :girlBean.getResults()){
               imgList.add(bean.getUrl());
           }
           girlBean = (GankIoDataBean) mACache.getAsObject(Constants.GANK_MEIZI);
           setAdapter(girlBean);
       }else {
           loadWelfareData();
       }

    }

    private void setAdapter(GankIoDataBean girlBean) {
        adapter.addAll(girlBean.getResults());
        //构造器中，第一个参数表示列数或者行数，第二个参数表示滑动方向
        bindingView.xrvWelfare.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        bindingView.xrvWelfare.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //显示成功后就不是第一次了，不再刷新
        isFirst = false;
        adapter.setOnItemClickListener(new OnItemClickListener<GankIoDataBean.ResultBean>() {
            @Override
            public void onClick(GankIoDataBean.ResultBean resultBean, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("select",2);//2.大图显示当前页数，1.头像不显示页数
                bundle.putInt("code", position);//第几张图片
                bundle.putStringArrayList("imgUrl",  imgList);//图片网数组
                Intent intent = new Intent(getContext(),ViewBigImageActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void loadWelfareData() {
        String baseURL = " https://gank.io/api/";
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(baseURL)
                .build();
        GankIoCustomService service = retrofit.create(GankIoCustomService.class);
        service.getGankIoData("福利", mPage, 20)
                .subscribeOn(Schedulers.newThread())//请求在新的线程中执行
                .observeOn(Schedulers.io())//请求完成后再io线程中执行
                .doOnNext(new Action1<GankIoDataBean>() {
                    @Override
                    public void call(GankIoDataBean gankIoDataBean) {
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
                        bindingView.xrvWelfare.refreshComplete();
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
                                imgList.clear();
                                for (GankIoDataBean.ResultBean bean :gankIoDataBean.getResults()){
                                    imgList.add(bean.getUrl());
                                }
                                setAdapter(gankIoDataBean);
                                mACache.remove(Constants.GANK_MEIZI);
                                mACache.put(Constants.GANK_MEIZI, gankIoDataBean, 3000);//缓存50分钟
                                Log.d(Tag, "onNext: " + 2);
                            }
                        } else {
                            if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                                adapter.addAll(gankIoDataBean.getResults());
                                bindingView.xrvWelfare.refreshComplete();
                                Log.d(Tag, "onNext: " + 3);
                                adapter.notifyDataSetChanged();
                                for (GankIoDataBean.ResultBean bean :gankIoDataBean.getResults()){
                                    imgList.add(bean.getUrl());
                                }

                            } else {
                                bindingView.xrvWelfare.noMoreLoading();
                                Log.d(Tag, "onNext: " + 4);
                            }
                        }


                    }
                });
    }

    @Override
    protected void onRefresh() {
        loadWelfareData();
    }
}
