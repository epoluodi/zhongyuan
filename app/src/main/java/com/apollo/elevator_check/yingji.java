package com.apollo.elevator_check;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.Common.ScanData;


public class yingji extends Activity {


    TextView txt1;
    TextView txt2;
    TextView txt3;
    TextView txt4;
    TextView title;

    ScanData scandata = new ScanData();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yingji);
        txt1 = (TextView)findViewById(R.id.txt1);
        txt2 = (TextView)findViewById(R.id.txt2);
        txt3 = (TextView)findViewById(R.id.txt3);
        txt4 = (TextView)findViewById(R.id.txt4);
        title=(TextView)findViewById(R.id.title);
        title.setText("应急维修");

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
        scandata.ProjectType = "应急维修";
        scandata.ReachTime = bundle.getString("ReachTime");
        scandata.RegisterNO = bundle.getString("RegisterNO");
        scandata.TableType = bundle.getString("TableType");


    }

    Boolean isexit=false;
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {

        if (keyCode ==4) {
            setResult(0,null);
            isexit=true;
            finish();
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        if (keyCode ==4)
        {
            if (isexit)
                return true;
            if (txt1.getText().toString().equals("")
//                    ||
//                    txt2.getText().toString().equals("") ||
//                    txt3.getText().toString().equals("") ||
//                    txt4.getText().toString().equals("")
                    )
            {
                Toast.makeText(yingji.this, "请输入内容", Toast.LENGTH_SHORT).show();
                return true;
            }


            String iteminfo = txt1.getText().toString() + "|";
            iteminfo += txt2.getText().toString() + "|";
            iteminfo += txt3.getText().toString() + "|";
            iteminfo += txt4.getText().toString() ;


            scandata.ScanCode = "0000";
            scandata.ScanInfo = iteminfo;
            scandata.ScanTime = Common.GetSysOnlyTime();
            Common.mainDB.DelYJinfo(scandata.ContractNO,scandata.LiftNO);
            Common.mainDB.Addprojectinfo(scandata);


            setResult(0,null);
            finish();
        }
        return true;

    }

}
