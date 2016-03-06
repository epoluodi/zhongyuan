package com.apollo.elevator_check;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.WebService.WebThreadDo;
import com.apollo.elevator_check.WebService.Webservice;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;


public class setting extends Activity {
    Button downloaddb;
    Button buttonsave;
    EditText editTextserver;
    RelativeLayout r1;
    Message message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        buttonsave = (Button)findViewById(R.id.buttonsave);
        buttonsave.setOnClickListener(onClickListenersave);
        editTextserver = (EditText)findViewById(R.id.textserver);
        downloaddb = (Button)findViewById(R.id.buttondownload);
        downloaddb.setOnClickListener(onClickListenerdownloaddb);
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

}
