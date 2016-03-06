package com.apollo.elevator_check;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;


public class taskfinish extends Activity {

    String LiftNO,ContractNO;
    String owername="";
    String OwnerConfirmStatus= "满意";
    String tableindex ="1";
    EditText scancode;
    Button bconfim;
    RadioButton radioButton1;
    RadioButton radioButton2;
    Button scan1;

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
//        LiftNO = bundle.getString("LiftNO");
        ContractNO = bundle.getString("ContractNO");
//        tableindex = bundle.getString("TableType");

        scancode =(EditText)findViewById(R.id.customercode);
        scancode.setOnKeyListener(onKeyListenerliftno);
        bconfim =(Button)findViewById(R.id.buttonok);
        bconfim.setOnClickListener(onClickListener);
        scan1 = (Button)findViewById(R.id.scan1);
        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(taskfinish.this,CaptureActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type",1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

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

            if (owername.equals(""))
            {
                Toast.makeText(taskfinish.this, "请扫描雇主编号", Toast.LENGTH_SHORT).show();
                return;
            }


            Common.mainDB.updatetaskfinish(ContractNO,owername,OwnerConfirmStatus);

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




                owername  = Common.BaseDB.Getowername(owercode);
                if (owername.equals(""))
                {
                    Toast.makeText(taskfinish.this,"雇主信息不存在",Toast.LENGTH_SHORT).show();
                    return false;
                }

                setTitle( owername + "--雇主确认");


            }

            return false;
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        String code = data.getExtras().getString("code");


        scancode.setText(code);
        owername  = Common.BaseDB.Getowername(code);
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
