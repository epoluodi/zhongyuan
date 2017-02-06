package com.apollo.elevator_check;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.elevator_check.Common.CameraHelper;
import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.Common.ScanData;
import com.apollo.elevator_check.WebService.Webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ScanTask extends Activity {

    ArrayAdapter<String> adapter;
    Spinner spinner;
    List<String> Alltype = new ArrayList<String>();

    ScanData scandata = new ScanData();

    TextView title;
    EditText scancode;
    TextView textViewinfo;
    TextView textViewtime;
    Button scan1;
    Button photo;
    Button remark;
    RadioButton checkOK,checkNO;
    String photofilename;
    private String scantype;

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
//            barcodeStr = intent.getExtras().getString ("data");




//            barcodeStr = intent.getExtras().getString ("data");
            barcodeStr = intent.getStringExtra("value");
            switch ((int)(Integer.valueOf( scandata.TableType)))
            {
                case 1:
                    if (!barcodeStr.contains("G-A")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 2:
                    if (!barcodeStr.contains("G-D")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 3:
                    if (!barcodeStr.contains("G-B")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 4:
                    if (!barcodeStr.contains("G-C")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;

            }


            scancode.setText(barcodeStr);
            onKeyListenerliftno.onKey(scancode,0,new KeyEvent(KeyEvent.ACTION_UP,66));
            scancode.requestFocus();

        }

    };


    private BroadcastReceiver mScanReceiverDD = new BroadcastReceiver() {

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
//            barcodeStr = intent.getExtras().getString ("data");




            barcodeStr = intent.getExtras().getString ("data");
//            barcodeStr = intent.getStringExtra("value");
            switch ((int)(Integer.valueOf( scandata.TableType)))
            {
                case 1:
                    if (!barcodeStr.contains("G-A")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 2:
                    if (!barcodeStr.contains("G-D")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 3:
                    if (!barcodeStr.contains("G-B")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;
                case 4:
                    if (!barcodeStr.contains("G-C")) {
                        Toast.makeText(ScanTask.this,"条码不属于概项目",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;

            }


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
        unregisterReceiver(mScanReceiverDD);
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
//        initScan();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Common.SCAN_ACTION);
        registerReceiver(mScanReceiver, filter);

        IntentFilter filter1 = new IntentFilter();
        filter1.addAction(Common.SCAN_ACTION_DD);
        registerReceiver(mScanReceiverDD, filter1);

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_task);
        Bundle bundle = getIntent().getExtras();
        scandata.CheckDate = bundle.getString("CheckDate");
        scandata.CheckPerson = bundle.getString("CheckPerson");
        scandata.ContractEffectiveDate = bundle.getString("ContractEffectiveDate");
        scandata.ContractNO = bundle.getString("ContractNO");
        scandata.EffectiveDate = bundle.getString("EffectiveDate");
        scandata.FactoryNo = bundle.getString("FactoryNo");
        scandata.LiftNO = bundle.getString("LiftNO");
        scandata.LiftNOTime = bundle.getString("LiftNOTime");
        scandata.ProjectName = bundle.getString("ProjectName");
        scandata.LiftType = bundle.getString("LiftType");
        scandata.ProjectType = bundle.getString("ProjectType");
        scandata.ReachTime = bundle.getString("ReachTime");
        scandata.RegisterNO = bundle.getString("RegisterNO");
        scandata.TableType = bundle.getString("TableType");
        scandata.ProjectCode = bundle.getString("ProjectCode");
        scandata.LiftCode = bundle.getString("LiftCode");
        scandata.pxid = bundle.getString("pxid");

        remark = (Button)findViewById(R.id.remark);
        remark.setOnClickListener(onClickListenerrenmark);
        photo = (Button)findViewById(R.id.photo);
        photo.setOnClickListener(onClickListenerphoto);
        scan1 = (Button)findViewById(R.id.scan1);
        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ScanTask.this,CaptureActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type",1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });
        scancode =(EditText)findViewById(R.id.scancode);
        scancode.setOnKeyListener(onKeyListenerliftno);
        textViewinfo = (TextView)findViewById(R.id.scaninfo);
        textViewtime=(TextView)findViewById(R.id.scantime);
        title=(TextView)findViewById(R.id.title);
        title.setText("电梯编号:" + scandata.LiftNO );

        checkOK = (RadioButton)findViewById(R.id.checkOK);
        checkNO = (RadioButton)findViewById(R.id.checkNO);
        checkOK.setOnCheckedChangeListener(onCheckedChangeListener);


    }


    View.OnClickListener onClickListenerrenmark = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent=new Intent(ScanTask.this,RemarkActivity.class);
            intent.putExtra("pxid",scandata.pxid);
            intent.putExtra("liftno",scandata.LiftNO);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        }
    };



    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b)
                scandata.state = 0;
            else
                scandata.state = 1;
            Common.mainDB.updateprojectinfostate(scandata);

        }
    };
    View.OnClickListener onClickListenerphoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            photofilename =  CameraHelper.takePhoto(ScanTask.this,0);
//            Toast.makeText(ScanTask.this,photofilename,Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==100) {
            if (resultCode == -1) {

                try {
                    Common.mainDB.Addprojectinfophoto(scandata, photofilename);


//                    new Thread(new Runnable() {
//                        @Override
//                        public void run() {
//                            Map<String, String> map;
//                            String base64img = "";
//
//
//                            PropertyInfo[] propertyInfos;
//                            PropertyInfo propertyInfo;
//
//                            JSONObject jsonObject = new JSONObject();
//                            try {
//                                jsonObject.put("projectcode", scandata.ProjectCode);
//                                jsonObject.put("liftno", scandata.LiftNO);
//                                jsonObject.put("liftcode", scandata.LiftCode);
//                                jsonObject.put("projectname", scandata.ProjectName);
//                                jsonObject.put("contractno", scandata.ContractNO);
//                                jsonObject.put("scancode", "");
//                                jsonObject.put("scanname", "");
//                                base64img = imgToBase64(photofilename);
//
//                                propertyInfos = new PropertyInfo[2];
//                                propertyInfo = new PropertyInfo();
//                                propertyInfo.setName("json");
//                                propertyInfo.setValue(jsonObject.toString());
//                                propertyInfos[0] = propertyInfo;
//                                propertyInfo = new PropertyInfo();
//                                propertyInfo.setName("imgbase64");
//                                propertyInfo.setValue(base64img);
//                                propertyInfos[1] = propertyInfo;
//
//
//                                Webservice webservice = new Webservice(Common.ServerWCF, 10000);
//                                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos, "A_PDA_submitphoto");
//                                if (r.equals("0") || r.equals("-1")) {
//                                    Common.mainDB.Addprojectinfophoto(scandata, photofilename);
//
//                                    return;
//                                }
//
//
//                                File file = new File(photofilename);
//                                file.delete();
//
//
//                            } catch (JSONException jsex) {
//
//                            }
//
//
//                        }
//                    }).start();


                } catch (Exception e) {


                }


            }
            return;
        };
        if (data == null)
            return;
        String code = data.getExtras().getString("code");


        scancode.setText(code);
        String iteminfo  = Common.BaseDB.GetscanCodeinfo(code,scandata.TableType,scandata.ProjectType);
        if (iteminfo.equals(""))
        {
            Toast.makeText(ScanTask.this,"无相关记录",Toast.LENGTH_SHORT).show();
            return;
        }
        if (Common.mainDB.IsScanned(scandata.ContractNO,scandata.LiftNO, code))
        {
            Toast.makeText(ScanTask.this,"该项已巡检",Toast.LENGTH_SHORT).show();
            return ;
        }

        textViewinfo.setText("内容：" + iteminfo);
        textViewtime.setText("扫描时间：" + Common.GetSysOnlyTime());
        scandata.ScanCode = code;
        scandata.ScanInfo = iteminfo;
        scandata.ScanTime = Common.GetSysOnlyTime();
        scandata.state=0;
        Common.mainDB.Addprojectinfo(scandata);

    }



    String imgToBase64(String filename)
    {

        Bitmap bitmap=  Common.decodeBitmap(filename,4);
        try {



            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);

            byte[] buffer = baos.toByteArray();
            bitmap.recycle();
            return Base64.encodeToString(buffer, Base64.DEFAULT);

        }
        catch (Exception e)
        {e.printStackTrace();
            return "";}


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



    View.OnKeyListener onKeyListenerliftno = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {
            String strscancode = scancode.getText().toString();

            if (keyEvent.getKeyCode()==66 && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                if (strscancode.equals("")) {
                    Toast.makeText(ScanTask.this, "请扫描巡检编号", Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (Common.mainDB.IsScanned(scandata.ContractNO, scandata.LiftNO, strscancode)) {
                    Toast.makeText(ScanTask.this, "该项已巡检", Toast.LENGTH_SHORT).show();
                    return false;
                }


                String iteminfo = Common.BaseDB.GetscanCodeinfo(strscancode, scandata.TableType,scandata.ProjectType);
                if (iteminfo.equals("")) {
                    Toast.makeText(ScanTask.this, "无相关记录", Toast.LENGTH_SHORT).show();
                    return false;
                }
                textViewinfo.setText("内容：" + iteminfo);
                textViewtime.setText("扫描时间：" + Common.GetSysOnlyTime());
                scandata.ScanCode = strscancode;
                scandata.ScanInfo = iteminfo;
                scandata.ScanTime = Common.GetSysOnlyTime();


                Common.mainDB.Addprojectinfo(scandata);

            }

            return false;
        }
    };

}
