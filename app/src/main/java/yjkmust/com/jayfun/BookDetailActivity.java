package yjkmust.com.jayfun;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.widget.ImageView;

import yjkmust.com.jayfun.Bean.BooksBean;
import yjkmust.com.jayfun.Util.CommonUtils;

/**
 * Created by 11432 on 2017/7/23.
 */

public class BookDetailActivity {
    public final static String EXTRA_PARAM = "bookBean";
    public static void start(Activity context, BooksBean positionData, ImageView imageView) {
        Intent intent = new Intent(context, BookDetailActivity.class);
        intent.putExtra(EXTRA_PARAM, positionData);
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                        imageView, CommonUtils.getString(R.string.transition_book_img));//与xml文件对应
        ActivityCompat.startActivity(context, intent, options.toBundle());
    }
}
