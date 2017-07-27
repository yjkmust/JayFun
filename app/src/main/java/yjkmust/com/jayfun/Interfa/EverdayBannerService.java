package yjkmust.com.jayfun.Interfa;

import retrofit2.http.GET;
import rx.Observable;
import yjkmust.com.jayfun.Bean.FrontpageBean;

/**
 * Created by GEOFLY on 2017/7/14.
 */

public interface EverdayBannerService {
    /**
     * 首页轮播图
     */
    @GET("ting?from=android&version=5.8.1.0&channel=ppzs&operator=3&method=baidu.ting.plaza.index&cuid=89CF1E1A06826F9AB95A34DC0F6AAA14")
    Observable<FrontpageBean> getFrontpage();
}
