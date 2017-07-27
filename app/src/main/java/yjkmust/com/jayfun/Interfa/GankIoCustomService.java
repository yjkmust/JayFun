package yjkmust.com.jayfun.Interfa;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import yjkmust.com.jayfun.Bean.GankIoDataBean;

/**
 * Created by GEOFLY on 2017/7/5.
 */

public interface GankIoCustomService {
    @GET("data/{type}/{pre_page}/{page}")
    Observable<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);
}
