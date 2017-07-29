package com.apollo.elevator_check;

import android.app.Activity;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.apollo.elevator_check.Common.Common;
import com.apollo.elevator_check.db.DBManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckTaskActivity extends Activity {

    private Button btnreturn;
    private ListView listView;
    private MyAdapter myAdapter;
    private String pxid,liftno;

    private List<Map<String,String>> mapList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_task);
        btnreturn = (Button)findViewById(R.id.buttonok);
        btnreturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        pxid = getIntent().getStringExtra("pxid");
        liftno = getIntent().getStringExtra("liftno");

        mapList = new ArrayList<>();


        myAdapter = new MyAdapter();
        initData();
        listView = (ListView)findViewById(R.id.list1);

        listView.setAdapter(myAdapter);



    }


    private void initData()
    {
        Cursor cursor = Common.mainDB.getScanCodeInfo(pxid,liftno);
        if (cursor == null)
            return;
        while (cursor.moveToNext())
        {
            Map<String,String> map = new HashMap<>();
            map.put("ScanInfo",cursor.getString(19));
            map.put("state",String.valueOf(cursor.getInt(22)));
            map.put("scantime",cursor.getString(20));
            mapList.add(map);
        }
        cursor.close();
        myAdapter.notifyDataSetChanged();
    }


    private class MyAdapter extends BaseAdapter
    {

        private TextView scaninfo,scantime,scanstate;
        @Override
        public int getCount() {
            return mapList.size();
        }

        @Override
        public Object getItem(int position) {
            return mapList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(CheckTaskActivity.this).
                    inflate(R.layout.list_checktask,null);
            Map<String,String>map = mapList.get(position);
            scaninfo = (TextView)convertView.findViewById(R.id.scaninfo);
            scantime = (TextView)convertView.findViewById(R.id.scantime);
            scanstate = (TextView)convertView.findViewById(R.id.state);
            scaninfo.setText(map.get("ScanInfo"));
            scantime.setText("扫描时间:" + map.get("scantime"));
            if (map.get("state").equals("0"))
            {
                scanstate.setText("状态:合格");
                scanstate.setTextColor(CheckTaskActivity.this.getResources().getColor(R.color.green1));

            }else
            {
                scanstate.setText("状态:不合格");
                scanstate.setTextColor(CheckTaskActivity.this.getResources().getColor(R.color.red));
            }


            return convertView;
        }
    }

}
