package yjkmust.com.jayfun.ViewModel;

import android.util.Log;

import rx.Observer;
import rx.Scheduler;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import yjkmust.com.jayfun.Bean.GankIoDataBean;
import yjkmust.com.jayfun.Http.HttpClient;
import yjkmust.com.jayfun.Http.RequestImpl;

/**
 * 分类数据：http://gank.io/api/data/数据类型/请求个数/第几页 的Model
 * 好处之一是请求数据接口可以统一，不用每个地方都写接口，更换接口方便
 * 其实代码量也没减少多少，但维护起来方便
 * Created by GEOFLY on 2017/7/6.
 */

public class GankOtherModel {
    private static String Tag= "Test";
    private String id;
    private int page;
    private int per_page;
    public void setData(String id,int page,int per_page){
        this.id = id;
        this.page = page;
        this.per_page = per_page;
    }
    public void getGankIoData(final RequestImpl listener){
        Subscription subscription = HttpClient.Builder.getGankIOServer().getGankIoData(id,page,per_page)
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GankIoDataBean>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        listener.loadFailed();
                        Log.d(Tag, "onError: ");
                    }
                    @Override
                    public void onNext(GankIoDataBean gankIoDataBean) {
                        listener.loadSuccess(gankIoDataBean);
                    }
                });
        listener.addSubscription(subscription);
    }
}
