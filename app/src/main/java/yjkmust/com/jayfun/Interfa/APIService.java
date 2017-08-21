package yjkmust.com.jayfun.Interfa;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import yjkmust.com.jayfun.Bean.ZhiHuDateBean;
import yjkmust.com.jayfun.Bean.ZhihuContentBean;

/**
 * Created by 11432 on 2017/8/16.
 */

public interface APIService {
    @GET("before/{date}")
    Call<ZhiHuDateBean> getNews(@Path("date") String date);
    @GET("{date}")
    Call<ZhihuContentBean> getContent(@Path("date") String date);
}
