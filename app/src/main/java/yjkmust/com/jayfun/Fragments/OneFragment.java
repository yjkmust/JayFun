package yjkmust.com.jayfun.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yjkmust.com.jayfun.Adapter.ZhihuAdapter;
import yjkmust.com.jayfun.App.MyApplication;
import yjkmust.com.jayfun.Bean.GlobalBean;
import yjkmust.com.jayfun.Bean.ZhiHuDateBean;
import yjkmust.com.jayfun.Bean.ZhiHuStoryBean;
import yjkmust.com.jayfun.Interfa.APIService;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.WebViewActivity;
import yjkmust.com.jayfun.WebViewsActivity;
import yjkmust.com.jayfun.Widgets.CustomerLoadMoreView;
import yjkmust.com.jayfun.databinding.FragmentOneBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class OneFragment extends BaseFragment<FragmentOneBinding> implements SwipeRefreshLayout.OnRefreshListener,BaseQuickAdapter.RequestLoadMoreListener {
    private RecyclerView recyclerView;
    private List<ZhiHuStoryBean> list;
    private ZhihuAdapter adapter;
    private static int DATE_TYPE1 = 1;
    private static int DATE_TYPE2 = 2;
    private static int ACTION_TYPE1 = 1;//刷新
    private static int ACTION_TYPE2 = 2;//加载更多
    private int num = 0;
    private SwipeRefreshLayout swipeRefresh;
    @Override
    public int setContent() {
       return R.layout.fragment_one;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        initView();
        initAdapter();
        loadData(num,ACTION_TYPE1);
    }
    private void loadData(final int nums, final int actionType){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalBean.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ZhiHuDateBean> call = service.getNews(Date.getBeforeDate(DATE_TYPE1,nums));
        call.enqueue(new Callback<ZhiHuDateBean>() {
            @Override
            public void onResponse(Call<ZhiHuDateBean> call, Response<ZhiHuDateBean> response) {
                if (actionType==1){
                    list.clear();
                }
                final ZhiHuDateBean data = response.body();
                List<ZhiHuDateBean.StoriesBean> stories = data.getStories();
                for (ZhiHuDateBean.StoriesBean bean : stories){
                    ZhiHuStoryBean zhiHuStoryBean = new ZhiHuStoryBean();
                    zhiHuStoryBean.setDate(Date.getBeforeDate(DATE_TYPE2,nums));
                    zhiHuStoryBean.setBean(bean);
                    list.add(zhiHuStoryBean);
                }
                if (nums>0){
                    adapter.loadMoreComplete();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ZhiHuDateBean> call, Throwable t) {
                num--;
                MyApplication.showToast("获取数据失败！");
                adapter.loadMoreFail();
            }
        });
    }


    public static class Date{
        public static String getNowDate(int type){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(calendar.getTimeInMillis()));
            String date = dateFormat.format(calendar.getTime());
            String dates = dateFormats.format(calendar.getTime());
            if (type==1){
                return date;
            }else {
                return dates;
            }
        }
        public static String getBeforeDate(int type ,int diffDay){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat dateFormats = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date(calendar.getTimeInMillis()));
            calendar.add(Calendar.DAY_OF_YEAR, -diffDay);
            String date = dateFormat.format(calendar.getTime());
            String dates = dateFormats.format(calendar.getTime());
            if (type==1){
                return date;
            }else {
                return dates;
            }
        }
    }
    private void initView(){
        swipeRefresh = bindingView.swipeRefresh;
        recyclerView = bindingView.rvView;
        recyclerView.setLayoutManager(new LinearLayoutManager(MyApplication.getContext()));
        swipeRefresh.setOnRefreshListener(this);
        swipeRefresh.setColorSchemeColors(Color.rgb(47, 223, 189));
        list = new ArrayList<>();
    }
    private void initAdapter(){
        adapter = new ZhihuAdapter(list);
        adapter.openLoadAnimation(BaseQuickAdapter.SLIDEIN_LEFT);
        adapter.setLoadMoreView(new CustomerLoadMoreView());
        adapter.setOnLoadMoreListener(this,recyclerView);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Intent intent = new Intent(MyApplication.getContext(), WebViewsActivity.class);
                intent.putExtra("UrlId", list.get(position).getBean().getId()+"");
                intent.putExtra("Title", list.get(position).getBean().getTitle());
                startActivity(intent);

            }
        });
    }
    @Override
    public void onLoadMoreRequested() {
        num++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(num,ACTION_TYPE2);
            }
        },1000);
    }

    @Override
    public void onRefresh() {
        num = 0;
        adapter.setEnableLoadMore(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData(num,ACTION_TYPE1);
                adapter.notifyDataSetChanged();
                swipeRefresh.setRefreshing(false);
                adapter.setEnableLoadMore(true);
            }
        }, 1000);
    }
}
