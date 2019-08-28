package com.cxmax.clientsocket;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

    private Button btn, connect_socket;
    private TextView btn2;
    private EditText et;
    private TextView tv;
    //    private WifiManager w = null;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread(new Runnable() {
            @Override
            public void run() {
//                        String command = "sh -c 'CLASSPATH=/data/app/com.wx.android.mobileaccessibility-1/base.apk /system/bin/app_process /system/bin com.android.support.core/Entrance";
                String command = "am instrument -w -r -e debug false -e class com.android.support.test.UITestEntrance com.wx.android.mobileaccessibility/android.test.InstrumentationTestRunner";
                ShellUtil.CommandResult rs = ShellUtil.execCommand(command, true);
                Log.i("info", "run: " + rs.result + "-------" + rs.responseMsg + "-------" + rs.errorMsg);
            }
        }).start();
        btn = (Button) findViewById(R.id.btn);
        connect_socket = (Button) findViewById(R.id.connect_socket);
        btn2 = (TextView) findViewById(R.id.btn2);
        et = (EditText) findViewById(R.id.et_send);
        tv = (TextView) findViewById(R.id.tv_js);


        connect_socket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        actionPerformed();
                    }
                }).start();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String string = et.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        actionPerformed();
                    }
                }).start();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "通过UI查找并点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    int i = 0;
    public void actionPerformed() {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
//            socket = new Socket(InetAddress.getLocalHost(), 10086);
            if (socket != null) {
                socket.close();
            }
            socket = new Socket("127.0.0.1", 10086);

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
}
