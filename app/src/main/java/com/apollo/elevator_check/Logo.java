package com.apollo.elevator_check;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.barcodescandemo.ScannerInerface;
import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.WebService.Webservice;
import com.apollo.elevator_check.db.DBManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static android.widget.Toast.LENGTH_SHORT;


public class Logo extends Activity {
    ImageView imageViewsetting;
    EditText editText1;
    EditText editText2;
    Button loginbutton;
    RadioButton radioButton1,radioButton2;
    ScannerInerface scannerInerface;
    private boolean isScaning = false;
//    private ScanManager mScanManager;
    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private final static String SCAN_ACTION = "urovo.rcv.message";//扫描结束action


    Thread thread;
    Boolean iswhile=false;
    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;

//            soundpool.play(soundid, 1, 1, 0, 0, 1);


//            byte[] barocode = intent.getByteArrayExtra("barocode");
//            byte[] barcode = intent.getByteArrayExtra("barcode");
//            byte[] barcode = intent.getByteArrayExtra("scandata");
//            int barocodelen = intent.getIntExtra("length", 0);
//
//            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
//            android.util.Log.i("debug", "----codetype--" + temp);
//            barcodeStr = new String(barcode, 0, barocodelen);

//            barcodeStr = intent.getExtras().getString ("data");
            barcodeStr = intent.getStringExtra("value");
            Log.i("scandata:",barcodeStr);
            if (editText1.isFocused())
            {
                editText1.setText(barcodeStr);
                editText2.requestFocus();

                return;
            }
           if (editText2.isFocused())
           {
               editText2.setText(barcodeStr);
               onClickListenerlogin.onClick(loginbutton);

               return;
           }
        }

    };

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        initScan();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Common.SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);
        imageViewsetting =(ImageView)findViewById(R.id.setting);
        imageViewsetting.setOnClickListener(onClickListenersetting);
        editText1 =(EditText) findViewById(R.id.wordk1);

        radioButton1 = (RadioButton)findViewById(R.id.dd);
        radioButton2 = (RadioButton)findViewById(R.id.idata);
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    Common.scantype=1;
                else {
                    Common.scantype = 2;
                    initidatascan();
                }
            }
        });


        editText1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

            Intent intent=new Intent(Logo.this,CaptureActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("type",0);
            intent.putExtras(bundle);
            startActivityForResult(intent, 0);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


                return false;
            }
        });
        editText2 =(EditText) findViewById(R.id.wordk2);
        editText2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Intent intent=new Intent(Logo.this,CaptureActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type",1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


                return false;
            }
        });
        loginbutton =(Button)findViewById(R.id.buttonlogin);
        loginbutton.setOnClickListener(onClickListenerlogin);

        Common.Appcontext =getApplication();
        File file = new File(Environment.getExternalStorageDirectory() + "/zhongyuan/");
        if (!file.exists())
            file.mkdir();

        Common.ServerIP = Common.GetconfigServer();
        if (Common.ServerIP.equals(""))
        {

            Intent intent =new Intent(Logo.this,setting.class);
            Bundle bundle=new Bundle();
            bundle.putInt("type",1);
            intent.putExtras(bundle);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
        else
        {
            Common.ServerWCF = String.format("Http://%1$s:9229/",Common.ServerIP);

            Webservice.InitService(Common.ServerWCF);


        }

        boolean isfirst =CheckDbfile();
        if (isfirst ==false)
            Common.CopyDb(Logo.this);



        initidatascan();
    }

    private void initidatascan()
    {
        scannerInerface = new ScannerInerface(this);
        scannerInerface.open();
        scannerInerface.enablePlayBeep(true);
        scannerInerface.lockScanKey();
        scannerInerface.setOutputMode(1);

    }


    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        unregisterReceiver(mScanReceiver);
    }


    View.OnClickListener onClickListenersetting = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(Logo.this,setting.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        String code = data.getExtras().getString("code");
        int type = data.getExtras().getInt("type");
        if (requestCode==0)
        {
            if (type ==0)
                 editText1.setText(code);
            if (type ==1)
                editText2.setText(code);


        }
    }

    View.OnClickListener onClickListenerlogin = new View.OnClickListener() {
        @Override
        public void onClick(View view) {



//            return;
            if (editText1.getText().toString().equals("") ||
                editText2.getText().toString().equals(""))
            {
                Toast.makeText(Logo.this,"请输入工号1和工号2",Toast.LENGTH_SHORT).show();
                return;
            }
            String work1 = editText1.getText().toString();
            String work2 = editText2.getText().toString();
            if (work1.equals(work2))
            {
                Toast.makeText(Logo.this,"登录工号不能一样",Toast.LENGTH_SHORT).show();
                editText1.setText("");
                editText2.setText("");
                editText1.setFocusable(true);
                return;
            }
            if (!CheckDbfile2())
            {
                Toast.makeText(Logo.this,"请同步数据",Toast.LENGTH_SHORT).show();
                return;
            }

            DBManager dbManager = new DBManager(getApplicationContext(),
                    Environment.getExternalStorageDirectory() + "/zhongyuan/zhongyuan.db");
            Common.BaseDB = dbManager;
            dbManager = new DBManager(getApplicationContext(),
                    Environment.getExternalStorageDirectory() + "/zhongyuan/main.db");
            Common.mainDB = dbManager;

            String work1name = Common.BaseDB.Getworkname(work1);
            if (work1name.equals(""))
            {
                Toast.makeText(Logo.this,"没有该工号信息",Toast.LENGTH_SHORT).show();
                return;
            }
            String work2name = Common.BaseDB.Getworkname(work2);
            if (work2name.equals(""))
            {
                Toast.makeText(Logo.this,"没有该工号信息",Toast.LENGTH_SHORT).show();
                return;
            }

            Common.work1 = work1;
            Common.work2=work2;
            Common.work1name=work1name;
            Common.work2name=work2name;



            Intent intent =new Intent(Logo.this,main.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
    };

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        //退出程序控制
        if (keyCode == 4) {


            Toast.makeText(this, "请长按退出", LENGTH_SHORT).show();


            return false;
        }
        return super.onKeyUp(keyCode,event);
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode ==4) {
            Common.projectcode = "";
            iswhile=false;
            if (thread!=null) {
                thread.interrupt();
                thread=null;
            }
            finish();
        }
        return super.onKeyLongPress(keyCode, event);

    }


    boolean CheckDbfile()
    {
        File file = new File(Environment.getExternalStorageDirectory() + "/zhongyuan/main.db");
//        file.delete();
        return file.exists();
    }
    boolean CheckDbfile2()
    {
        File file = new File(Environment.getExternalStorageDirectory() + "/zhongyuan/zhongyuan.db");
//        file.delete();
        return file.exists();
    }




}
