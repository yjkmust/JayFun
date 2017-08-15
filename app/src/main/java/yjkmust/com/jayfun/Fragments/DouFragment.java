package yjkmust.com.jayfun.Fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;
import com.yjkmust.onepeace.View.WaveView;

import java.util.ArrayList;
import java.util.List;

import yjkmust.com.jayfun.App.MyApplication;
import yjkmust.com.jayfun.FragmentPagerAdapter.MyFragmentPagerAdapter;
import yjkmust.com.jayfun.Fragments.GankFragments.AndroidFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.CustomFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.EveryDayFragment;
import yjkmust.com.jayfun.Fragments.GankFragments.WelFareFragment;
import yjkmust.com.jayfun.LoginActivity;
import yjkmust.com.jayfun.R;
import yjkmust.com.jayfun.Util.ToastUtil;
import yjkmust.com.jayfun.databinding.ActivityLoginBinding;

/**
 * Created by GEOFLY on 2017/6/28.
 */

public class DouFragment extends BaseFragment<ActivityLoginBinding> {
    List<String> mTitles = new ArrayList<>();
    List<Fragment> mFragments = new ArrayList<>();
    private View mLoginFormView;
    private SpinKitView mProgressView;
    private AutoCompleteTextView tvUser;
    private EditText tvPassword;
    private UserLoginTask userLoginTask;


    @Override
    public int setContent() {
        return R.layout.activity_login;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showContentView();
        initUserPic();
        mLoginFormView = bindingView.loginForm;
        mProgressView = bindingView.loginProgress;
        tvUser = bindingView.email;
        tvPassword = bindingView.password;
        bindingView.emailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AutoLogin();
            }
        });
    }
    private void initUserPic(){
        final FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(-2, -2);
        lp.gravity = Gravity.BOTTOM|Gravity.CENTER;
        bindingView.waveView.setOnWaveAnimationListener(new WaveView.onWaveAnimationListener() {
            @Override
            public void onWaveAnimation(float y) {
                lp.setMargins(0,0,0,(int) y+2);
                bindingView.ivUser.setLayoutParams(lp);
            }
        });
    }
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
//            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public class UserLoginTask extends AsyncTask<Void,Void,Boolean>{
        private String user;
        private String password;
        public UserLoginTask(String user, String password){
            this.user = user;
            this.password = password;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            showProgress(false);
            if (aBoolean){
                ToastUtil.showToast("登录成功");
            }else{
                ToastUtil.showToast("用户错误或者密码错误");
                bindingView.password.requestFocus();
                bindingView.password.setError("密码错误");
            }
        }
    }
    private void AutoLogin(){
        tvUser.setError(null);
        tvPassword.setError(null);
        String userContent = tvUser.getText().toString();
        String psContent = tvPassword.getText().toString();
        if (TextUtils.isEmpty(userContent)){
            tvUser.requestFocus();
            tvUser.setError("请输入用户名！");
            return;
        }
        if (TextUtils.isEmpty(psContent)){
            tvPassword.requestFocus();
            tvPassword.setError("请输入密码");
            return;
        }
        showProgress(true);
        userLoginTask = new UserLoginTask(userContent,psContent);
        userLoginTask.execute((Void) null);
    }
}
