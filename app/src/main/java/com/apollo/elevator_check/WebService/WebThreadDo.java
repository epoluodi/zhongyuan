package com.apollo.elevator_check.WebService;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.apollo.elevator_check.Common.Common;

import org.ksoap2.serialization.PropertyInfo;


/**
 * Created by Administrator on 14-6-9.
 */
public class WebThreadDo {

    RunClass runClass;
    Thread thread = null;
    Handler handler;
    Webservice webservice;
    IcallBackWebReponse icallBackWebReponse = null;
    public Message amessage;
    int calltype;

    public WebThreadDo(PropertyInfo[] propertyInfo, String methname) {

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                amessage =msg;
                if (icallBackWebReponse != null) {

                    icallBackWebReponse.setCallBackWebreponseXML(msg);


                }
            }
        };


        runClass = new RunClass(propertyInfo, methname);
    }


    public void requstWebinterfaceForString(Boolean sync) {
        try {
            thread = new Thread(runClass);
            calltype = 0;
            thread.start();

            if (sync == true) {
                synchronized (Common.olock) {

                    Common.olock.wait();
                }
            }
        } catch (Exception ex) {
            if (icallBackWebReponse != null)
                icallBackWebReponse.setCallBackWebreponseXML(null);
        }
    }

    public void requstWebinterfaceForbyte(Boolean sync) {
        try {
            thread = new Thread(runClass);
            calltype = 1;
            thread.start();
            if (sync == true) {
                synchronized (Common.olock) {

                    Common.olock.wait();
                }
            }
        } catch (Exception ex) {
            if (icallBackWebReponse != null)
                icallBackWebReponse.setCallBackWebreponseXML(null);
        }
    }


    public void setIcallBackWebReponse(IcallBackWebReponse icallBackWebReponse_event) {
        icallBackWebReponse = icallBackWebReponse_event;
    }


    private final class RunClass implements Runnable {
        String strmethname;
        PropertyInfo[] mpropertyInfos = null;

        public RunClass(PropertyInfo[] propertyInfo, String methname) {
            strmethname = methname;
            mpropertyInfos = propertyInfo;
        }

        @Override
        public void run() {
            Looper.prepare();
            Message message = Message.obtain();
            webservice = new Webservice(Common.ServerWCF,15000);

            switch (calltype) {
                case 0:
                    String resultstr = webservice.PDA_GetInterFaceForStringNew(mpropertyInfos, strmethname);
                    message.what = 0;
                    message.obj = resultstr;
                    break;
                case 1:
                    byte[] resultbyte = webservice.PDA_GetInterFaceForbyteNew(mpropertyInfos, strmethname);
                    message.what = 1;
                    message.obj = resultbyte;
                    break;
            }

            amessage = message;
            handler.sendMessage(message);

            synchronized (Common.olock) {

                Common.olock.notify();
            }
        }
    }
}
