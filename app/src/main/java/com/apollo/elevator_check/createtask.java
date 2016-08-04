package com.apollo.elevator_check;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;


public class createtask extends Activity {

    ArrayAdapter<String> adapter;
    Spinner spinner;
    List<String> Alltype = new ArrayList<String>();


    Button buttondo;
    Button buttonclear;
    Button buttonconfim;

    ListView listView;
    List<Map<String,String>> mapList;
    MyAdapter myAdapter;

    EditText projedtno;
    EditText liftno;
    Button scan1;
    Button scan2;

    String projectcode="";
    String liftNo="";

    TextView title;

    Map<String,String> selectmap=null;
    AlertDialog alertDialog;


    String tableindex ="1";
    String ProjectType = "";

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
//            barcodeStr = intent.getExtras().getString("data");
            barcodeStr = intent.getStringExtra("value");
            if (projedtno.isFocused())
            {
                projedtno.setText(barcodeStr);
                onKeyListenerprojectno.onKey(projedtno, 0, new KeyEvent(KeyEvent.ACTION_UP, 66));
                liftno.requestFocus();

                return;
            }
            if (liftno.isFocused())
            {
                liftno.setText(barcodeStr);
                onKeyListenerliftno.onKey(liftno,0,new KeyEvent(KeyEvent.ACTION_UP,66));
                return;
            }
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
        setContentView(R.layout.activity_createtask);


        listView = (ListView)findViewById(R.id.list1);
        listView.setOnItemClickListener(onItemClickListener);
        spinner = (Spinner) findViewById(R.id.dttype);
        projedtno = (EditText)findViewById(R.id.textprojectno);
        liftno = (EditText)findViewById(R.id.textdiantinumber);
        projedtno.setOnKeyListener(onKeyListenerprojectno);
        projedtno.setOnEditorActionListener(onEditorActionListenerprojectno);
        liftno.setOnKeyListener(onKeyListenerliftno);

        buttondo=(Button)findViewById(R.id.buttondo);
        buttondo.setOnClickListener(onClickListenerbuttondo);
        buttonclear=(Button)findViewById(R.id.buttonclear);
        buttonclear.setOnClickListener(onClickListenerbuttonclear);
        buttonconfim=(Button)findViewById(R.id.buttonconfim);
        buttonconfim.setOnClickListener(onClickListenerbuttonconfim);

        title=(TextView)findViewById(R.id.title);
        scan1 = (Button)findViewById(R.id.scan1);
        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(createtask.this,CaptureActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type",1);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);


            }
        });
        scan2 = (Button)findViewById(R.id.scan2);
        scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(createtask.this,CaptureActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("type",2);
                intent.putExtras(bundle);
                startActivityForResult(intent, 0);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

            }
        });
        String strtitle = String.format("工号：%1$s(%2$s)",Common.work1,Common.work1name);
        title.setText(strtitle);
//        Alltype.add("请选择保养类型");
//        Alltype.add("半月保养");
//        Alltype.add("季度保养");
//        Alltype.add("半年保养");
//        Alltype.add("全年保养");
//        Alltype.add("应急维修");
        Time time = new Time("GMT+8");
        time.setToNow();
