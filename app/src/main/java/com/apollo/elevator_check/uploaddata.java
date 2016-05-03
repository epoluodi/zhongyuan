package com.apollo.elevator_check;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.WebService.WebThreadDo;
import com.apollo.elevator_check.WebService.Webservice;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.PropertyInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;


public class uploaddata extends Activity {
    ListView listView1;
    Button uploaddatabutton;
    Button uploadphotodatabutton;

    List<Map<String, String>> mapList;
    SimpleAdapter simpleAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uploaddata);
        listView1 = (ListView) findViewById(R.id.list1);
        uploaddatabutton = (Button) findViewById(R.id.upload);

        uploaddatabutton.setOnClickListener(onClickListener);

        uploadphotodatabutton = (Button)findViewById(R.id.uploadphoto);
        uploadphotodatabutton.setOnClickListener(onClickListeneruploadphoto);
        mapList = new ArrayList<Map<String, String>>();

        mapList = Common.mainDB.GetuploadData();
        simpleAdapter = new SimpleAdapter(this, mapList, R.layout.list_uploaddata,
                new String[]{"ProjectName", "ContractNO", "LiftNO", "counts"},
                new int[]{R.id.projectname, R.id.projectno, R.id.LiftNO, R.id.counts});
        listView1.setAdapter(simpleAdapter);
    }




    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case 3:
                    Toast.makeText(uploaddata.this, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                Toast.makeText(uploaddata.this, "上传失败", Toast.LENGTH_SHORT).show();
                Common.CLosePopwindow();
                break;
                case 1:
                    Toast.makeText(uploaddata.this, "上传成功", Toast.LENGTH_SHORT).show();
                    Common.CLosePopwindow();
                    mapList = Common.mainDB.GetuploadData();
                    simpleAdapter = new SimpleAdapter(uploaddata.this, mapList, R.layout.list_uploaddata,
                            new String[]{"ProjectName", "ContractNO", "LiftNO", "counts"},
                            new int[]{R.id.projectname, R.id.projectno, R.id.LiftNO, R.id.counts});
                    listView1.setAdapter(simpleAdapter);
                    mapList.clear();
                    File file = new File(Environment.getExternalStorageDirectory(),"zhongyuan");
                    File[] files= file.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            if (file.getName().contains("png"))
                                return true;
                            return false;
                        }
                    });

                    for (File file1 :files)
                    {
                        file1.delete();
                    }

                    break;

            }

        }
    };





    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (mapList.size() == 0) {
                Toast.makeText(uploaddata.this, "没有记录", Toast.LENGTH_SHORT).show();
                return;
            }




            Common.ShowPopWindow(listView1, getLayoutInflater(), "上传数据");


            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map;
                        Map<String, String> map2;
                        List<Map<String, String>> mapList1;
                        String ContractNO;
                        String LiftNO;
                        PropertyInfo[] propertyInfos;
                        PropertyInfo propertyInfo;
                        String signfile="",pxid="";
                        String pname="";

                        for (int i = 0; i < mapList.size(); i++) {
                            map = mapList.get(i);
                            ContractNO = map.get("ContractNO").replace("合同编号:", "");
                            LiftNO = map.get("LiftNO").replace("电梯编号:", "");
                            mapList1 = Common.mainDB.GetTaskDetailinfo(ContractNO, LiftNO);


                            for (int ii = 0; ii < mapList1.size(); ii++) {
                                map2 = mapList1.get(ii);

                                propertyInfos = new PropertyInfo[23];
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ProjectName");
                                propertyInfo.setValue(map2.get("ProjectName"));
                                pname=map2.get("ProjectName");
                                propertyInfos[0] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ContractNO");
                                propertyInfo.setValue(map2.get("ContractNO"));
                                propertyInfos[1] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ProjectType");
                                propertyInfo.setValue(map2.get("ProjectType"));
                                propertyInfos[2] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("LiftType");
                                propertyInfo.setValue(map2.get("LiftType"));
                                propertyInfos[3] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("CheckDate");
                                propertyInfo.setValue(map2.get("CheckDate"));
                                propertyInfos[4] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("EffectiveDate");
                                propertyInfo.setValue(map2.get("EffectiveDate"));
                                propertyInfos[5] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ContractEffectiveDate");
                                propertyInfo.setValue(map2.get("ContractEffectiveDate"));
                                propertyInfos[6] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ReachTime");
                                propertyInfo.setValue(map2.get("ReachTime"));
                                propertyInfos[7] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("LeaveTime");
                                propertyInfo.setValue(map2.get("LeaveTime"));
                                propertyInfos[8] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("CheckPerson");
                                propertyInfo.setValue(map2.get("CheckPerson"));
                                propertyInfos[9] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("LiftNO");
                                propertyInfo.setValue(map2.get("LiftNO"));
                                propertyInfos[10] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("LiftNOTime");
                                propertyInfo.setValue(map2.get("LiftNOTime"));
                                propertyInfos[11] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("FactoryNo");
                                propertyInfo.setValue(map2.get("FactoryNo"));
                                propertyInfos[12] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("RegisterNO");
                                propertyInfo.setValue(map2.get("RegisterNO"));
                                propertyInfos[13] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("OwnerName");
                                propertyInfo.setValue(map2.get("OwnerName"));
                                signfile=map2.get("OwnerName");
                                propertyInfos[14] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("OwnerConfirmStatus");
                                propertyInfo.setValue(map2.get("OwnerConfirmStatus"));
                                propertyInfos[15] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("OwnerConfirmTime");
                                propertyInfo.setValue(map2.get("OwnerConfirmTime"));
                                propertyInfos[16] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ScanCode");
                                propertyInfo.setValue(map2.get("ScanCode"));
                                propertyInfos[17] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ScanInfo");
                                propertyInfo.setValue(map2.get("ScanInfo"));
                                propertyInfos[18] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("ScanTime");
                                propertyInfo.setValue(map2.get("ScanTime"));
                                propertyInfos[19] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("TableType");
                                propertyInfo.setValue(map2.get("TableType"));
                                propertyInfos[20] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("pxid");
                                propertyInfo.setValue(map2.get("pxid"));
                                pxid=map2.get("pxid");
                                propertyInfos[21] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("state");
                                propertyInfo.setValue(map2.get("state"));
                                propertyInfos[22] = propertyInfo;

                                Webservice webservice = new Webservice(Common.ServerWCF,10000);
                                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"PDA_submit3");
                                if (r.equals("0") || r.equals("-1")) {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }






                            }

                            if (signfile.contains("sign_"))
                            {
                                String base64img = "";
                                base64img  = imgToBase64(Environment.getExternalStorageDirectory().getAbsolutePath()
                                        + File.separator + "zhongyuan/"+signfile.replace("sign_","")+".png");
                                propertyInfos = new PropertyInfo[2];
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("pxid");
                                propertyInfo.setValue(pxid);
                                propertyInfos[0] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("imgbase64");
                                propertyInfo.setValue(base64img);
                                propertyInfos[1] = propertyInfo;

                                Webservice webservice = new Webservice(Common.ServerWCF,10000);
                                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_PDA_submitphotoForsign");
                                if (r.equals("-1"))
                                {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }
                            }
                            Common.mainDB.DelTaskinfo(ContractNO,LiftNO);

                            Message message=handler.obtainMessage();
                            message.what=3;
                            message.obj=String.format("(%1$s)%2$s 成功上传，服务已经接收到!!",pxid,pname);
                            handler.sendMessage(message);
                        }


                        handler.sendEmptyMessage(1);
                    }
                }).start();



            } catch (Exception e) {
                handler.sendEmptyMessage(0);

            }
        }
    };



    View.OnClickListener onClickListeneruploadphoto = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            mapList = Common.mainDB.Getphotoinfo();


            if (mapList.size() == 0) {
                Toast.makeText(uploaddata.this, "没有照片数据", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(uploaddata.this, "共有 " + String.valueOf(mapList.size())  + " 组数据", Toast.LENGTH_SHORT).show();



            Common.ShowPopWindow(listView1, getLayoutInflater(), "上传数据");


            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Map<String, String> map;
                        String base64img = "";


                        PropertyInfo[] propertyInfos;
                        PropertyInfo propertyInfo;
                        for (int i = 0; i < mapList.size(); i++) {
                            map = mapList.get(i);
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("projectcode", map.get("Projectcode"));
                                jsonObject.put("liftno", map.get("LiftNO"));
                                jsonObject.put("liftcode", map.get("Liftcode"));
                                jsonObject.put("projectname", map.get("Projectname"));
                                jsonObject.put("contractno", map.get("ContractNO"));
                                jsonObject.put("scancode", map.get("ScanCode"));
                                jsonObject.put("scanname", map.get("Scanname"));
                                base64img  = imgToBase64(map.get("img"));

                                propertyInfos = new PropertyInfo[2];
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("json");
                                propertyInfo.setValue(jsonObject.toString());
                                propertyInfos[0] = propertyInfo;
                                propertyInfo = new PropertyInfo();
                                propertyInfo.setName("imgbase64");
                                propertyInfo.setValue(base64img);
                                propertyInfos[1] = propertyInfo;


                                Webservice webservice = new Webservice(Common.ServerWCF,10000);
                                String r = webservice.PDA_GetInterFaceForStringNew(propertyInfos,"A_PDA_submitphoto");
                                if (r.equals("0") || r.equals("-1")) {
                                    handler.sendEmptyMessage(0);
                                    return;
                                }


                                File file= new File(map.get("img"));
                                file.delete();

                                Common.mainDB.Delphotoinfo(map.get("ContractNO"),map.get("LiftNO"),
                                        map.get("Projectcode") );
                            }
                            catch (JSONException jsex)
                            {

                            }



                        }
                        handler.sendEmptyMessage(1);
                    }
                }).start();



            } catch (Exception e) {
                handler.sendEmptyMessage(0);

            }
        }
    };




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


}
