package yjkmust.com.jayfun;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import yjkmust.com.jayfun.Util.BaseTools;
import yjkmust.com.jayfun.Util.ShareUtils;
import yjkmust.com.jayfun.Util.ToastUtil;
import yjkmust.com.jayfun.databinding.ActivityWebViewBinding;

public class WebViewActivity extends SwipeBackActivity {

    private ActivityWebViewBinding mBinding;
    private String mTitle;
    private String mUrl;
    private Toolbar titleToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_web_view);
        getIntenrData();
        initTitle();
        mBinding.webviewDetail.loadUrl(mUrl);
        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                titleToolBar.setTitle(title);
            }
        };
        mBinding.webviewDetail.setWebChromeClient(wvcc);
    }


// 设置setWebChromeClient对象

    private void initTitle() {
        titleToolBar = mBinding.titleToolBar;
        setSupportActionBar(titleToolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.icon_back);
        }
        titleToolBar.setTitle(mTitle);
        titleToolBar.setOverflowIcon(ContextCompat.getDrawable(this,R.drawable.actionbar_more));
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
//                        String shareText = mWebChromeClient.getTitle() + mUrl + "（分享自JayFun）";
//                        ShareUtils.share(WebViewActivity.this, shareText);
                        break;
                    case R.id.actionbar_cope:// 复制链接
                        BaseTools.copy(mUrl);
                        ToastUtil.showToast("复制成功");
                        break;
                    case R.id.actionbar_open:// 打开链接
                        BaseTools.openLink(WebViewActivity.this, mUrl);
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

    public static void loadUrl(Context mContext , String mUrl, String mTitle){
        Intent intent = new Intent(mContext, WebViewActivity.class);
        intent.putExtra("mUrl",mUrl);
        intent.putExtra("mTitle", mTitle);
        mContext.startActivity(intent);
    }

    public void getIntenrData() {
        mTitle = getIntent().getStringExtra("mTitle");
        mUrl = getIntent().getStringExtra("mUrl");
    }
    private void initWebView() {
        mBinding.pbProgress.setVisibility(View.VISIBLE);
        WebSettings ws = mBinding.webviewDetail.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 缩放比例 1
        mBinding.webviewDetail.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

//        mWebChromeClient = new MyWebChromeClient(this);
//        mBinding.webviewDetail.setWebChromeClient(mWebChromeClient);
        // 与js交互
//        mBinding.webviewDetail.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
//        mBinding.webviewDetail.setWebViewClient(new MyWebViewClient(this));
    }

}
