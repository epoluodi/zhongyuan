package com.apollo.elevator_check.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.Common.ScanData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 14-6-5.
 */
public class DBManager {

    public static String DBPath;

    public String userdbpath="";
    public SQLiteDatabase db;
    private Context context;
    SQLiteOpenHelper sqLiteOpenHelper;


    public DBManager(Context context, String userDbpath) {
        sqLiteOpenHelper = new SQLiteOpenHelper(context,userDbpath,null,1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

            }
        };

        userdbpath= userDbpath;
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        this.context = context;

        db  = sqLiteOpenHelper.getWritableDatabase();
    }




    public void closeDB() {
        if (db !=null)
            db.close();
    }


    public String Getworkname(String work)
    {
        String strvar="";
        try {
            Cursor cursor = db.rawQuery("select * from Employees_TABLE where Number= ? ", new String[]{work});
            if (cursor.moveToNext()) {
                strvar = cursor.getString(1);
            }
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strvar;
    }


    public String Gettabletype(String ContractNO,String LiftNO)
    {

        String strvar="";
        try {
            Cursor cursor = db.rawQuery("select tabletype from ProjectInfo_TABLE where ContractNO = ? and LiftNO = ? "
                    , new String[]{ContractNO,LiftNO});
            if (cursor.moveToNext()) {
                strvar = cursor.getString(0);
            }
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strvar;
    }


    public List<Map<String,String>> Getprojectinfo(String projectno)
    {
        List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
        Map<String,String> map;
        try {

            Cursor cursor = db.rawQuery("select * from ProjectInfo_TABLE where ProjectCode = ? ", new String[]{projectno});
            while (cursor.moveToNext()) {

                map = new HashMap<String, String>();

                map.put("ProjectName",cursor.getString(1));
                map.put("ContractNO",cursor.getString(2));
                map.put("LiftType",cursor.getString(3));
                map.put("ProjectCode",cursor.getString(4));
                map.put("EffectiveDate",cursor.getString(5));
                map.put("ContractEffectiveDate",cursor.getString(6));
                map.put("LiftNO",cursor.getString(7));
                map.put("LiftCode",cursor.getString(8));
                map.put("FactoryNo",cursor.getString(9));
                map.put("RegisterNO",cursor.getString(10));
                map.put("pxid",cursor.getString(11));
                map.put("state",Common.mainDB.Gettaskstate(cursor.getString(2),cursor.getString(7)));
                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mapList;
    }



    public List<Map<String,String>> GetTaskDetailinfo(String ContractNO,String LiftNO)
    {
        List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
        Map<String,String> map;
        try {

            Cursor cursor = db.rawQuery("select * from ProjectInfo_TABLE where ContractNO = ? and LiftNO = ?",
                    new String[]{ContractNO,LiftNO});
            while (cursor.moveToNext()) {

                map = new HashMap<String, String>();

                map.put("ProjectName",cursor.getString(1));
                map.put("ContractNO",cursor.getString(2));
                map.put("ProjectType",cursor.getString(3));
                map.put("LiftType",cursor.getString(4));
                map.put("CheckDate",cursor.getString(5));
                map.put("EffectiveDate",cursor.getString(6));
                map.put("ContractEffectiveDate",cursor.getString(7));
                map.put("ReachTime",cursor.getString(8));
                map.put("LeaveTime",cursor.getString(9));
                map.put("CheckPerson",cursor.getString(10));
                map.put("LiftNO",cursor.getString(11));
                map.put("LiftNOTime",cursor.getString(12));
                map.put("FactoryNo",cursor.getString(13));
                map.put("RegisterNO",cursor.getString(14));
                map.put("OwnerName",cursor.getString(15));
                map.put("OwnerConfirmStatus",cursor.getString(16));
                map.put("OwnerConfirmTime",cursor.getString(17));
                map.put("ScanCode",cursor.getString(18));
                map.put("ScanInfo",cursor.getString(19));
                map.put("ScanTime",cursor.getString(20));
                map.put("TableType",cursor.getString(21));
                map.put("state",String.valueOf( cursor.getInt(22)));
                map.put("pxid", cursor.getString(23));
                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mapList;
    }


    public List<Map<String,String>> Getphotoinfo()
    {
        List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
        Map<String,String> map;
        try {

            Cursor cursor = db.rawQuery("select * from photoinfo", null);
            while (cursor.moveToNext()) {

                map = new HashMap<String, String>();

                map.put("Projectcode",cursor.getString(0));
                map.put("LiftNO",cursor.getString(1));
                map.put("Liftcode",cursor.getString(2));
                map.put("Projectname",cursor.getString(3));
                map.put("ContractNO",cursor.getString(4));
                map.put("ScanCode",cursor.getString(5) == null ? "":cursor.getString(5));
                map.put("Scanname",cursor.getString(6) == null ? "":cursor.getString(6));
                map.put("dt",cursor.getString(7));
                map.put("img",cursor.getString(8));

                mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mapList;
    }


    public void Delphotoinfo(String ContractNO,String LiftNO,String projectcode)
    {

        try {

            db.delete("photoinfo", "ContractNO = ? and LiftNO = ? and Projectcode= ?",
                    new String[]{ContractNO, LiftNO,projectcode});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }




    public List<Map<String,String>> GetuploadData()
    {
        List<Map<String,String>> mapList = new ArrayList<Map<String, String>>();
        Map<String,String> map;
        try {

            Cursor cursor = db.rawQuery("select * from task where state = 3 ", null);
            while (cursor.moveToNext()) {

                map = new HashMap<String, String>();

                map.put("ProjectName",Common.BaseDB.Getprojectname(cursor.getString(0),
                        cursor.getString(1)));
                map.put("ContractNO","合同编号:" + cursor.getString(0));
                map.put("LiftNO","电梯编号:" + cursor.getString(1));
                map.put("counts","巡检编号数量:" + Getprojectcounts(cursor.getString(0),
                        cursor.getString(1)));


                 mapList.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return mapList;
    }




    public void AddTask(String ContractNO,String LiftNO)
    {

        try {
            ContentValues cv = new ContentValues();
            cv.put("ContractNO",ContractNO);
            cv.put("LiftNO",LiftNO);
            cv.put("state",1);
            cv.put("time", Common.GetSysTime());

            db.insert("task",null,cv);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }


    public void DelYJinfo(String ContractNO,String LiftNO)
    {

        try {

            db.delete("ProjectInfo_TABLE", "ContractNO = ? and LiftNO = ? ", new String[]{ContractNO, LiftNO});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }



    public void DelTaskinfo(String ContractNO,String LiftNO)
    {

        try {

            db.delete("task","ContractNO = ? and LiftNO = ? ",new String[]{ContractNO,LiftNO} );
            db.delete("ProjectInfo_TABLE","ContractNO = ? and LiftNO = ? ",new String[]{ContractNO,LiftNO} );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }

    public void DelTaskinfo2(String ContractNO,String LiftNO)
    {

        try {

            db.delete("task","ContractNO = ? and LiftNO = ? ",new String[]{ContractNO,LiftNO} );
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }
    public String Gettaskstate(String ContractNO,String LiftNO)
    {
        String result="0";
        try {
            Cursor cursor = db.rawQuery("select state from task where ContractNO = ? and LiftNO= ? ",
                    new String[]{ContractNO,LiftNO});

            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }
            else
                result= "0";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String Getprojectname(String ContractNO,String LiftNO)
    {
        String result="0";
        try {
            Cursor cursor = db.rawQuery("select projectname from ProjectInfo_TABLE where ContractNO = ? and LiftNO= ? ",
                    new String[]{ContractNO,LiftNO});

            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }
            else
                result= "0";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String Getprojectcounts(String ContractNO,String LiftNO)
    {
        String result="0";
        try {
            Cursor cursor = db.rawQuery("select count(*) from ProjectInfo_TABLE where ContractNO = ? and LiftNO= ? ",
                    new String[]{ContractNO,LiftNO});

            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }
            else
                result= "0";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String GetscanCodeinfo(String cancode,String tableindex,String projecttype)
    {
        String strvar="";
        try {
            Cursor cursor=null;
            if (tableindex.equals("5"))
                cursor= db.rawQuery("select Floors from DoorHead_TABLE where FloorCode= ? ", new String[]{cancode});
            if (tableindex.equals("1"))
                cursor= db.rawQuery("select iteminfo,itemtype from WHBYJL_TABLE where itemcode= ? ", new String[]{cancode});
            if (tableindex.equals("2"))
                cursor= db.rawQuery("select iteminfo,itemtype from ZaWulift_WBJL_TABLE where itemcode= ? ", new String[]{cancode});
            if (tableindex.equals("3"))
                cursor= db.rawQuery("select iteminfo,itemtype from FuTi_WBJL_TABLE where itemcode= ? ", new String[]{cancode});
            if (tableindex.equals("4"))
                cursor= db.rawQuery("select iteminfo,itemtype from YeYalift_WBJL_TABLE where itemcode= ? ", new String[]{cancode});



            String mark="";
            if (cursor.moveToNext()) {
                strvar = cursor.getString(0);
                mark = cursor.getString(1);
                cursor.close();
                if (projecttype.equals("△（半月保养项目）"))
                {
                    if (!mark.contains("△"))
                        return "";
                }
                if (projecttype.equals("△+■（季度保养项目）"))
                {
                    if (!mark.contains("△") && !mark.contains("■"))
                        return "";
                }
                if (projecttype.equals("△+■+○（半年保养项目）"))
                {
                    if (!mark.contains("△") && !mark.contains("■")
                            && mark.contains("○"))
                        return "";
                }
//                if (projecttype.equals("△+■+○+★（全年保养项目）"))
//                {
//                    if (!mark.contains("△") && !mark.contains("■")
//                            && mark.contains("○") && mark.contains("★"))
//                        return "";
//                }

            }
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strvar;
    }


    public Boolean IsScanned(String ContractNO,String LiftNO,String code)
    {

        try {
            Cursor cursor = db.rawQuery("select * from ProjectInfo_TABLE where ContractNO = ? and LiftNO= ? and ScanCode = ? ",
                    new String[]{ContractNO,LiftNO,code});

            if (cursor.getCount() ==1)
                return  true;
            else
                return false;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



    public void Addprojectinfo(ScanData scandata)
    {

        try {
            ContentValues cv = new ContentValues();
            cv.put("ProjectName",scandata.ProjectName);
            cv.put("ContractNO",scandata.ContractNO);
            cv.put("ProjectType",scandata.ProjectType);
            cv.put("LiftType",scandata.LiftType);
            cv.put("CheckDate",scandata.CheckDate);
            cv.put("EffectiveDate",scandata.EffectiveDate);
            cv.put("ContractEffectiveDate",scandata.ContractEffectiveDate);
            cv.put("ReachTime",scandata.ReachTime);
            cv.put("CheckPerson",scandata.CheckPerson);
            cv.put("LiftNO",scandata.LiftNO);
            cv.put("LiftNOTime",scandata.LiftNOTime);
            cv.put("FactoryNo",scandata.FactoryNo);
            cv.put("RegisterNO",scandata.RegisterNO);
            cv.put("TableType",scandata.TableType);
            cv.put("ScanCode",scandata.ScanCode);
            cv.put("ScanInfo",scandata.ScanInfo);
            cv.put("ScanTime",scandata.ScanTime);
            cv.put("state",scandata.state);
            cv.put("pxid",scandata.pxid);
            db.insert("ProjectInfo_TABLE",null,cv);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }


    public void Addprojectinfophoto(ScanData scandata,String photofile)
    {

        try {
            ContentValues cv = new ContentValues();

            cv.put("projectcode",scandata.ProjectCode);
            cv.put("LiftNO",scandata.LiftNO);
            cv.put("LiftCode",scandata.LiftCode);
            cv.put("projectname",scandata.ProjectName);
            cv.put("ContractNO",scandata.ContractNO);
            cv.put("ScanCode",scandata.ScanCode);
            cv.put("Scanname",scandata.ScanInfo);
            cv.put("dt",scandata.ScanTime);
            cv.put("img",photofile);




            db.insert("photoinfo",null,cv);


        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }


    public void updateprojectinfostate(ScanData scandata)
    {

        try {
            ContentValues cv = new ContentValues();

            cv.put("state", scandata.state);
            db.update("ProjectInfo_TABLE", cv,
                    "ContractNO = ? and LiftNO = ? and ScanCode = ? and ProjectType = ?",
                    new String[]{scandata.ContractNO,scandata.LiftNO,
                            scandata.ScanCode,scandata.ProjectType});



        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }


    public String Getowername(String owercode,String pxid)
    {
        String result="";
        try {
            Cursor cursor = db.rawQuery("select name from Owner_TABLE where Number= ? and pxid = ? ",
                    new String[]{owercode,pxid});

            if (cursor.moveToNext()) {
                result = cursor.getString(0);
            }
            else
                result= "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updatetaskfinish(String ContractNO
    ,String OwnerName,String OwnerConfirmStatus)
    {

        try {
            ContentValues cv = new ContentValues();
            cv.put("OwnerName",OwnerName);
            cv.put("OwnerConfirmStatus",OwnerConfirmStatus);
            cv.put("OwnerConfirmTime",Common.GetSysOnlyTime());
            cv.put("LeaveTime",Common.GetSysOnlyTime());



            db.update("ProjectInfo_TABLE",cv,"ContractNO = ? "
                    ,new String[]{ContractNO} );
            cv = new ContentValues();
            cv.put("state","3");
            db.update("task",cv,"ContractNO = ?  "
                    ,new String[]{ContractNO} );




        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }














}
