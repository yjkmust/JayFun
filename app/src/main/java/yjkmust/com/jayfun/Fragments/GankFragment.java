package yjkmust.com.jayfun.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import yjkmust.com.jayfun.FragmentPagerAdapter.MyFragmentPagerAdapter;
import yjkmust.com.jayfun.Fragments.DouFragments.BookCustomFragment;
import yjkmust.com.jayfun.Fragments.DouFragments.BookCustomOtherFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.AndroidFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.CustomFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.CustombysSelfFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.EveryDayFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.WelFareFragment;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.databinding.FragmentGankBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class GankFragment extends BaseFragment<FragmentGankBinding> {
    private ArrayList<String> mTitleList = new ArrayList<>();
    private ArrayList<Fragment> mFragmnts = new ArrayList<>();

    @Override
    protected void onInVisible() {

    }

    @Override
    public int setContent() {
        return R.layout.fragment_gank;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoading();
        initFragmentList();
        initViewPager();

    }

    private void initViewPager() {
        MyFragmentPagerAdapter myAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), mFragmnts, mTitleList);
        bindingView.vpGank.setAdapter(myAdapter);
        //左右预加载的页面个数
        bindingView.vpGank.setOffscreenPageLimit(3);
        myAdapter.notifyDataSetChanged();
        bindingView.tabGank.setTabMode(TabLayout.MODE_FIXED);
        bindingView.tabGank.setupWithViewPager(bindingView.vpGank);
        showContentView();
    }

    private void initFragmentList() {
        mTitleList.add("新闻");
        mTitleList.add("安卓");
        mTitleList.add("福利");
        mTitleList.add("书籍");
        mFragmnts.add(new CustombysSelfFragment());
        mFragmnts.add(new AndroidFragment());
        mFragmnts.add(new WelFareFragment());
        mFragmnts.add(new BookCustomOtherFragment());

    }


}
