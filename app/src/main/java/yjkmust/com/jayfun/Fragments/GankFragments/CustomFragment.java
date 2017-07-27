package yjkmust.com.jayfun.Fragments.GankFragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import rx.Subscription;
import yjkmust.com.jayfun.Adapter.AndroidAdapter;
import yjkmust.com.jayfun.App.Constants;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Fragments.BaseFragment;
import yjkmust.com.jayfun.Http.RequestImpl;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.ACache;
import yjkmust.com.jayfun.Util.SPUtils;
import yjkmust.com.jayfun.Util.ToastUtil;
import yjkmust.com.jayfun.ViewModel.GankOtherModel;
import yjkmust.com.jayfun.databinding.FragmentCustomBinding;



/**
 * Created by GEOFLY on 2017/6/28.
 */

public class CustomFragment extends BaseFragment<FragmentCustomBinding> {
    private boolean mIsPrepared;//准备
    private boolean mIsFirst = true;//Fragment第一次创建
    private GankIoDataBean mAllBean;
    private ACache mAcache;
    private AndroidAdapter adapter;
    private View mHeaderView;//recycleView的Header
    private int mPage = 1;
    private GankOtherModel mModel;
    private String mType = "all";
    private int per_page = 20;
    private static String Tag= "Test";

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
        bindingView.xrvCustom.setPullRefreshEnabled(false);//禁止下拉刷新
        adapter = new AndroidAdapter();
        bindingView.xrvCustom.setLoadingListener(new com.example.xrecyclerview.XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
                loadCustomData();

            }
        });
    }

    @Override
    protected void loadData() {
        if (!mIsPrepared || !mIsVisible || !mIsFirst) {
            return;
        }
        if (mAllBean != null && mAllBean.getResults() != null && mAllBean.getResults().size() > 0) {
            showContentView();//完成加载状态
            mAllBean = (GankIoDataBean) mAcache.getAsObject(Constants.GANK_CUSTOM);//读取缓存
            initAdapter(mAllBean);
        }else {
            loadCustomData();
        }

    }
    private void loadCustomData() {
        mModel.setData(mType, mPage, per_page);
        mModel.getGankIoData(new RequestImpl() {
            @Override
            public void loadSuccess(Object object) {
//                ToastUtil.showToast("成功");
                Log.d(Tag, "loadsuccess: ");
                showContentView();//加载完成切换页面
                GankIoDataBean gankIoDataBean = (GankIoDataBean) object;
                if (mPage==1){
                    if (gankIoDataBean!=null&&gankIoDataBean.getResults()!=null&&gankIoDataBean.getResults().size()>0){
                        initAdapter(gankIoDataBean);
                        mAcache.remove(Constants.GANK_CUSTOM);
                        mAcache.put(Constants.GANK_CUSTOM, gankIoDataBean,3000);//缓存50分钟
                    }else {
                        if (gankIoDataBean!=null&&gankIoDataBean.getResults()!=null&&gankIoDataBean.getResults().size()>0){
                            bindingView.xrvCustom.refreshComplete();
                            adapter.addAll(gankIoDataBean.getResults());
                            adapter.notifyDataSetChanged();
                        }else {
                            bindingView.xrvCustom.noMoreLoading();
                        }
                    }
                }
            }

            @Override
            public void loadFailed() {
//                ToastUtil.showToast("失败");
                Log.d(Tag, "loadFailed: ");
                showContentView();
                bindingView.xrvCustom.refreshComplete();
                if (adapter.getItemCount()==0){
                    showError();
                }
                if (mPage>1){
                    mPage--;
                }

            }

            @Override
            public void addSubscription(Subscription subscription) {
                CustomFragment.this.addSubscription(subscription);

            }
        });
    }

    private void initAdapter(GankIoDataBean mCustomBean) {
        if (mHeaderView == null) {
            mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.header_item_gank_custom, null);
            bindingView.xrvCustom.addHeaderView(mHeaderView);
            initHeader(mHeaderView);
            boolean isAll = SPUtils.getString("gank_cala", "全部").equals("全部");
            adapter.clear();
            adapter.setAllType(isAll);
            adapter.addAll(mCustomBean.getResults());
            bindingView.xrvCustom.setLayoutManager(new LinearLayoutManager(getActivity()));
            bindingView.xrvCustom.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            mIsFirst = false;
        }
    }

    private void initHeader(View mHeaderView) {
        //选择分类
        final TextView tvType = (TextView) mHeaderView.findViewById(R.id.tx_name);
        String gankCala = SPUtils.getString("gank_cala", "全部");
        tvType.setText(gankCala);
        final View mTypeView = mHeaderView.findViewById(R.id.ll_choose_catalogue);
        mTypeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BottomSheet.Builder(getContext(),R.style.BottomSheet_Dialog).title("选择分类").sheet(R.menu.gank_bottomsheet).listener(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case R.id.gank_all:
                                if (isOtherType("全部")){
                                    tvType.setText("全部");
                                    mType = "all";
                                    mPage = 1;
                                    adapter.clear();
                                    SPUtils.putString("gank_cala","全部");
                                    showLoading();//显示加载中的状态
                                    loadCustomData();
                                }
                                break;
                            case R.id.gank_ios:
                                if (isOtherType("IOS")){
                                    tvType.setText("IOS");
                                    mType = "ios";
                                    mPage = 1;
                                    adapter.clear();
                                    SPUtils.putString("gank_cala","IOS");
                                    showLoading();
                                    loadCustomData();
                                }
                                break;
                            case R.id.gank_qian:
                                if (isOtherType("前端")){
                                    changeContent(tvType,"前端");
                                }
                                break;
                            case R.id.gank_app:
                                if (isOtherType("App")){
                                    changeContent(tvType,"App");
                                }
                                break;
                            case R.id.gank_movie:
                                if (isOtherType("休息视频")){
                                    changeContent(tvType,"休息视频");
                                }
                                break;
                            case R.id.gank_resouce:
                                if (isOtherType("拓展资源")){
                                    changeContent(tvType,"拓展资源");
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
        SPUtils.putString("gank_cala", content);
        showLoading();
        loadCustomData();
    }

    /**
     * 判断选择类型
     * @param string
     * @return
     */
    private boolean isOtherType(String string) {
        String mType = SPUtils.getString("gank_cala", "全部");
        if (mType.equals(string)){
            ToastUtil.showToast("选择类型相同！");
            return false;
        }else {
            // 重置XRecyclerView状态，解决 如出现刷新到底无内容再切换其他类别后，无法上拉加载的情况
//            bindingView.xrvCustom.reset();
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
