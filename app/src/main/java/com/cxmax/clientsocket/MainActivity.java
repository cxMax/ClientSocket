package com.cxmax.clientsocket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    private Button btn, btn1;
    private TextView btn2;
    private EditText et;
    private TextView tv;
    //    private WifiManager w = null;
    private Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                        String command = "sh -c 'CLASSPATH=/data/app/com.wx.android.mobileaccessibility-1/base.apk /system/bin/app_process /system/bin com.android.support.core/Entrance";
//                String command = "am instrument -w -r -e debug false -e class com.android.support.test.UITestEntrance com.wx.android.mobileaccessibility/android.test.InstrumentationTestRunner";
//                ShellUtil.CommandResult rs = ShellUtil.execCommand(command, true);
//                Log.i("info", "run: " + rs.result + "-------" + rs.responseMsg + "-------" + rs.errorMsg);
//            }
//        }).start();
        btn = (Button) findViewById(R.id.btn);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (TextView) findViewById(R.id.btn2);
        et = (EditText) findViewById(R.id.et_send);
        tv = (TextView) findViewById(R.id.tv_js);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
//                        AdbShellUtil.adbCommond("push Main.dex /sdcard/Main.dex");
//                        String am = "am start com.wx.android.mobileaccessibility/com.wx.android.mobileaccessibility.LauncherActivity";
//                        try {
//                            Runtime.getRuntime().exec(am);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            Process p = Runtime.getRuntime().exec("cat /sys/class/power_supply/max170xx_battery/capacity");
//                            InputStream is = p.getInputStream();
//                            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
//                            String line = reader.readLine();
//                            Log.e("info", "run: " + line);
//                            is.close();
//                            reader.close();
//                            p.destroy();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }

//                        AdbShellUtil.shellCommond(new String[]{am});

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
//                        OutputStream outputStream = null;
//                        BufferedWriter bw = null;
//                        try {
//                            outputStream = socket.getOutputStream();
//                            bw = new BufferedWriter(new OutputStreamWriter(outputStream));
//                            //向服务器端发送一条消息
//                            String command = "3, 寻找控件\n";
//                            bw.write(command);
//                            bw.flush();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

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
            socket = new Socket("127.0.0.1",10086);

            //构建IO
            is = socket.getInputStream();
            os = socket.getOutputStream();

            bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            bw.write("1012," + ++i +"\n");
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
