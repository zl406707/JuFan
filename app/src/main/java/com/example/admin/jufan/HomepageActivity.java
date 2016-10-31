package com.example.admin.jufan;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import GengXin.Conf;
import GengXin.UpdateEntity;
import GengXin.UpdateTools;
import fragment.ShouyeFragMent;
import fragment.WodeFragMent;

public class HomepageActivity extends FragmentActivity implements View.OnClickListener {

    private FrameLayout viewpager;
    private RadioButton shouye;
    private RadioButton zhibo;
    private RadioButton wode;
    private List<Fragment> listfra;

    /**
     * 消息机制
     */
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            switch (msg.what) {
                case 0:
                    dialog.setMessage(updateEntity.getDescription());
                    dialog.show();
                    break;

                case 1:
                    downLoadApk();
                    break;
            }


        }

    };
    private AlertDialog dialog;
    private UpdateEntity updateEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        init();
        banben();
    }



    private void init() {
        viewpager =(FrameLayout) findViewById(R.id.homepager);

        findViewById(R.id.shouye).setOnClickListener(HomepageActivity.this);

        findViewById(R.id.myzhibo).setOnClickListener(this);
       findViewById(R.id.wode).setOnClickListener(this);

        listfra = new ArrayList<Fragment>();
        listfra.add(new ShouyeFragMent());
        listfra.add(new WodeFragMent());


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shouye:
                FragmentManager supportFragmentManager = getSupportFragmentManager();
                FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();

                ShouyeFragMent fa=new ShouyeFragMent();

                beginTransaction.replace(R.id.homepager,fa);
                beginTransaction.commit();
                break;
            case R.id.myzhibo:
                Intent intent=new Intent(HomepageActivity.this,MyZhiBoActivity.class);
                startActivity(intent);
                break;
            case R.id.wode:
                FragmentManager supportFragmentManager1 = getSupportFragmentManager();
                FragmentTransaction beginTransaction1 = supportFragmentManager1.beginTransaction();

                WodeFragMent fa1=new WodeFragMent();

                beginTransaction1.replace(R.id.homepager,fa1);
                beginTransaction1.commit();
                break;
        }
    }


    //双击退出
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {




           exitBy2Click(); //调用双击退出函数

        return false;
    }

    private static Boolean isExit = false;
    private void exitBy2Click() {

        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();

            System.exit(0);
        }

    }

    //版本更新
    private void banben() {

        dialog = new AlertDialog.Builder(HomepageActivity.this).
                setTitle("升级提醒").
                setIcon(R.drawable.ic_launcher).
                setPositiveButton("在线升级", onclick).
                setNegativeButton("不想升级", null).
                create();




        //开启线程
        new Thread(new CheckVersionTask()).start();

    }

    DialogInterface.OnClickListener  onclick = new DialogInterface.OnClickListener(){

        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TODO Auto-generated method stub
            handler.sendEmptyMessage(1);
        }

    };

    /*
      * 从服务器中下载APK
      */
    protected void downLoadApk() {
        final ProgressDialog pd;    //进度条对话框
        pd = new  ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.show();
        new Thread(){
            @Override
            public void run() {
                try {
                    File file =  getFileFromServer(updateEntity.getUrl(), pd);
                    sleep(3000);

                    UpdateTools tools = new UpdateTools();
                    //安装APk
                    tools.installApk(file,HomepageActivity.this);
                    pd.dismiss(); //结束掉进度条对话框

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }}.start();
    }

    /*
      * 从服务器获取xml解析并进行比对版本号
     */
    public class CheckVersionTask implements Runnable{

        public void run() {
            try {
                //从资源文件获取服务器 地址
                String path = getResources().getString(R.string.serverurl);
                //包装成url的对象
                URL url = new URL(path);
                HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                InputStream is =conn.getInputStream();
                updateEntity =  UpdateTools.getUpdataInfo(is);

                int versionCode = HomepageActivity.this.getPackageManager().getPackageInfo(HomepageActivity.this.getPackageName(), 0).versionCode;

                if(Integer.parseInt(updateEntity.getVersion()) <= versionCode){
                    Log.i("xxx","版本号相同无需升级");
                }else{
                    Log.i("xxxx","版本号不同 ,提示用户升级 ");
                    handler.sendEmptyMessage(0);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载方法
     *
     * @param path
     * @param pd
     * @return
     * @throws Exception
     */
    public File getFileFromServer(String path, ProgressDialog pd)
            throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(),
                    "updata.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                // 获取当前下载量
                pd.setProgress(total);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    @Override
    protected void onPause() {
        Log.w(Conf.TAG, "Activity1.onPause()");
        // TODO Auto-generated method stub
        super.onPause();

    }

    @Override
    protected void onResume() {
        Log.w(Conf.TAG, "Activity1.onResume()");
        // TODO Auto-generated method stub
        super.onResume();

    }

}
