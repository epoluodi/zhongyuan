package com.apollo.elevator_check;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.drawable.BitmapDrawable;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.Common.signaturepad.views.SignaturePad;
import com.apollo.elevator_check.WebService.WebThreadDo;

import org.ksoap2.serialization.PropertyInfo;

import java.io.File;
import java.io.FileInputStream;


public class taskfinish extends Activity {

    String LiftNO,ContractNO;
    String owername="";
    String OwnerConfirmStatus= "满意";
    String tableindex ="1";
    String pxid;
    EditText scancode;
    Button bconfim,btnquestcode,btnrefresh,btntel;
    RadioButton radioButton1;
    RadioButton radioButton2;
    Button btnsign;
    String Signfile="";
    ImageView sigimg;
    Boolean isrequest=false;
    String code="";
    String khmc;

    private boolean isScaning = false;

    private SoundPool soundpool = null;
    private int soundid;
    private String barcodeStr;
    private final static String SCAN_ACTION = "urovo.rcv.message";//扫描结束action


    private BroadcastReceiver mScanReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            isScaning = false;
//            soundpool.play(soundid, 1, 1, 0, 0, 1);

            if (isrequest==false)
            {
                Toast.makeText(taskfinish.this, "需要请求助理获得许可，才可以扫描雇主条码", Toast.LENGTH_SHORT).show();
                return;
            }
//            byte[] barocode = intent.getByteArrayExtra("barocode");
//            byte[] barcode = intent.getByteArrayExtra("barcode");
//            int barocodelen = intent.getIntExtra("length", 0);
//            byte temp = intent.getByteExtra("barcodeType", (byte) 0);
//            android.util.Log.i("debug", "----codetype--" + temp);
//            barcodeStr = new String(barcode, 0, barocodelen);
            barcodeStr = intent.getExtras().getString ("data");

            scancode.setText(barcodeStr);
            onKeyListenerliftno.onKey(scancode,0,new KeyEvent(KeyEvent.ACTION_UP,66));
            scancode.requestFocus();

        }

    };

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        unregisterReceiver(mScanReceiver);
    }


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
        setContentView(R.layout.activity_taskfinish);
        Bundle bundle = getIntent().getExtras();
        LiftNO = bundle.getString("LiftNO");
        ContractNO = bundle.getString("ContractNO");
        pxid=bundle.getString("pxid");
        khmc=bundle.getString("khmc");
