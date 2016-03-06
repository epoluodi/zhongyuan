package com.apollo.elevator_check.Common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2015-01-18.
 */
public class CameraHelper {
    private static final String LOG_TAG = "CameraHelper";
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    public static Uri fileUri;

    public  static  String takePhoto( Activity activity ,int taskId   )
    {
        boolean result=false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file to save the image
        fileUri = getOutputMediaFileUri(taskId);
        result=fileUri!=null;

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        return  fileUri.getPath();
    }


    public  static  boolean takePhoto( Activity activity    )
    {
        boolean result=false;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // create a file to save the image

        activity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        return  result;
    }


    private static Uri getOutputMediaFileUri(int taskId)
    {
        Uri outputUri=null;
        File mediaStorageDir = null;
        try
        {
            mediaStorageDir = new File( Environment.getExternalStorageDirectory(),"zhongyuan");
            if (!mediaStorageDir.exists())
            {
             mediaStorageDir.mkdirs();
            }

            if (mediaStorageDir.exists())
            {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                File mediaFile = new File(mediaStorageDir.getPath() + File.separator + String.format("%s-%s.jpg", String.valueOf(taskId), timeStamp));

                return Uri.fromFile(mediaFile) ;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.d(LOG_TAG, "Error in Creating mediaStorageDir: "
                    + mediaStorageDir);
        }
        return  outputUri;
    }
}