//        Calendar calendar ;
        int day = time.monthDay+1;
        int month =time.month+1;
        switch (month)
        {
            case 1:
            case 2:
            case 4:
            case 5:
            case 7:
            case 8:
            case 10:
            case 11:
                spinner.setSelection(1);
                ProjectType = "△（半月保养项目）";
                Alltype.add("半月保养");
                break;
            case 3:
            case 9:

                if (day <= 15)
                {
                    spinner.setSelection(1);
                    ProjectType = "△（半月保养项目）";
                    Alltype.add("半月保养");
                    break;
                }
                spinner.setSelection(2);
                ProjectType = "△+■（季度保养项目）";
                Alltype.add("季度保养");
                break;

            case 6:
                if (day <= 15)
                {
                    spinner.setSelection(1);
                    ProjectType = "△（半月保养项目）";
                    Alltype.add("半月保养");
                    break;
                }
                spinner.setSelection(3);
                ProjectType = "△+■+○（半年保养项目）";
                Alltype.add("半年保养");
                break;
            case 12:
                if (day <= 15)
                {
                    spinner.setSelection(1);
                    ProjectType = "△（半月保养项目）";
                    Alltype.add("半月保养");
                    break;
                }
                spinner.setSelection(4);
                ProjectType = "△+■+○+★（全年保养项目）";
                Alltype.add("全年保养");
                break;
        }
        Alltype.add("应急维修");
        adapter = new ArrayAdapter<String>(this,
                R.layout.type_list, Alltype);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(onItemSelectedListenertype);





        mapList = new ArrayList<Map<String, String>>();
        myAdapter = new MyAdapter(this);
        listView.setAdapter(myAdapter);

        if (!Common.projectcode.equals(""))
        {
            projedtno.setText(Common.projectcode);
            projectcode = Common.projectcode;
            mapList = Common.BaseDB.Getprojectinfo(projectcode);
            if (mapList == null)
            {
                Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                return;
            }
            if (mapList .size()==0)
            {
                Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                return;
            }
            MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
            myAdapter1.notifyDataSetChanged();
        }
    }


    View.OnClickListener onClickListenerbuttondo = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (selectmap ==null)
            {
                Toast.makeText(createtask.this,
                        "请选择一个项目",Toast.LENGTH_SHORT).show();
                return ;
            }

            if (selectmap.get("state").equals("3"))
            {
                Toast.makeText(createtask.this,
                        "该项目已经完成，不能再次执行",Toast.LENGTH_SHORT).show();
                return ;
            }

            Intent intent;
            Bundle bundle;

            intent =new Intent(createtask.this,ScanTask.class);
            bundle =new Bundle();
            bundle.putString("ProjectName",selectmap.get("ProjectName"));
            bundle.putString("ContractNO",selectmap.get("ContractNO"));
            bundle.putString("ProjectType",ProjectType );
            bundle.putString("LiftType",selectmap.get("LiftType") );
            bundle.putString("CheckDate",Common.GetSysTimeshort());
            bundle.putString("EffectiveDate",selectmap.get("EffectiveDate") );
            bundle.putString("ContractEffectiveDate",selectmap.get("ContractEffectiveDate") );
            bundle.putString("ReachTime",Common.GetSysOnlyTime() );
            bundle.putString("CheckPerson",Common.work1name + "," + Common.work2name );
            bundle.putString("LiftNO",selectmap.get("LiftNO") );
            bundle.putString("LiftNOTime",Common.GetSysOnlyTime() );
            bundle.putString("FactoryNo",selectmap.get("FactoryNo") );
            bundle.putString("RegisterNO",selectmap.get("RegisterNO") );
            bundle.putString("ProjectCode",selectmap.get("ProjectCode") );
            bundle.putString("LiftCode",selectmap.get("LiftCode") );
            bundle.putString("pxid",selectmap.get("pxid") );
            if (selectmap.get("LiftCode").substring(0,3).equals("L-A"))
                tableindex = "1";
            if (selectmap.get("LiftCode").substring(0,3).equals("L-D"))
                tableindex = "2";
            if (selectmap.get("LiftCode").substring(0,3).equals("L-B"))
                tableindex = "3";
            if (selectmap.get("LiftCode").substring(0,3).equals("L-C"))
                tableindex = "4";
            if (spinner.getSelectedItemPosition() ==spinner.getCount()-1)
                tableindex = "5";




            if (selectmap.get("state").equals("1")) {
                String tableindex2 = Common.mainDB.Gettabletype(
                        selectmap.get("ContractNO"),selectmap.get("LiftNO"));
                if (tableindex2.equals(""))
                    tableindex2=tableindex;
                else
                {
                    if (tableindex.equals("5") && !tableindex2.equals("5")) {
//                        AlertDialog.Builder builder=new AlertDialog.Builder(createtask.this);
//                        builder.setTitle("重要提示").setMessage("切换到应急保养后，将清除原来数据");
//                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//
//                                Common.mainDB.DelTaskinfo(selectmap.get("ContractNO"),selectmap.get("LiftNO"));
//                                mapList = Common.BaseDB.Getprojectinfo(projectcode);
//                                MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
//                                myAdapter1.notifyDataSetChanged();
//
//                                Common.mainDB.AddTask(selectmap.get("ContractNO"),selectmap.get("LiftNO"));
//                                Intent intent =new Intent(createtask.this,yingji.class);
//                                Bundle bundle=new Bundle();
//                                bundle.putString("TableType","5" );
//                                intent.putExtras(bundle);
//                                startActivityForResult(intent,1);
//                                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//
//                            }
//                        });
//                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                spinner.setSelection(0);
//
//                            }
//                        });
//                        AlertDialog alertDialog=builder.create();
//                        alertDialog.show();


                        Common.mainDB.DelTaskinfo2(selectmap.get("ContractNO"), selectmap.get("LiftNO"));
                        mapList = Common.BaseDB.Getprojectinfo(projectcode);
                        MyAdapter myAdapter1 = (MyAdapter) listView.getAdapter();
                        myAdapter1.notifyDataSetChanged();

                        Common.mainDB.AddTask(selectmap.get("ContractNO"), selectmap.get("LiftNO"));
                        intent = new Intent(createtask.this, yingji.class);
                        bundle = new Bundle();
                        bundle.putString("TableType", "5");
                        intent.putExtras(bundle);
                        startActivityForResult(intent, 1);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        return;
                    }

                }
                bundle.putString("TableType",tableindex2 );
                if (tableindex2.equals("5") )
                    intent =new Intent(createtask.this,yingji.class);
            }
            else {
//                if (spinner.getSelectedItemPosition() ==0)
//                {
//                    Toast.makeText(createtask.this,
//                            "请选择保养内容",Toast.LENGTH_SHORT).show();
//                    return ;
//                }

                Common.mainDB.AddTask(selectmap.get("ContractNO"),selectmap.get("LiftNO"));
                bundle.putString("TableType", tableindex);
                if (tableindex.equals("5") )
                    intent =new Intent(createtask.this,yingji.class);
            }

            intent.putExtras(bundle);
            startActivityForResult(intent,1);


            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

            return ;
        }
    };

    View.OnClickListener onClickListenerbuttonclear = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (selectmap ==null)
            {
                Toast.makeText(createtask.this,
                        "请选择一个项目",Toast.LENGTH_SHORT).show();
                return ;
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(createtask.this);
            builder.setTitle("提示");
            builder.setCancelable(true);
            String note =String.format("项目名称：%1$s 电梯编号：%2$s 是否需要清除"
                    ,selectmap.get("ProjectName"),selectmap.get("LiftNO"));
            builder.setMessage(note);
            builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Common.mainDB.DelTaskinfo(selectmap.get("ContractNO"),selectmap.get("LiftNO"));
                    alertDialog.dismiss();
                    mapList = Common.BaseDB.Getprojectinfo(projectcode);
                    MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
                    myAdapter1.notifyDataSetChanged();

                }
            });
            builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    alertDialog.dismiss();
                }
            });

            alertDialog = builder.create();
            alertDialog.show();
        }
    };

    View.OnClickListener onClickListenerbuttonconfim = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (selectmap ==null)
            {
                Toast.makeText(createtask.this,
                        "没有项目",Toast.LENGTH_SHORT).show();
                return ;
            }
