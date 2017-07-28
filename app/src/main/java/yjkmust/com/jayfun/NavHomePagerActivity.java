package yjkmust.com.jayfun;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import yjkmust.com.jayfun.Util.ShareUtils;
import yjkmust.com.jayfun.databinding.ActivityNavHomePagerBinding;

public class NavHomePagerActivity extends SwipeBackActivity {

    private ActivityNavHomePagerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nav_home_pager);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
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
