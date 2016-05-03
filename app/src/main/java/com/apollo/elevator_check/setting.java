package com.apollo.elevator_check;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.WebService.WebThreadDo;
import com.apollo.elevator_check.WebService.Webservice;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class setting extends Activity {
    Button downloaddb;
    Button buttonsave;
    Button btndbrestore,btnupdateapp;
    EditText editTextserver;
    RelativeLayout r1;
    Message message;

    DownloadManager downloadManager;
    DownloadCompleteReceiver downloadCompleteReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        buttonsave = (Button)findViewById(R.id.buttonsave);
        buttonsave.setOnClickListener(onClickListenersave);
        editTextserver = (EditText)findViewById(R.id.textserver);
        downloaddb = (Button)findViewById(R.id.buttondownload);
        downloaddb.setOnClickListener(onClickListenerdownloaddb);
        btndbrestore = (Button)findViewById(R.id.btndbrestore);
        btndbrestore.setOnClickListener(onClickListenerbtnrestore);
        btnupdateapp = (Button)findViewById(R.id.btnupdateapp);
        btnupdateapp.setOnClickListener(onClickListenerbtnupdateapp);
        r1=(RelativeLayout)findViewById(R.id.r1);
        if (CheckDbfile())
        {
            editTextserver.setText(Common.ServerIP);

        }
        else
        {
            Toast.makeText(this, "还没有设置网络信息,请设置服务地址", Toast.LENGTH_SHORT).show();
        }

    }


    boolean CheckDbfile()
    {
        File file = new File(Environment.getExternalStorageDirectory()+"/zhongyuan/config.json");
//        file.delete();
        return file.exists();
    }

    View.OnClickListener onClickListenerdownloaddb = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (Common.ServerWCF.equals(""))
            {
                Toast.makeText(setting.this,"请配置服务地址",Toast.LENGTH_SHORT).show();
                return;
            }


            Common.ShowPopWindow(r1,getLayoutInflater(),"正在下载");

            WebThreadDo webThreadDo = new WebThreadDo(null,"A_DownloadBaseDB");
            webThreadDo.requstWebinterfaceForbyte(true);
            byte[] buffer = (byte[])webThreadDo.amessage.obj;

            if (buffer == null)
            {

                Toast.makeText(setting.this,"下载失败",Toast.LENGTH_SHORT).show();
                Common.CLosePopwindow();
                return;
            }

            Toast.makeText(setting.this,"下载成功",Toast.LENGTH_SHORT).show();


            File file = new File(Environment.getExternalStorageDirectory()+ "/zhongyuan/zhongyuan.db");
            if (file.exists())
                file.delete();


            try {

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(buffer);
                fileOutputStream.close();


            }
            catch (Exception e)
            {

            }

            Common.CLosePopwindow();

        }
    };


    View.OnClickListener onClickListenerbtnrestore = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            AlertDialog.Builder builder = new AlertDialog.Builder(setting.this);
            builder.setTitle("重要提示").setMessage("初始化后，所有没有上传的数据都将被清空!");
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    File file = new File(Environment.getExternalStorageDirectory() + "/zhongyuan/main.db");
                    file.delete();
                    Common.CopyDb(setting.this);
                    Toast.makeText(setting.this,"初始化成功",Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("取消",null);
            AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }
    };


    View.OnClickListener onClickListenerbtnupdateapp = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Common.ShowPopWindow(getCurrentFocus(),getLayoutInflater(),"正在更新");
            String updateurl = String.format("http://%1$s:8091/UpdateApp/zhongyuan.apk",Common.ServerIP);
            downloadCompleteReceiver = new DownloadCompleteReceiver();
            DownloadServer downloadServer = new DownloadServer(getApplicationContext(), downloadCompleteReceiver);

            registerReceiver(downloadCompleteReceiver, new IntentFilter(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            downloadManager = downloadServer.initDownloadServer(updateurl);
            if (downloadManager == null)
            {
                unregisterReceiver(downloadCompleteReceiver);
                Common.CLosePopwindow();
                Toast.makeText(setting.this,"更新失败",Toast.LENGTH_SHORT).show();
            }


        }
    };



    View.OnClickListener onClickListenersave = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String Serverip = editTextserver.getText().toString();
            if (Serverip.equals(""))
            {
                Toast.makeText(setting.this,"请输入服务地址",Toast.LENGTH_SHORT).show();
                return;
            }


            WebThreadDo webThreadDo = new WebThreadDo(null,"A_CheckNet");
            webThreadDo.requstWebinterfaceForString(true);
            Drawable drawable;
            if (webThreadDo.amessage.obj!=null && webThreadDo.amessage.obj.toString().equals("1"))
            {
                drawable = getResources().getDrawable(R.drawable.ok);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                editTextserver.setCompoundDrawables(null, null, drawable, null);
            }
            else
            {
                drawable = getResources().getDrawable(R.drawable.wrong);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                editTextserver.setCompoundDrawables(null, null, drawable, null);
            }


            JSONObject jsonObject = new JSONObject();
            try
            {
                jsonObject.put("server",Serverip);

                File file = new File(Environment.getExternalStorageDirectory()+"/zhongyuan/config.json");

                if (file.exists())
                    file.delete();

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(jsonObject.toString().getBytes());
                fileOutputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();

            }
            Common.ServerIP=Serverip;
            Common.ServerWCF = String.format("Http://%1$s:9229/",Common.ServerIP);
            Webservice.InitService(Common.ServerWCF);
            Toast.makeText(setting.this,"保存成功",Toast.LENGTH_SHORT).show();

            
        }
    };


    // 接受下载完成后的intent
    public class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                //自动安装apk
                unregisterReceiver(downloadCompleteReceiver);
                Common.CLosePopwindow();
                installAPK(downloadManager.getUriForDownloadedFile(downId));
            }
        }

        /**
         * 安装apk文件
         */
        private void installAPK(Uri apk) {

            // 通过Intent安装APK文件
            if (apk ==null)
            {

                Toast.makeText(setting.this,"下载更新失败，请重新尝试",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intents = new Intent();
            intents.setAction("android.intent.action.VIEW");
            intents.addCategory("android.intent.category.DEFAULT");
            intents.setType("application/vnd.android.package-archive");
            intents.setData(apk);
            intents.setDataAndType(apk, "application/vnd.android.package-archive");
            intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intents);
            finish();


        }

    }


    public class DownloadServer {
        Context context;
        DownloadManager downloadManager;
        setting.DownloadCompleteReceiver downloadCompleteReceiver;

        /**
         * 初始化下载器 *
         */

        public DownloadServer(Context context1, setting.DownloadCompleteReceiver downloadCompleteReceiver1) {
            downloadCompleteReceiver = downloadCompleteReceiver1;
            context = context1;
        }

        public DownloadManager initDownloadServer(final String updateurl) {

            try {
                downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);

                //设置下载地址
                DownloadManager.Request down = new DownloadManager.Request(
                        Uri.parse(updateurl));
                down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                        | DownloadManager.Request.NETWORK_WIFI);
                down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                down.setVisibleInDownloadsUi(true);
                down.setDestinationInExternalFilesDir(context,
                        Environment.DIRECTORY_DOWNLOADS, "zhongyuan.apk");
                downloadManager.enqueue(down);

                return downloadManager;
            }
            catch (Exception e)
            {
                return null;
            }

        }


    }


}
