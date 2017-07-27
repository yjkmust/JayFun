package yjkmust.com.jayfun;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import yjkmust.com.jayfun.Util.ShareUtils;
import yjkmust.com.jayfun.databinding.ActivityNavHomePagerBinding;

public class NavHomePagerActivity extends AppCompatActivity {

    private ActivityNavHomePagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_home_pager);
        binding.toolbarLayout.setTitle("JayFun");
        binding.fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareUtils.share(view.getContext(),"强烈推荐！");
            }
        });
    }
    public static void startHome(Context context){
        Intent intent = new Intent(context, NavHomePagerActivity.class);
        context.startActivity(intent);
    }
}
