package com.cxmax.clientsocket;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WxMobile/ClientMain";
    private Button sendBtn, connect_socket;
    private TextView btn2;
    private EditText et;
    private TextView tv;
    //    private WifiManager w = null;
    private Socket socket;
    private int imageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        doWorkBackground(() -> {
            try {
                // 等界面先起来再去连接
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // String command = "sh -c 'CLASSPATH=/data/app/com.wx.android.mobileaccessibility-1/base.apk /system/bin/app_process /system/bin com.android.support.core/Entrance";
            String command = "am instrument -w -r -e debug false -e class com.android.support.test.UITestEntrance com.wx.android.mobileaccessibility/android.test.InstrumentationTestRunner";
            ShellUtil.CommandResult rs = ShellUtil.execCommand(command, true);
            Log.i(TAG, "run: " + rs.result + "-------" + rs.responseMsg + "-------" + rs.errorMsg);
        });
        sendBtn = (Button) findViewById(R.id.btn_send);
        connect_socket = (Button) findViewById(R.id.connect_socket);
        btn2 = (TextView) findViewById(R.id.btn2);
        et = (EditText) findViewById(R.id.et_send);
        tv = (TextView) findViewById(R.id.tv_js);

        connect_socket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doWorkBackground(() -> connect());
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String string = et.getText().toString();
                doWorkBackground(() -> actionPerformed());
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "通过UI查找并点击", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.btn_send_image).setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btn_send_image");
            doWorkBackground(() -> sendBitmap());
        });
    }

    /**
     * 这里仅仅是与server建立连接
     */
    private void connect() {
        try {
            // socket = new Socket(InetAddress.getLocalHost(), 10086);
            if (socket != null) {
                socket.close();
            }
            socket = new Socket("127.0.0.1", 10086);
        } catch (Exception e) {
            Log.e(TAG, "connect: e:" + e);
        }
        Log.d(TAG, "connect: socket:" + socket);
    }

    /**
     * 发送指令数据
     */
    public void actionPerformed() {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {

            //构建IO
            is = socket.getInputStream();
            os = socket.getOutputStream();

            bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write(Constants.index12);
            bw.flush();

            //读取服务器返回的消息
            br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            System.out.println("服务器："+mess);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (IOException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        } finally {
//            try {
//                if (is != null) {
//                    is.close();
//                }
//                if (os != null) {
//                    os.close();
//                }
//                if (bw != null) {
//                    bw.close();
//                }
//                if (br != null) {
//                    br.close();
//                }
//                if (socket != null) {
//                    socket.close();
//                }
//            } catch (IOException e3) {
//                // TODO Auto-generated catch block
//                e3.printStackTrace();
//            }
        }

    }

    public void sendBitmap() {
        try {
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            InputStream fIs = getAssets().open("socket_bitmap.jpg");
            int available = fIs.available();
            byte[] imageData = new byte[available];
            int read = fIs.read(imageData);
            Log.w(TAG, "sendBitmap read:" + read);

            JSONObject obj = new JSONObject();
            obj.put("index", 18);
            obj.put("image", Base64.encodeToString(imageData, 0, read, Base64.URL_SAFE));
            bw.write(obj.toString());
            // must new line to flush data
            bw.write("\n");
            bw.flush();

            Log.w(TAG, "图片发送完成，长度:" + available);

            // 读取服务器返回的消息
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String mess = br.readLine();
            Log.d(TAG, "sendBitmap: receive path：" + mess);
        } catch (IOException e) {
            Log.e(TAG, "sendBitMap: e:" + e);
        } catch (JSONException e) {
            Log.e(TAG, "sendBitmap: json e:" + e);
        }
        Log.d(TAG, "sendBitMap: succeed");
    }

    /**
     * 后台任务，需要子线程处理的，理论上跑线程池更好
     * 目前测试阶段的话，就直接新建Thread吧
     * @param work 需要执行的任务
     */
    private void doWorkBackground(Runnable work) {
        Thread wt = new Thread(work);
        wt.setPriority(Thread.NORM_PRIORITY + 1);
        wt.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
    }

    private void clear() {
        if (socket != null) {
            IOUtil.silenceClose(socket);
        }
    }
}
