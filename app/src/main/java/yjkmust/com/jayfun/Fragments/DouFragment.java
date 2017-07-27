package yjkmust.com.jayfun.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import yjkmust.com.jayfun.FragmentPagerAdapter.MyFragmentPagerAdapter;
import yjkmust.com.jayfun.Fragments.GankFragments.AndroidFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.CustomFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.EveryDayFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.WelFareFragment;
import yjkmust.com.jayfun.R;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class DouFragment extends BaseFragment {
    List<String> mTitles = new ArrayList<>();
    List<Fragment> mFragments = new ArrayList<>();

    @Override
    public int setContent() {
        return R.layout.fragment_base;
    }


}
