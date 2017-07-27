package yjkmust.com.jayfun.Interfa;

import android.database.Observable;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import yjkmust.com.jayfun.Bean.GankIoDataBean;

/**
 * Created by GEOFLY on 2017/7/5.
 */

public interface GankIoAndroidService {
    @GET("data/{type}/{pre_page}/{page}")
    Call<GankIoDataBean> getGankIoData(@Path("type") String id, @Path("page") int page, @Path("pre_page") int pre_page);
}
