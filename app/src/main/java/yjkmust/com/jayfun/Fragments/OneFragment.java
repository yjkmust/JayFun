package yjkmust.com.jayfun.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import yjkmust.com.jayfun.R;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class OneFragment extends BaseFragment {
    @Override
    public int setContent() {
       return R.layout.fragment_one;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
    }
}
