package com.expscannerrn;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.google.gson.Gson;
import com.intsig.exp.sdk.ISCardScanActivity;
import com.intsig.expressscanner.PreviewActivityAuto;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ExpScannerReaderManager extends ReactContextBaseJavaModule {

    private Callback mResultCallback;
    /*
     * @CN:替换您申请的合合信息授权提供的APP_KEY; 2017 0331
     *
     * @EN:replace your appkey from the intsig company
     */
    private static final String APP_KEY = "3d88abba23cff6632f582ea191-vagfvt";
    private static final int REQ_CODE_CAPTURE = 100;
    private static final int REQ_CODE_PP_CAPTURE = 101;

    private static final String TEXT = "RetCode";
    private static final String FORMAT = "RetMSG";
    public static boolean boolCheckAppKey = true;
    public static final String BANKCARD_TRIM_DIR = Environment.getExternalStorageDirectory() + "/trimedcard.jpg";
    public static final String BANKCARD_ORG_DIR = Environment.getExternalStorageDirectory() + "/origianlcard.jpg";
    public static final String DIR_IMG_RESULT = Environment.getExternalStorageDirectory()+"/ppcardscan/";

    public ExpScannerReaderManager(ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "ExpScannerReaderManager";
    }

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {

        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode == REQ_CODE_CAPTURE) {
                if (mResultCallback != null) {
                    if (resultCode == Activity.RESULT_OK) {

                       String  number =  intent
                                .getStringExtra("number");
                        Map<String, Object> resultMap = new HashMap<>();
                        String  phone =    intent.getStringExtra("phone");
                        resultMap.put(TEXT, 0);
                        resultMap.put(FORMAT, "success");
                        resultMap.put("barcode", number==null?"":number);
                        resultMap.put("phone", phone==null?"":phone);

                        String jsonResult = new Gson().toJson(resultMap);
                        mResultCallback.invoke(jsonResult);
                    } else if (resultCode == Activity.RESULT_CANCELED && requestCode == REQ_CODE_CAPTURE) {


                         Integer code =-100;
                         if(intent!=null) {
                             code = intent
                                     .getIntExtra(ISCardScanActivity.EXTRA_KEY_RESULT_ERROR_CODE, 0);
                         }
//                        final String codeMsg = intent
//                                .getStringExtra(ISCardScanActivity.EXTRA_KEY_RESULT_ERROR_MSG);
                        // Toast.makeText(getReactApplicationContext(), "Cancelled", Toast.LENGTH_LONG).show();

                        JSONObject obj = new JSONObject();
                        try {
                            obj.put(TEXT, code + "");
                            obj.put(FORMAT, code==-100?"cancel":"error code");
                        } catch (JSONException e) {
                            Log.d("json cancel", "This should never happen");
                        }
                        mResultCallback.invoke(obj.toString());

                    }

                }
            }





        }
    };


    @ReactMethod
    public void scannerExp(String appkey, Callback resultCallback, Callback errorCallback) {


        mResultCallback = resultCallback;

        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            errorCallback.invoke("Activity doesn't exist");
            return;
        }


        Intent intent = new Intent(currentActivity, PreviewActivityAuto.class);
        // 合合信息授权提供的APP_KEY
        intent.putExtra(PreviewActivityAuto.EXTRA_KEY_BOOL_BAR, false);// 是否开启同时识别

        intent.putExtra(PreviewActivityAuto.EXTRA_KEY_APP_KEY, appkey);

        currentActivity.startActivityForResult(intent, REQ_CODE_CAPTURE);
    }



}