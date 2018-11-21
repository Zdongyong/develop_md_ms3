package com.moredian.securitygate;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.moredian.activation.Activation;
import com.moredian.activation.request.HandlerMsgType;
import com.moredian.activation.util.DeviceUtil;
import com.moredian.common.arouter.ArouterCenter;
import com.moredian.common.arouter.ArouterConfig;
import com.moredian.common.arouter.RouterURLS;
import com.moredian.common.view.view.RippleLayout;
import com.moredian.face.MainActivity;
import com.moredian.mdservice.IMdSericelInterface;

/**
 * 加载配置文件Activity
 */
@Route(path = RouterURLS.MD_SPLASH)
public class SplashActivity extends AppCompatActivity implements Activation.ActivationListener{
    private static final String TAG = SplashActivity.class.getSimpleName();

    private ImageView qrImg;
    private RippleLayout rippleLayout;
    private TextView hintText, macText;
    private RelativeLayout relWaveInfo;
    private Handler handler;
    private String secretKey;
    IMdSericelInterface mdService;
    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                mdService = IMdSericelInterface.Stub.asInterface(service);
                secretKey = mdService.getSecretKey();
                Log.i(TAG, "secretKey: " + secretKey + "\nsn="+Build.SERIAL+"\nmac="+DeviceUtil.getLocalMacAddressFromIp());
                Activation.getInstance().setSecretKey(secretKey);
            } catch (Exception e) {
                Log.i(TAG, "Exception: " + e.toString());
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        qrImg = (ImageView) findViewById(R.id.create_img);
        hintText = (TextView) findViewById(R.id.hintText);
        relWaveInfo = (RelativeLayout) findViewById(R.id.rel_wave_info);
        macText = (TextView) findViewById(R.id.macText);
        macText.setText(DeviceUtil.getLocalMacAddressFromIp() + "-" + Build.SERIAL);

        rippleLayout = (RippleLayout) findViewById(R.id.ripple_layout);
        rippleLayout.startRippleAnimation();

        Intent intent = new Intent();
        intent.setAction("com.mordian.SysService");
        intent.setPackage("com.moredian.mdservice");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        handler = new MyHandler(getMainLooper());
        Activation.getInstance().setActivationListener(this);

//        SystemSever.getInterfaceImpl().bindServer(getApplicationContext());

        //加载配置文件
//        AppSingleton.getInstance().submit(new LoadConfigsRunnable(this, handler));
    }

    @Override
    public void onHandlerMessage(int i, String s) {
        switch (i) {
            case HandlerMsgType.ACTION_ERROR:
                Activation.getInstance().action(s);
                Log.e(TAG, "激活异常:" + s);
                break;
            case HandlerMsgType.ACTION_FAILED:
                hintText.setText("激活失败，请联系管理员");
                Log.e(TAG, "激活失败:" + s);
                Activation.getInstance().activate();
                break;
            case HandlerMsgType.QR_ERROR:
                Activation.getInstance().activate();
                break;
            case HandlerMsgType.QR_FAILED:
                hintText.setText("获取二维码失败，请联系管理员");
                Log.e(TAG, "获取二维码失败:" + s);
                Activation.getInstance().activate();
                break;
        }
    }

    @Override
    public void onQrCallBack(String s) {
        if (TextUtils.isEmpty(s)) {
            Log.e(TAG, "获取二维码失败");
        } else {
            relWaveInfo.setVisibility(View.GONE);
            qrImg.setVisibility(View.VISIBLE);
            updateQR(s);
        }
    }

    @Override
    public void onActivateSuccess() {
        hintText.setText("激活成功");
        rippleLayout.stopRippleAnimation();
//        handler.sendEmptyMessageDelayed(MsgWhatManager.REGISTER_OK,2000);
    }


    private class MyHandler extends Handler {

        public MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
//                case MsgWhatManager.LOAD_DEFAULT_CONFIGS:
//                case MsgWhatManager.LOAD_SETTING_CONFIGS:
//                    if(msg.what == MsgWhatManager.LOAD_DEFAULT_CONFIGS) {
//                    }
//                    Activation.getInstance().activate();
//                    break;
//                case MsgWhatManager.LOAD_CONFIGS_FAILED:
//                    break;
//                case MsgWhatManager.REGISTER_OK:
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    break;
//                default:
//                    break;
            }
        }
    }

    private void updateQR(String strQr) {
        try {
//            Bitmap qrcodeBitmap = QRCode.createQRCodeWithLogo(strQr, 500, BitmapFactory.decodeResource(getResources(), R.mipmap.qr_code_logo));
//            if (qrcodeBitmap != null) {
//                qrImg.setImageBitmap(qrcodeBitmap);
//                hintText.setText("请扫码屏幕上方二维码");
//            } else {
//                Toast.makeText(this, "生成二维码失败", Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rippleLayout.stopRippleAnimation();
        unbindService(mServiceConnection);
    }
}