//        tableindex = bundle.getString("TableType");
        sigimg = (ImageView)findViewById(R.id.imgsign);
        scancode =(EditText)findViewById(R.id.customercode);
        scancode.setOnKeyListener(onKeyListenerliftno);
        bconfim =(Button)findViewById(R.id.buttonok);
        bconfim.setOnClickListener(onClickListener);
        btnsign = (Button)findViewById(R.id.btnsign);
        btnsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(taskfinish.this,SignaturepadViewPlugin.class);
                Bundle bundle=new Bundle();
                bundle.putString("pxid",pxid);
                bundle.putString("LiftNO",LiftNO);
                intent.putExtras(bundle);
                startActivityForResult(intent, SignaturepadViewPlugin.SIGNATUREPADRESULTREQUEST);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });


        btnquestcode =(Button)findViewById(R.id.btnquestcode);
        btnrefresh =(Button)findViewById(R.id.btnrefresh);
        btnrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(code.equals(""))
                {
                    Toast.makeText(taskfinish.this,"请先发送申请许可",Toast.LENGTH_SHORT).show();
                    return;
                }
                PropertyInfo[] propertyInfos = new PropertyInfo[1];
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("uuid");
                propertyInfo.setValue(code);
                propertyInfos[0] = propertyInfo;

                WebThreadDo webThreadDo = new WebThreadDo(propertyInfos,"A_PDA_getquest");
                webThreadDo.requstWebinterfaceForString(true);
                String buffer = (String)webThreadDo.amessage.obj;

                if (buffer.equals("0") || buffer.equals("-1"))
                {
                    Toast.makeText(taskfinish.this,"没有获得许可,请再次尝试",Toast.LENGTH_SHORT).show();
                    return;
                }
                Signfile="";
                sigimg.setImageDrawable(getResources().getDrawable(R.drawable.sing_ico));

                isrequest=true;
                scancode.setEnabled(true);


                Toast.makeText(taskfinish.this,"获得许可" ,Toast.LENGTH_SHORT).show();

            }
        });
        btntel =(Button)findViewById(R.id.btntel);
        btnquestcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PropertyInfo[] propertyInfos = new PropertyInfo[6];
                PropertyInfo propertyInfo = new PropertyInfo();
                propertyInfo.setName("pxid");
                propertyInfo.setValue(pxid);
                propertyInfos[0] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("htbh");
                propertyInfo.setValue(ContractNO);
                propertyInfos[1] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("worker1");
                propertyInfo.setValue(Common.work1name);
                propertyInfos[2] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("worker2");
                propertyInfo.setValue(Common.work2name);
                propertyInfos[3] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("khmc");
                propertyInfo.setValue(khmc);
                propertyInfos[4] = propertyInfo;
                propertyInfo = new PropertyInfo();
                propertyInfo.setName("dtbh");
                propertyInfo.setValue(LiftNO);
                propertyInfos[5] = propertyInfo;


                WebThreadDo webThreadDo = new WebThreadDo(propertyInfos,"A_PDA_submitquest");
                webThreadDo.requstWebinterfaceForString(true);
                String buffer = (String)webThreadDo.amessage.obj;

                if (buffer.equals("0") || buffer.equals("-1"))
                {
                    Toast.makeText(taskfinish.this,"请求许可失败，请重新尝试",Toast.LENGTH_SHORT).show();
                    return;
                }

                code =buffer;
                Toast.makeText(taskfinish.this,"请求许可成功，请稍后点击刷新获得许可" ,Toast.LENGTH_SHORT).show();

            }
        });


        File file=new File(Environment.getExternalStorageDirectory(),
                String.format("zhongyuan/%1$s.png",pxid));
        if (file.exists()) {
            Signfile = pxid;
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                BitmapDrawable bitmapDrawable= new BitmapDrawable(fileInputStream);
                sigimg.setImageDrawable(bitmapDrawable);
                fileInputStream.close();
            }
            catch (Exception e)
            {e.printStackTrace();}
        }

        btntel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //传入服务， parse（）解析号码
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:18905271111"));
                //通知activtity处理传入的call服务
                startActivity(intent);

            }
        });

        radioButton1=(RadioButton)findViewById(R.id.r_ok);
        radioButton2=(RadioButton)findViewById(R.id.r_no);
        radioButton1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    OwnerConfirmStatus = "满意";
            }
        });

        radioButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    OwnerConfirmStatus = "不满意";
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {



            if (Signfile!="")
                Common.mainDB.updatetaskfinish(ContractNO,"sign_" + Signfile,OwnerConfirmStatus);
            else {

                if (owername.equals(""))
                {
                    Toast.makeText(taskfinish.this, "请雇主签字或者扫描雇主条码", Toast.LENGTH_SHORT).show();
                    return;
                }
                Common.mainDB.updatetaskfinish(ContractNO, owername, OwnerConfirmStatus);
            }
            setResult(1,null);
            finish();

        }
    };
    View.OnKeyListener onKeyListenerliftno = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            String owercode = scancode.getText().toString();
            if (keyEvent.getKeyCode()==66 && keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                if (owercode.equals(""))
                {
                    Toast.makeText(taskfinish.this, "请扫描雇主编号", Toast.LENGTH_SHORT).show();
                    return false;
                }




                owername  = Common.BaseDB.Getowername(owercode,pxid);
                if (owername.equals(""))
                {
                    Toast.makeText(taskfinish.this,"雇主信息不存在",Toast.LENGTH_SHORT).show();
                    return false;
                }

                scancode.setText(owername);
                setTitle( owername + "--雇主确认");


            }

            return false;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SignaturepadViewPlugin.SIGNATUREPADRESULTREQUEST)
        {
            if (resultCode==1) {
                Signfile = data.getExtras().getString("file");
                File file=new File(Environment.getExternalStorageDirectory(),
                        String.format("zhongyuan/%1$s.png",Signfile));
                try {
                    FileInputStream fileInputStream = new FileInputStream(file);
                    BitmapDrawable bitmapDrawable= new BitmapDrawable(fileInputStream);
                    sigimg.setImageDrawable(bitmapDrawable);
                    fileInputStream.close();
                }
                catch (Exception e)
                {e.printStackTrace();}




                owername="雇主已签字";
            }
            return;
        }


        if (data == null)
            return;
        String code = data.getExtras().getString("code");


        scancode.setText(code);
        owername  = Common.BaseDB.Getowername(code,pxid);
        if (owername.equals(""))
        {
            Toast.makeText(taskfinish.this,"雇主信息不存在",Toast.LENGTH_SHORT).show();
            return ;
        }

        setTitle( owername + "--雇主确认");

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode ==4)
        {
            setResult(1,null);
            finish();
        }
        return true;

    }

}
