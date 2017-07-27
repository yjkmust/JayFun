package yjkmust.com.jayfun.Fragments.DouFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yjkmust.com.jayfun.Adapter.BookAdapter;
import yjkmust.com.jayfun.Bean.BookBean;
import yjkmust.com.jayfun.Bean.BooksBean;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Fragments.BaseFragment;
import yjkmust.com.jayfun.Http.HttpClient;
import yjkmust.com.jayfun.Interfa.DouBanService;
import yjkmust.com.jayfun.Interfa.GankIoAndroidService;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.CommonUtils;
import yjkmust.com.jayfun.Util.DebugUtil;
import yjkmust.com.jayfun.databinding.FragmentBookCustomBinding;

/**
 * Created by 11432 on 2017/7/17.
 */

public class BookCustomFragment extends BaseFragment<FragmentBookCustomBinding> {
    private boolean mIsPrepared;
    private boolean mIsFirst = true;
    private static final String TYPE = "param1";
    private String mType = "综合";
    private int mStart = 0;
    private int mCount = 18;//一次请求的数量
    private GridLayoutManager gridLayoutManager;
    private BookAdapter mBookAdapter;

    @Override
    public int setContent() {
        return R.layout.fragment_book_custom;
    }
//    public static BookCustomFragment newInstance(String param1){
//        BookCustomFragment fragment = new BookCustomFragment();
//        Bundle args = new Bundle();
//        args.putString(TYPE, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (savedInstanceState!=null){
//            mType = getArguments().getString(TYPE);
//        }
//    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        bindingView.srlBook.setColorSchemeColors(CommonUtils.getColor(R.color.colorTheme));
        bindingView.srlBook.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                bindingView.srlBook.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                         mStart = 0;
                         loadCustomData();

                    }
                },1000);

            }
        });
        gridLayoutManager = new GridLayoutManager(getContext(), 3);
        bindingView.xrvBook.setLayoutManager(gridLayoutManager);
        scrollRecycleView();
        mIsPrepared = true;//准备就绪
        /**
         * 因为启动时先走loadData()再走onActivityCreated，
         * 所以此处要额外调用load(),不然最初不会加载内容
         */
        loadData();
    }

    @Override
    protected void loadData() {
        super.loadData();
        if (!mIsPrepared||!mIsFirst||!mIsVisible){
            return;
        }
        bindingView.srlBook.setRefreshing(true);
        bindingView.srlBook.postDelayed(new Runnable() {
            @Override
            public void run() {
//                LoadDatas();
            }
        },500);
    }

    @Override
    protected void onRefresh() {
        bindingView.srlBook.setRefreshing(true);
        loadCustomData();
    }
//private void LoadDatas(){
//    OkHttpClient builder = new OkHttpClient.Builder().connectTimeout(2, TimeUnit.SECONDS)
//            .readTimeout(2, TimeUnit.SECONDS)
//            .writeTimeout(2, TimeUnit.SECONDS)
//            .build();
//    String BaseUrl = " https://api.douban.com/";
//    Retrofit retrofit = new Retrofit.Builder().baseUrl(BaseUrl)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(builder)
//            .build();
//    DouBanService service = retrofit.create(DouBanService.class);
////    Call<BookBean> calls = service.getBook(mType,mStart,mCount);
//    Call<BookBean> call = (Call<BookBean>) service.getBook(mType, mStart, mCount);
//    call.enqueue(new Callback<BookBean>() {
//
//        @Override
//        public void onResponse(Call<BookBean> call, Response<BookBean> response) {
//            DebugUtil.debug("onSucceess");
//            BookBean bookBean = response.body();
//            if (mStart==0){
//                if (bookBean!=null&&bookBean.getBooks()!=null&&bookBean.getBooks().size()>0){
//                    if (mBookAdapter==null){
//                        mBookAdapter = new BookAdapter(getActivity());
//                    }
//                    mBookAdapter.setList(bookBean.getBooks());
//                    mBookAdapter.notifyDataSetChanged();
//                    bindingView.xrvBook.setAdapter(mBookAdapter);
//                }
//                mIsFirst = false;
//            }else {
//                mBookAdapter.addAll(bookBean.getBooks());
//                mBookAdapter.notifyDataSetChanged();
//            }
//            if (mBookAdapter!=null){
//                mBookAdapter.updateLoadStatus(BookAdapter.LOAD_PULL_TO);
//            }
//
//        }
//
//        @Override
//        public void onFailure(Call<BookBean> call, Throwable t) {
//            DebugUtil.debug("onError");
//        }
//    });
//}

    private void loadCustomData() {
       HttpClient.Builder.getDouBanService().getBook(mType,mStart,mCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookBean>() {
                    @Override
                    public void onCompleted() {

                        DebugUtil.debug("onComplete");
                        showContentView();
                        if (bindingView.srlBook.isRefreshing()){
                            bindingView.srlBook.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugUtil.debug("onError");
                        showContentView();
                        if (bindingView.srlBook.isRefreshing()){
                            bindingView.srlBook.setRefreshing(false);
                        }
                        if (mCount==0){
                            showError();
                        }

                    }

                    @Override
                    public void onNext(BookBean bookBean) {
                        DebugUtil.debug("onNext");
                        if (mStart==0){
                            if (bookBean!=null&&bookBean.getBooks()!=null&&bookBean.getBooks().size()>0){
                                if (mBookAdapter==null){
                                    mBookAdapter = new BookAdapter(getActivity());
                                }
                                mBookAdapter.setList(bookBean.getBooks());
                                mBookAdapter.notifyDataSetChanged();
                                bindingView.xrvBook.setAdapter(mBookAdapter);
                            }
                            mIsFirst = false;
                        }else {
                            mBookAdapter.addAll(bookBean.getBooks());
                            mBookAdapter.notifyDataSetChanged();
                        }
                        if (mBookAdapter!=null){
                            mBookAdapter.updateLoadStatus(BookAdapter.LOAD_PULL_TO);
                        }

                    }
                });
//       addSubscription(get);
    }
    public void scrollRecycleView() {
        bindingView.xrvBook.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int lastVisibleItem;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                    /**StaggeredGridLayoutManager*/
//                    int[] into = new int[(mLayoutManager).getSpanCount()];
//                    lastVisibleItem = findMax(mLayoutManager.findLastVisibleItemPositions(into));

                    if (mBookAdapter == null) {
                        return;
                    }
                    if (gridLayoutManager.getItemCount() == 0) {
                        mBookAdapter.updateLoadStatus(BookAdapter.LOAD_NONE);
                        return;

                    }
                    if (lastVisibleItem + 1 == gridLayoutManager.getItemCount()
                            && mBookAdapter.getLoadStatus() != BookAdapter.LOAD_MORE) {
//                        mBookAdapter.updateLoadStatus(BookAdapter.LOAD_PULL_TO);
                        // isLoadMore = true;
                        mBookAdapter.updateLoadStatus(BookAdapter.LOAD_MORE);

                        //new Handler().postDelayed(() -> getBeforeNews(time), 1000);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
//                                String tag= BookApiUtils.getRandomTAG(listTag);
//                                doubanBookPresenter.searchBookByTag(BookReadingFragment.this,tag,true);
                                mStart += mCount;
                                loadCustomData();
                            }
                        }, 1000);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                /**StaggeredGridLayoutManager*/
//                int[] into = new int[(mLayoutManager).getSpanCount()];
//                lastVisibleItem = findMax(mLayoutManager.findLastVisibleItemPositions(into));
            }
        });
    }

}
