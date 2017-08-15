package yjkmust.com.jayfun;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import yjkmust.com.jayfun.FragmentPagerAdapter.MyFragmentPagerAdapter;
import yjkmust.com.jayfun.Fragments.DouFragment;
import yjkmust.com.jayfun.Fragments.GankFragment;
import yjkmust.com.jayfun.Fragments.OneFragment;
import yjkmust.com.jayfun.Util.PerfectClickListener;
import yjkmust.com.jayfun.databinding.ActivityMainBinding;
import yjkmust.com.jayfun.databinding.NavHeaderMainBinding;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,ViewPager.OnPageChangeListener {

    private ActivityMainBinding mBinding;
    private Toolbar toolbar;
    private FrameLayout llTitleMenu;
    private ImageView ivTitleDou;
    private ImageView ivTitleGank;
    private ImageView ivTitleOne;
    private NavHeaderMainBinding bind;
    private ViewPager vpContent;
    private DrawerLayout drawerLayout;
    private String TAG ="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();
        initListener();
        initDrawerLayout();
        initContentFragment();

    }

    private void initContentFragment() {
        List<Fragment> mFragments = new ArrayList<>();
        Fragment GankFragmnt = new GankFragment();
        Fragment OneFragment = new OneFragment();
        Fragment DouFragment = new DouFragment();
        mFragments.add(GankFragmnt);
        mFragments.add(OneFragment);
        mFragments.add(DouFragment);
        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),mFragments);
        mBinding.include.vpContent.setAdapter(pagerAdapter);
        // 设置ViewPager最大缓存的页面个数(cpu消耗少)
        mBinding.include.vpContent.setOffscreenPageLimit(2);
        mBinding.include.vpContent.addOnPageChangeListener(this);
        mBinding.include.ivTitleGank.setSelected(true);
        mBinding.include.vpContent.setCurrentItem(0);


    }

    private void initDrawerLayout() {
        mBinding.navView.inflateHeaderView(R.layout.nav_header_main);
        View headerview = mBinding.navView.getHeaderView(0);
        bind = DataBindingUtil.bind(headerview);
        bind.setListener(this);
        bind.llNavHomepage.setOnClickListener(listener);
        bind.llNavScanDownload.setOnClickListener(listener);
        bind.llNavDeedback.setOnClickListener(listener);
        bind.llNavAbout.setOnClickListener(listener);
        bind.llNavLogin.setOnClickListener(listener);
        bind.llNavExit.setOnClickListener(listener);

    }

    private void initListener() {
        llTitleMenu.setOnClickListener(this);
        ivTitleGank.setOnClickListener(this);
        ivTitleOne.setOnClickListener(this);
        ivTitleDou.setOnClickListener(this);

    }

    private void initView() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
        }
        toolbar = mBinding.include.toolbar;
        llTitleMenu = mBinding.include.llTitleMenu;
        ivTitleDou = mBinding.include.ivTitleDou;
        ivTitleGank = mBinding.include.ivTitleGank;
        ivTitleOne = mBinding.include.ivTitleOne;
        vpContent = mBinding.include.vpContent;
        drawerLayout = mBinding.drawerLayout;


    }
    private PerfectClickListener listener = new PerfectClickListener() {
        @Override
        protected void onNoDoubleClick(final View v) {
            mBinding.drawerLayout.closeDrawer(GravityCompat.START);
            mBinding.drawerLayout.postDelayed(new Runnable() {//postDelayed创造多线程消息的函数
                @Override
                public void run() {
                    switch (v.getId()) {
                        case R.id.ll_nav_homepage:// 主页
                            NavHomePagerActivity.startHome(MainActivity.this);
                            break;
                        case R.id.ll_nav_scan_download://扫码下载
//
                            break;
                        case R.id.ll_nav_deedback:// 掘金社区
                             WebViewActivity.loadUrl(v.getContext(), "https://juejin.im/timeline", "掘金社区");
                            break;
                        case R.id.ll_nav_about:// 关于JayFun
//
                            break;
                        case R.id.ll_nav_login:// 登录GitHub账号
                            WebViewActivity.loadUrl(v.getContext(), "https://github.com/login", "登录GitHub账号");
                            break;
                        case R.id.ll_nav_exit:
                           ExitApp();
                           break;
                    }
                }
            }, 260);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_title_menu://开启侧边栏
                mBinding.drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_title_gank://开启干货栏
                if (vpContent.getCurrentItem()!=0){
                    ivTitleGank.setSelected(true);
                    ivTitleOne.setSelected(false);
                    ivTitleDou.setSelected(false);
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.iv_title_one://开启电影栏
                if (vpContent.getCurrentItem()!=1){
                    ivTitleGank.setSelected(false);
                    ivTitleOne.setSelected(true);
                    ivTitleDou.setSelected(false);
                    vpContent.setCurrentItem(1);
                }
                break;
            case R.id.iv_title_dou://开启书籍栏
                if (vpContent.getCurrentItem()!=2){
                    ivTitleGank.setSelected(false);
                    ivTitleOne.setSelected(false);
                    ivTitleDou.setSelected(true);
                    vpContent.setCurrentItem(2);
                }
                break;
            default:
                break;

        }

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                ivTitleGank.setSelected(true);
                ivTitleOne.setSelected(false);
                ivTitleDou.setSelected(false);
                break;
            case 1:
                ivTitleGank.setSelected(false);
                ivTitleOne.setSelected(true);
                ivTitleDou.setSelected(false);
                break;
            case 2:
                ivTitleGank.setSelected(false);
                ivTitleOne.setSelected(false);
                ivTitleDou.setSelected(true);
                break;
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                // 不退出程序，进入后台
                moveTaskToBack(true);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    private void ExitApp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("提示");
        builder.setMessage("确认退出吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