//                if (!selectmap.get("state"). equals("1")) {
//                    Toast.makeText(createtask.this,
//                            "请选择执行中的项目",Toast.LENGTH_SHORT).show();
//                    return true;
//                }

            Intent intent;
            Bundle bundle;
            intent =new Intent(createtask.this,taskfinish.class);
            bundle =new Bundle();

            bundle.putString("ContractNO",selectmap.get("ContractNO"));
            bundle.putString("pxid",selectmap.get("pxid"));
            bundle.putString("LiftNO",selectmap.get("LiftNO"));
            bundle.putString("khmc",selectmap.get("ProjectName"));
//                bundle.putString("TableType",Common.mainDB.Gettabletype(
//                selectmap.get("ContractNO"),selectmap.get("LiftNO")));
            String ContractNO = selectmap.get("ContractNO").replace("合同编号:", "");
            String LiftNO = selectmap.get("LiftNO").replace("电梯编号:", "");

            List<Map<String, String>> mapList1;
            mapList1 = Common.mainDB.GetTaskDetailinfo(ContractNO, LiftNO);
            if (mapList1.size() == 0)
            {
                Toast.makeText(createtask.this,"没有保养记录，不能确认任务，或者请清除数据",Toast.LENGTH_SHORT).show();
                return;
            }

            if (!CheckWork(mapList1.get(0).get("ProjectType"),
                    mapList1.get(0).get("TableType"),mapList1.size()))
            {
                Toast.makeText(createtask.this,"没有完成所有保养内容，不能完成任务",Toast.LENGTH_SHORT).show();
                return;
            }

            intent.putExtras(bundle);
            startActivityForResult(intent,1);
            overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);

        }
    };

    Boolean CheckWork(String projecttype,String tabletype,int count)
    {
       if (tabletype.equals("1"))
       {

           if (projecttype.equals("△（半月保养项目）"))
           {
               if (count <27 )
                   return false;
           }
           if (projecttype.equals("△+■（季度保养项目）"))
           {
               if (count <39 )
                   return false;
           }
           if (projecttype.equals("△+■+○（半年保养项目）"))
           {
               if (count <51 )
                   return false;
           }
           if (projecttype.equals("△+■+○+★（全年保养项目）"))
           {
               if (count <66 )
                   return false;
           }
           return true;
       }
        if (tabletype.equals("3"))
        {

            if (projecttype.equals("△（半月保养项目）"))
            {
                if (count <33 )
                    return false;
            }
            if (projecttype.equals("△+■（季度保养项目）"))
            {
                if (count <39 )
                    return false;
            }
            if (projecttype.equals("△+■+○（半年保养项目）"))
            {
                if (count <49 )
                    return false;
            }
            if (projecttype.equals("△+■+○+★（全年保养项目）"))
            {
                if (count <63 )
                    return false;
            }
            return true;
        }
        if (tabletype.equals("2"))
        {

            if (projecttype.equals("△（半月保养项目）"))
            {
                if (count <18 )
                    return false;
            }
            if (projecttype.equals("△+■（季度保养项目）"))
            {
                if (count <27 )
                    return false;
            }
            if (projecttype.equals("△+■+○（半年保养项目）"))
            {
                if (count <36 )
                    return false;
            }
            if (projecttype.equals("△+■+○+★（全年保养项目）"))
            {
                if (count <51 )
                    return false;
            }
            return true;
        }

        if (tabletype.equals("4"))
        {

            if (projecttype.equals("△（半月保养项目）"))
            {
                if (count <30 )
                    return false;
            }
            if (projecttype.equals("△+■（季度保养项目）"))
            {
                if (count <42 )
                    return false;
            }
            if (projecttype.equals("△+■+○（半年保养项目）"))
            {
                if (count <51 )
                    return false;
            }
            if (projecttype.equals("△+■+○+★（全年保养项目）"))
            {
                if (count <65 )
                    return false;
            }
            return true;
        }

        return true;
    }



    AdapterView.OnItemClickListener onItemClickListener =new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            selectmap = mapList.get(i);

        }
    };


    TextView.OnEditorActionListener onEditorActionListenerprojectno =new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
            if (i == EditorInfo.IME_ACTION_DONE)
            {
                projectcode  = projedtno.getText().toString();
                if (projectcode.equals(""))
                {
                    Toast.makeText(createtask.this,"请扫描合同编号",Toast.LENGTH_SHORT).show();
                    return false;
                }
                mapList.clear();
                liftNo="";
                mapList = Common.BaseDB.Getprojectinfo(projectcode);
                if (mapList ==null)
                {
                    Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (mapList .size()==0)
                {
                    Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                    return false;
                }
                Common.projectcode = projectcode;

                MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
                myAdapter1.notifyDataSetChanged();
            }


            return false;
        }
    };
    View.OnKeyListener onKeyListenerliftno = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if (keyEvent.getKeyCode()==66 && keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                liftNo = liftno.getText().toString();
                if (liftNo.equals(""))
                {
                    Toast.makeText(createtask.this,"请扫描电梯编号",Toast.LENGTH_SHORT).show();
                    return false;
                }

                if (mapList == null)
                {
                    Toast.makeText(createtask.this,"请先扫描项目",Toast.LENGTH_SHORT).show();
                    return false;
                }

                for (int ii= 0;ii < mapList.size();ii++)
                {
                    if (mapList.get(ii).get("LiftCode").toString().equals(liftNo))
                    {
                        listView.setSelection(ii);
                        listView.smoothScrollToPosition(ii);
                        MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
                        myAdapter1.notifyDataSetChanged();
                        return false;
                    }


                }


            }

            return false;
        }
    };


    View.OnKeyListener onKeyListenerprojectno = new View.OnKeyListener() {
        @Override
        public boolean onKey(View view, int i, KeyEvent keyEvent) {

            if (keyEvent.getKeyCode()==66 && keyEvent.getAction() == KeyEvent.ACTION_UP)
            {
                projectcode  = projedtno.getText().toString();
                if (projectcode.equals(""))
                {
                    Toast.makeText(createtask.this,"请扫描合同编号",Toast.LENGTH_SHORT).show();
                    return false;
                }
                mapList.clear();
                liftNo="";
                mapList = Common.BaseDB.Getprojectinfo(projectcode);
                if (mapList ==null)
                {
                    Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (mapList .size()==0)
                {
                    Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                    return false;
                }
                Common.projectcode = projectcode;

                MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
                myAdapter1.notifyDataSetChanged();
            }

            return false;
        }
    };

    AdapterView.OnItemSelectedListener onItemSelectedListenertype = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i)
            {
                case 1:
                    ProjectType = "△（半月保养项目）";
                    break;
                case 2:
                    ProjectType = "△+■（季度保养项目）";
                    break;
                case 3:
                    ProjectType = "△+■+○（半年保养项目）";
                    break;
                case 4:
                    ProjectType = "△+■+○+★（全年保养项目）";
                    break;
                case 5:
                    ProjectType = "应急维修";
                    tableindex="5";
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
////        getMenuInflater().inflate(R.menu.createtask, menu);
//        return false;
//    }









    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1) {
            mapList = Common.BaseDB.Getprojectinfo(projectcode);
            MyAdapter myAdapter1 = (MyAdapter) listView.getAdapter();
            myAdapter1.notifyDataSetChanged();

        }

        if (requestCode==0)
        {
            if (data == null)
                return;
            String code = data.getExtras().getString("code");
            int type = data.getExtras().getInt("type");
            if (type ==1) {
                projedtno.setText(code);
                projectcode  = projedtno.getText().toString();

                mapList.clear();
                liftNo="";
                mapList = Common.BaseDB.Getprojectinfo(projectcode);
                if (mapList == null)
                {
                    Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mapList .size()==0)
                {
                    Toast.makeText(createtask.this,"没有找到项目",Toast.LENGTH_SHORT).show();
                    return;
                }



                Common.projectcode = projectcode;
                MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
                myAdapter1.notifyDataSetChanged();
            }
            if (type ==2) {
                liftno.setText(code);
                liftNo = liftno.getText().toString();

                MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
                myAdapter1.notifyDataSetChanged();

            }


        }




    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        Intent intent;
