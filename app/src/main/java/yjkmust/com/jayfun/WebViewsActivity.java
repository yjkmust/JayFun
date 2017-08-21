package yjkmust.com.jayfun;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import yjkmust.com.jayfun.App.MyApplication;
import yjkmust.com.jayfun.Bean.GlobalBean;
import yjkmust.com.jayfun.Bean.ZhiHuDateBean;
import yjkmust.com.jayfun.Bean.ZhiHuStoryBean;
import yjkmust.com.jayfun.Bean.ZhihuContentBean;
import yjkmust.com.jayfun.Interfa.APIService;
import yjkmust.com.jayfun.Util.BaseTools;
import yjkmust.com.jayfun.Util.ShareUtils;
import yjkmust.com.jayfun.Util.ToastUtil;
import yjkmust.com.jayfun.WebViewConfig.FullscreenHolder;
import yjkmust.com.jayfun.WebViewConfig.IWebPageView;
import yjkmust.com.jayfun.WebViewConfig.ImageClickInterface;
import yjkmust.com.jayfun.WebViewConfig.MyWebChromeClient;
import yjkmust.com.jayfun.WebViewConfig.MyWebViewClient;
import yjkmust.com.jayfun.databinding.ActivityWebViewBinding;

public class WebViewsActivity extends SwipeBackActivity {

    private ActivityWebViewBinding mBinding;
    private String mTitle;
    private String mUrl;
    private Toolbar titleToolBar;
    private WebView mWebView;
    // 加载视频相关
    private ZhihuContentBean contentBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getIntenrData();
        initTitle();
        initWebView();
        initRetrofit();
    }
    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalBean.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        APIService service = retrofit.create(APIService.class);
        Call<ZhihuContentBean> call = service.getContent(mUrl);
        call.enqueue(new Callback<ZhihuContentBean>() {
            @Override
            public void onResponse(Call<ZhihuContentBean> call, Response<ZhihuContentBean> response) {
                contentBean = response.body();
                loadUrl(contentBean);
            }

            @Override
            public void onFailure(Call<ZhihuContentBean> call, Throwable t) {
                MyApplication.showToast("获取数据失败！");
            }
        });
    }
    private void loadUrl(ZhihuContentBean data) {
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/css/news.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + data.getBody() + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        mWebView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);
    }



// 设置setWebChromeClient对象

    private void initTitle() {
        mWebView = mBinding.webviewDetail;
        titleToolBar = mBinding.titleToolBar;
        setSupportActionBar(titleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        titleToolBar.setTitle(mTitle);
        titleToolBar.setOverflowIcon(ContextCompat.getDrawable(this, R.drawable.actionbar_more));
        titleToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.actionbar_share:// 分享到
                        String shareText = mWebView.getTitle() + (GlobalBean.baseUrl+mUrl)+ "（分享自JayFun）";
                        ShareUtils.share(WebViewsActivity.this, shareText);
                        break;
                    case R.id.actionbar_cope:// 复制链接
                        BaseTools.copy(mUrl);
                        ToastUtil.showToast("复制成功");
                        break;
                    case R.id.actionbar_open:// 打开链接
                        BaseTools.openLink(WebViewsActivity.this, (GlobalBean.baseUrl+mUrl));
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.webview_menu, menu);
        return true;
    }
    public void getIntenrData() {
        mTitle = getIntent().getStringExtra("Title");
        mUrl = getIntent().getStringExtra("UrlId");
    }

    private void initWebView() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 开启DOM storage API 功能
        mWebView.getSettings().setDomStorageEnabled(true);
        // 开启database storage API功能
        mWebView.getSettings().setDatabaseEnabled(true);
        // 开启Application Cache功能
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack(); // goBack()表示返回WebView的上一页面
            loadUrl(contentBean);
            return true;
        }
        return super.onKeyDown(keyCode,event);
    }
}
