package yjkmust.com.jayfun.Interfa;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;
import yjkmust.com.jayfun.Bean.BookBean;

/**
 * Created by 11432 on 2017/7/23.
 */

public interface DouBanService {
    @GET("v2/book/search")
    Observable<BookBean> getBook(@Query("tag") String tag, @Query("start") int start, @Query("count") int count) ;
}