//        Bundle bundle;
//        switch (id)
//        {
//            case R.id.dotask:
//
//                if (selectmap ==null)
//                {
//                    Toast.makeText(createtask.this,
//                            "请选择一个项目",Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//
//                if (selectmap.get("state").equals("3"))
//                {
//                    Toast.makeText(createtask.this,
//                            "该项目已经完成，不能再次执行",Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//
//
//
//                intent =new Intent(createtask.this,ScanTask.class);
//                bundle =new Bundle();
//                bundle.putString("ProjectName",selectmap.get("ProjectName"));
//                bundle.putString("ContractNO",selectmap.get("ContractNO"));
//                bundle.putString("ProjectType",ProjectType );
//                bundle.putString("LiftType",selectmap.get("LiftType") );
//                bundle.putString("CheckDate",Common.GetSysTimeshort());
//                bundle.putString("EffectiveDate",selectmap.get("EffectiveDate") );
//                bundle.putString("ContractEffectiveDate",selectmap.get("ContractEffectiveDate") );
//                bundle.putString("ReachTime",Common.GetSysOnlyTime() );
//                bundle.putString("CheckPerson",Common.work1name + "," + Common.work2name );
//                bundle.putString("LiftNO",selectmap.get("LiftNO") );
//                bundle.putString("LiftNOTime",Common.GetSysOnlyTime() );
//                bundle.putString("FactoryNo",selectmap.get("FactoryNo") );
//                bundle.putString("RegisterNO",selectmap.get("RegisterNO") );
//                bundle.putString("ProjectCode",selectmap.get("ProjectCode") );
//                bundle.putString("LiftCode",selectmap.get("LiftCode") );
//
//                if (selectmap.get("LiftCode").substring(0,3).equals("L-A"))
//                    tableindex = "1";
//                if (selectmap.get("LiftCode").substring(0,3).equals("L-D"))
//                    tableindex = "2";
//                if (selectmap.get("LiftCode").substring(0,3).equals("L-B"))
//                    tableindex = "3";
//                if (selectmap.get("LiftCode").substring(0,3).equals("L-C"))
//                    tableindex = "4";
//                if (spinner.getSelectedItemPosition() ==spinner.getCount()-1)
//                    tableindex = "5";
//
//                if (selectmap.get("state").equals("1")) {
//                    String tableindex2 = Common.mainDB.Gettabletype(
//                            selectmap.get("ContractNO"),selectmap.get("LiftNO"));
//                    bundle.putString("TableType",tableindex2 );
//                    if (tableindex2.equals("5") )
//                        intent =new Intent(createtask.this,yingji.class);
//                }
//                else {
//                    if (spinner.getSelectedItemPosition() ==0)
//                    {
//                        Toast.makeText(createtask.this,
//                                "请选择保养内容",Toast.LENGTH_SHORT).show();
//                        return true;
//                    }
//
//                    Common.mainDB.AddTask(selectmap.get("ContractNO"),selectmap.get("LiftNO"));
//                    bundle.putString("TableType", tableindex);
//                    if (tableindex.equals("5") )
//                        intent =new Intent(createtask.this,yingji.class);
//                }
//
//                intent.putExtras(bundle);
//                startActivityForResult(intent,1);
//
//
//                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//
//                return true;
//            case R.id.cleardata:
//                if (selectmap ==null)
//                {
//                    Toast.makeText(createtask.this,
//                            "请选择一个项目",Toast.LENGTH_SHORT).show();
//                    return true;
//                }
//
//                AlertDialog.Builder builder = new AlertDialog.Builder(createtask.this);
//                builder.setTitle("提示");
//                builder.setCancelable(true);
//                String note =String.format("项目名称：%1$s 电梯编号：%2$s 是否需要清除"
//                        ,selectmap.get("ProjectName"),selectmap.get("LiftNO"));
//                builder.setMessage(note);
//                builder.setPositiveButton("确认",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        Common.mainDB.DelTaskinfo(selectmap.get("ContractNO"),selectmap.get("LiftNO"));
//                        alertDialog.dismiss();
//                        mapList = Common.BaseDB.Getprojectinfo(projectcode);
//                        MyAdapter myAdapter1 = (MyAdapter)listView.getAdapter();
//                        myAdapter1.notifyDataSetChanged();
//
//                    }
//                });
//                builder.setNegativeButton("取消",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        alertDialog.dismiss();
//                    }
//                });
//
//                alertDialog = builder.create();
//                alertDialog.show();
//                return true;
//            case R.id.confimtask:
//                if (selectmap ==null)
//                {
//                    Toast.makeText(createtask.this,
//                            "没有项目",Toast.LENGTH_SHORT).show();
//                    return true;
//                }
////                if (!selectmap.get("state"). equals("1")) {
////                    Toast.makeText(createtask.this,
////                            "请选择执行中的项目",Toast.LENGTH_SHORT).show();
////                    return true;
////                }
//
//
//                intent =new Intent(createtask.this,taskfinish.class);
//                bundle =new Bundle();
//
//                bundle.putString("ContractNO",selectmap.get("ContractNO"));
////                bundle.putString("LiftNO",selectmap.get("LiftNO") );
////                bundle.putString("TableType",Common.mainDB.Gettabletype(
////                selectmap.get("ContractNO"),selectmap.get("LiftNO")));
//
//                intent.putExtras(bundle);
//                startActivityForResult(intent,1);
//
//                overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
//
//                return true;
//        }
//
//
//        return super.onOptionsItemSelected(item);
//    }


    class MyAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        TextView projectname;
        TextView projectno;
        TextView liftno;
        TextView state;

        public MyAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub

            return mapList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return mapList.get(arg0);

        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = mInflater.inflate(R.layout.list_createtask, null);

            projectname = (TextView) view.findViewById(R.id.projectname);
            projectno = (TextView) view.findViewById(R.id.projectno);
            liftno = (TextView) view.findViewById(R.id.LiftNO);
            state = (TextView) view.findViewById(R.id.state);

            projectname.setText(mapList.get(i).get("ProjectName"));
            projectno.setText("合同编号：" + mapList.get(i).get("ContractNO"));
            liftno.setText("电梯编号：" +mapList.get(i).get("LiftNO"));

            if (mapList.get(i).get("state").equals("0"))
                state.setText("任务状态：未检查");
            if (mapList.get(i).get("state").equals("1"))
                state.setText("任务状态：未确认");
            if (mapList.get(i).get("state").equals("3"))
                state.setText("任务状态：未发送");

            if (!liftNo.equals("") && liftNo.equals(mapList.get(i).get("LiftCode")))
            {

                projectname.setTextColor(getResources().getColor(R.color.red1));
                liftno.setTextColor(getResources().getColor(R.color.firebrick));
                projectno.setTextColor(getResources().getColor(R.color.firebrick));
                state.setTextColor(getResources().getColor(R.color.firebrick));
                listView.smoothScrollToPosition(i);
                listView.setSelection(i);
                selectmap = mapList.get(i);
            }
            else
            {
                projectname.setTextColor(getResources().getColor(R.color.blue));
                liftno.setTextColor(getResources().getColor(R.color.mediumslateblue));
                projectno.setTextColor(getResources().getColor(R.color.mediumslateblue));
                state.setTextColor(getResources().getColor(R.color.mediumslateblue));

            }


            return view;
        }
    }
}
