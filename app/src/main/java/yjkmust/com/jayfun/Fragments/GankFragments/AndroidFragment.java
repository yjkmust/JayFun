package yjkmust.com.jayfun.Fragments.GankFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.Toast;



import java.lang.annotation.Retention;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yjkmust.com.jayfun.Adapter.AndroidAdapter;
import yjkmust.com.jayfun.App.Constants;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Fragments.BaseFragment;
import yjkmust.com.jayfun.Interfa.GankIoAndroidService;
import yjkmust.com.jayfun.Interfa.RequestInterfa;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.ACache;
import yjkmust.com.jayfun.databinding.FragmentAndroidBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class AndroidFragment extends BaseFragment<FragmentAndroidBinding> {
    private GankIoDataBean gankIoDataBean;
    private int mPage = 1;
    private AndroidAdapter adapter;
    private ACache mACache;
    private GankIoDataBean mAndroidBean;

    @Override
    public int setContent() {
        return R.layout.fragment_android;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        mACache = ACache.get(getContext());
        initRetrofit(mPage, new RequestInterfa() {
            @Override
            public void loadSuccess(Object object) {
//                Toast.makeText(getActivity(), "加载成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void loadFailed() {
//                Toast.makeText(getActivity(), "加载失败", Toast.LENGTH_SHORT).show();

            }
        });
        adapter = new AndroidAdapter();
        bindingView.xrvAndroid.setAdapter(adapter);
        bindingView.xrvAndroid.setLayoutManager(new LinearLayoutManager(getActivity()));
        bindingView.xrvAndroid.setLoadingListener(new com.example.xrecyclerview.XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                mPage=1;
                loadAndroidData();
            }
            @Override
            public void onLoadMore() {
                mPage++;
                loadAndroidData();
            }
        });

    }
    private void loadAndroidData() {
        showContentView();
        initRetrofit(mPage, new RequestInterfa() {
            @Override
            public void loadSuccess(Object object) {
                if (mPage == 1) {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                        mACache.remove(Constants.GANK_ANDROID);
                        // 缓存50分钟
                        mACache.put(Constants.GANK_ANDROID, gankIoDataBean, 30000);
                        bindingView.xrvAndroid.refreshComplete();
                    }
                } else {
                    if (gankIoDataBean != null && gankIoDataBean.getResults() != null && gankIoDataBean.getResults().size() > 0) {
                        bindingView.xrvAndroid.refreshComplete();
                        adapter.addAll(gankIoDataBean.getResults());
                        adapter.notifyDataSetChanged();
                    } else {
                        bindingView.xrvAndroid.noMoreLoading();
                        bindingView.xrvAndroid.refreshComplete();
                    }
                }
            }

            @Override
            public void loadFailed() {
                bindingView.xrvAndroid.refreshComplete();
                // 注意：这里不能写成 mPage == 1，否则会一直显示错误页面
                if (adapter.getItemCount() == 0) {
                    showError();
                }
                if (mPage > 1) {
                    mPage--;
                }
            }
        });
    }

    private void initRetrofit(int page, final RequestInterfa listener) {
        OkHttpClient builder = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
                .readTimeout(2, TimeUnit.SECONDS)
                .writeTimeout(2, TimeUnit.SECONDS)
                .build();
        String BaseUrl = " https://gank.io/api/";
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(builder)
                .build();
        GankIoAndroidService service = retrofit.create(GankIoAndroidService.class);
        Call<GankIoDataBean> call = service.getGankIoData("Android", page, 20);
        call.enqueue(new Callback<GankIoDataBean>() {
            @Override
            public void onResponse(Call<GankIoDataBean> call, Response<GankIoDataBean> response) {
                gankIoDataBean = response.body();
                adapter.addAll(gankIoDataBean.getResults());
                adapter.notifyDataSetChanged();
                listener.loadSuccess(gankIoDataBean.getResults());
                if (gankIoDataBean != null) {
                    mACache.remove(Constants.GANK_ANDROID);
                    mACache.put(Constants.GANK_ANDROID, gankIoDataBean);
                }
            }

            @Override
            public void onFailure(Call<GankIoDataBean> call, Throwable t) {
                listener.loadFailed();

            }
        });
    }

    @Override
    protected void loadData() {
        if (mAndroidBean != null
                && mAndroidBean.getResults() != null
                && mAndroidBean.getResults().size() > 0) {
            showContentView();
            mAndroidBean = (GankIoDataBean) mACache.getAsObject(Constants.GANK_ANDROID);
            adapter.addAll(mAndroidBean.getResults());
            adapter.notifyDataSetChanged();
        } else {
            loadAndroidData();
        }
    }

    @Override
    protected void onRefresh() {
        loadAndroidData();
    }
}
