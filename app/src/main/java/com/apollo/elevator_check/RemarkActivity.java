package com.apollo.elevator_check;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class RemarkActivity extends Activity {


    private Button btnok;
    private EditText remark;
    private String pxid, liftno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remark);
        btnok = (Button) findViewById(R.id.ok);
        remark = (EditText) findViewById(R.id.remark);

        pxid = getIntent().getStringExtra("pxid");
        liftno = getIntent().getStringExtra("liftno");
        btnok.setOnClickListener(onClickListenerbtnok);


        String content = ReadTxtFile(getFilesDir() + "/" + pxid + "_" + liftno + ".txt");
        remark.setText(content);
        remark.setHint("填写备注信息");
    }


    View.OnClickListener onClickListenerbtnok = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            WriteTxt(getFilesDir() + "/" + pxid + "_" + liftno + ".txt", remark.getText().toString());
            finish();
        }
    };


    public static String ReadTxtFile(String strFilePath) {
        String path = strFilePath;
        String content = ""; //文件内容字符串
        //打开文件
        File file = new File(path);
        if (!file.exists())
            return "";
        try {
            InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }
                buffreader.close();
                instream.close();
            }
        } catch (java.io.FileNotFoundException e) {
            Log.d("TestFile", "The File doesn't not exist.");
        } catch (IOException e) {
            Log.d("TestFile", e.getMessage());
        }

        return content;
    }


    public void WriteTxt(String path, String content) {


        //打开文件

        try {
            File file = new File(path);
            if (!file.exists())
                file.createNewFile();
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(content);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();


        } catch (java.io.FileNotFoundException e) {
            Log.d("TestFile", "The File doesn't not exist.");
        } catch (IOException e) {
            Log.d("TestFile", e.getMessage());
        }
    }


}
