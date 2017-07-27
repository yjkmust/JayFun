package yjkmust.com.jayfun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabHost;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import yjkmust.com.jayfun.Adapter.welfarePagerAdapter;
import yjkmust.com.jayfun.Util.ToastUtil;
import yjkmust.com.jayfun.databinding.ActivityViewBigImageBinding;

public class ViewBigImageActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener{

    private int select;// 用于判断是头像还是文章图片 1:头像 2：文章大图
    private int code;//选中的页数
    private ArrayList<String> imgUrl;//图片网址数组
    private ActivityViewBigImageBinding mbinding;
    private Activity mActivity;
    private welfarePagerAdapter adapter;
    private int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mbinding = DataBindingUtil.setContentView(this, R.layout.activity_view_big_image);
        getView();
    }

    private void getView() {
        Bundle bundle = getIntent().getExtras();
        select = bundle.getInt("select", 2);
        code = bundle.getInt("code", 1);
        imgUrl = bundle.getStringArrayList("imgUrl");
        adapter = new welfarePagerAdapter(imgUrl, getLayoutInflater(), mActivity);
        mbinding.vpViewpager.setAdapter(adapter);
        page = code;
        mbinding.vpViewpager.setOnPageChangeListener(this);
        mbinding.vpViewpager.setCurrentItem(page);
        mbinding.vpViewpager.setEnabled(false);
        //设置当前页数和总页数
        if (select==2){
            mbinding.tvPager.setText((page+1)+"/"+imgUrl.size());
        }
        mbinding.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast(getApplicationContext(),"开始下载图片！");
                final BitmapFactory.Options options = new BitmapFactory.Options();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final String imagetPath = getImagePath(imgUrl.get(page));
                        ViewBigImageActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (imagetPath!=null){
                                    Bitmap bitmap = BitmapFactory.decodeFile(imagetPath, options);
                                    if (bitmap!=null){
                                        try {
                                            saveImageToGallery(ViewBigImageActivity.this, bitmap);

                                            ToastUtil.showToast(getApplicationContext(),"已保存至"+Environment.getExternalStorageDirectory().getAbsolutePath()+"JayFunPic");
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }
                            }
                        });
                    }
                }).start();
            }
        });

    }

    /**
     *Glide缓存图片的路径
     */
    private String getImagePath(String imgUrl){
        String path = null;
        FutureTarget<File> future = Glide.with(this).load(imgUrl)
                .downloadOnly(500,500);
        File cacheFile = null;
        try {
            cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException|ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     *保存图片到相册
     */
    private void saveImageToGallery(Context context, Bitmap bmp) throws FileNotFoundException {
        File file = new File(Environment.getExternalStorageDirectory(),"JayFunPic");
        if (!file.exists()){
            file.mkdir();
        }
        String fileName = System.currentTimeMillis()+".jpg";
        File filePic = new File(file, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(filePic);
            bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
            fos.flush();
            fos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        //其次把文件插入到系统图库
        MediaStore.Images.Media.insertImage(context.getContentResolver(), filePic.getAbsolutePath(), fileName, null);
        //最后通知图库刷新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://"+filePic.getAbsoluteFile())));

    }
    private void savePic(View v){

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mbinding.tvPager.setText((position+1)+"/"+imgUrl.size());
        Log.d("TAG", "onPageSelected: ");

    }

    @Override
    public void onPageScrollStateChanged(int state) {
//        onBackPressed();
    }

    @Override
    public void onTabChanged(String tabId) {
//        onBackPressed();
    }
}
