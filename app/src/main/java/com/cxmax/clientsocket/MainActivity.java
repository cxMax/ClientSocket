package com.cxmax.clientsocket;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "WxMobile/ClientMain";
    private static Uri SMS_INBOX = Uri.parse("content://sms/");
    private Button sendBtn, connect_socket, btn_lock_unlock;
    private TextView find_tv;
    private EditText et;
    private TextView tv;
    //    private WifiManager w = null;
    private Socket socket;
    private int imageNumber = 0;
    // 联系人数据
    private JSONArray contactArray;
    private ExecutorService service;

    private volatile boolean isHeartBeating = false;
    private static final int SOCKET_HEART_BEATING = 59;

    private void initPool() {
        service = new ThreadPoolExecutor(
                2, 2,
                5L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), runnable -> {
                    Thread wt = new Thread(runnable);
                    wt.setPriority(Thread.NORM_PRIORITY + 1);
                    return wt;
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPool();
        // todo 以后这里通过 Install的main函数去启动
        doWorkBackground(() -> {
            try {
                // 等界面先起来再去连接
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String command = "am instrument -w -r -e debug false -e class com.wx.android.psy.test.UITestEntrance com.wx.android.psy/android.test.InstrumentationTestRunner";
            ShellUtil.CommandResult rs = ShellUtil.execCommand(command, true);
            Log.i(TAG, "run: " + rs.result + "-------" + rs.responseMsg + "-------" + rs.errorMsg);
        });
        sendBtn = findViewById(R.id.btn_send);
        connect_socket = findViewById(R.id.connect_socket);
        find_tv = findViewById(R.id.find_tv);
        et = findViewById(R.id.et_send);
        tv = findViewById(R.id.tv_js);
        findViewById(R.id.wx_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点击了微信", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.recycler_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RecyclerActivity.class));
                doWorkBackground(() -> {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actionPerformed(Constants.index1004_list);
                });
            }
        });
        firstLineButtons();
        secondLineButtons();
        thirdLineButtons();
        forthLineButtons();
        fifthLineButtons();


        find_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "通过UI查找并点击", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void firstLineButtons() {
        connect_socket.setOnClickListener(v -> {
            doWorkBackground(() -> connect());
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String string = et.getText().toString();
                // doWorkBackground(() -> actionPerformed(string));
                // doWorkBackground(() -> sendSms());
                // doWorkBackground(() -> getVerifyCode());
                // doWorkBackground(() -> actionPerformed(Constants.COMMAND_STORAGE_INFO));
//                doWorkBackground(() -> actionPerformed(Constants.COMMAND_STORAGE_INFO));
                doWorkBackground(() -> {
                    try {
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    actionPerformed(Constants.index1004_toutiao);
                });
            }
        });
    }

    private void secondLineButtons() {
        findViewById(R.id.btn_send_image).setOnClickListener(v -> {
            Log.d(TAG, "onCreate: btn_send_image");
            doWorkBackground(() -> {
                sendBitmap(); // custom data send logic
            });
        });
        findViewById(R.id.btn_get_number).setOnClickListener(view -> {
            doWorkBackground(() -> {
                actionPerformed(Constants.COMMAND_GET_PHONE_NUMBER);
            });
        });
    }

    private void thirdLineButtons() {
        List<ContactBean> infos = new ArrayList<>();
        findViewById(R.id.btn_send_contacts).setEnabled(false);
        findViewById(R.id.btn_query_contacts).setOnClickListener(v -> {
            doWorkBackground(() -> {
                infos.clear();
                infos.addAll(ContactUtils.getAllContacts(MainActivity.this));

                try {
                    contactArray = new JSONArray();
                    for (ContactBean item : infos) {
                        JSONObject obj = new JSONObject();

                        obj.put("id", item.id);
                        obj.put("name", item.name);
                        // handle phone number
                        JSONArray phones = new JSONArray();
                        for (ContactBean.Phone p : item.phones) {
                            JSONObject obj2 = new JSONObject();
                            obj2.put("type", p.type);
                            obj2.put("number", p.number);
                            phones.put(obj2);
                        }
                        obj.put("phones", phones);
                        contactArray.put(obj);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onCreate: btn_insert_contacts, e:" + e);
                }

                Log.d(TAG, "onCreate: getAll size:" + infos.size());
                runOnUiThread(() -> findViewById(R.id.btn_send_contacts).setEnabled(true));
            });
        });
        findViewById(R.id.btn_remove_contacts).setOnClickListener(v -> {
            doWorkBackground(() -> {
                ContactUtils.deleteContacts(MainActivity.this, infos);
            });
        });
        findViewById(R.id.btn_send_contacts).setOnClickListener(v -> {
            if (contactArray == null) {
                Toast.makeText(MainActivity.this, "请先点击查看联系人", Toast.LENGTH_SHORT).show();
                return;
            }
            doWorkBackground(() -> {
                String command = "{\"index\":22, contact:" + contactArray.toString() + "}\n";
                actionPerformed(command);
            });
        });
    }

    private void forthLineButtons() {
        findViewById(R.id.btn_lock_unlock).setOnClickListener(view -> {
            doWorkBackground(() -> {
                Log.i(TAG, "onCreate: will send lock");
                actionPerformed(Constants.index12);
            });

            doWorkBackground(() -> {
                try {
                    Log.i(TAG, "onCreate: will send unlock");
                    TimeUnit.SECONDS.sleep(10);
                    actionPerformed(Constants.index13);
                    Log.i(TAG, "onCreate: will send unlock end");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        });

        // 连接socket之后，自动开启心跳，可以不需要按钮的点击
        findViewById(R.id.btn_heart_beating).setOnClickListener(v -> {
            heartBeating();
        });
    }

    private void fifthLineButtons() {
        // --------- checkbox begin --------- //
        findViewById(R.id.btn_ckbox_check).setOnClickListener(new View.OnClickListener() {
            String command = Constants.COMMAND_CKB_UN_SELECT;
            @Override
            public void onClick(View view) {
                doWorkBackground(() -> {
                    if (Constants.COMMAND_CKB_TO_SELECT.equals(command)) {
                        command = Constants.COMMAND_CKB_UN_SELECT;
                    } else {
                        command = Constants.COMMAND_CKB_TO_SELECT;
                    }
                    actionPerformed(command);
                });
            }
        });
        // --------- phone mute begin --------- //
        findViewById(R.id.btn_phone_mute).setOnClickListener(new View.OnClickListener() {
            String command = Constants.COMMAND_MUTE_OFF;
            @Override
            public void onClick(View view) {
                doWorkBackground(() -> {
                    if (Constants.COMMAND_MUTE_OFF.equals(command)) {
                        command = Constants.COMMAND_MUTE_ON;
                    } else {
                        command = Constants.COMMAND_MUTE_OFF;
                    }
                    actionPerformed(command);
                });
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void getSmsFromPhone2() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceid = tm.getDeviceId(); //获取ID号
        String tel = tm.getLine1Number();//手机号码
        String imei = tm.getSimSerialNumber();
        String imsi = tm.getSubscriberId();
        int simState = tm.getSimState();
        Log.d(TAG, "onClick: deviceid:" + deviceid);
        Log.d(TAG, "onClick: tel:" + tel);
        Log.d(TAG, "onClick: imei:" + imei);
        Log.d(TAG, "onClick: imsi:" + imsi);
        Log.d(TAG, "onClick: simState:" + simState);
    }

    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{"body", "_id", "address", "person", "date", "type"};
        long now = System.currentTimeMillis();
        long day = 24 * 60 * 60 * 1000;
        String send = "type = 1 AND date > " + (now - 10 * day);
        Cursor cur = cr.query(SMS_INBOX, projection, send, null, "date desc");
        if (null == cur) {
            return;
        }

        Log.d(TAG, "getSmsFromPhone: " + cur.getCount());
        while (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));//短信内容
            String date = cur.getString(cur.getColumnIndex("date"));//短信内容
            Log.d(TAG, "getSmsFromPhone: number:" + number + ", person:" + name + ", body:" + body + ", date:" + date + "\n");
            // 这里我是要获取自己短信服务号码中的验证码~~
            // Matcher matcher = pattern.matcher(body);
            // if (matcher.find()) {
            //     String res = matcher.group().substring(1, 11);
            //     //Log.d(TAG, "getSmsFromPhone: " + res);
            // }
        }
    }

    private void sendSms() {
        actionPerformed(Constants.COMMAND_SEND_SMS);
    }

    private void getVerifyCode() {
        actionPerformed(Constants.COMMAND_GET_VERIFY_CODE);
    }

    /**
     * 这里仅仅是与server建立连接
     */
    private void connect() {
        try {
             socket = new Socket(InetAddress.getLocalHost(), 10086);
//            if (socket != null) {
//                socket.close();
//            }
//            socket = new Socket("127.0.0.1", 10086);
//            socket.setKeepAlive(true);
//            // 读写超时时间，3 分钟
//            socket.setSoTimeout(1000 * 60 * 3);
            // 开始心跳，59s 一次心跳包
//            heartBeating();
        } catch (Exception e) {
            Log.e(TAG, "connect: e:" + e);
        }
        Log.d(TAG, "connect: socket:" + socket);
    }

    /**
     * 发送指令数据
     */
    public void actionPerformed(String command) {
        InputStream is = null;
        OutputStream os = null;
        BufferedWriter bw = null;
        BufferedReader br = null;
        try {
            if (socketUnavailable()) {
                return;
            }

            //构建IO
            is = socket.getInputStream();
            os = socket.getOutputStream();

            bw = new BufferedWriter(new OutputStreamWriter(os));
            //向服务器端发送一条消息
            // bw.write(Constants.index5);
            bw.write(command);
            bw.flush();
            Log.d(TAG, "actionPerformed: 发送命令结束");

            //读取服务器返回的消息
            // br = new BufferedReader(new InputStreamReader(is));
            // String mess = br.readLine();
            // Log.d(TAG, "actionPerformed: 服务器：" + mess);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public void sendBitmap() {
        try {
            if (socketUnavailable()) {
                return;
            }

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

    private void heartBeating() {
//        if (socket == null || isHeartBeating) {
//            return;
//        }
//        isHeartBeating = true;
        doWorkBackground(() -> {
            InputStream is = null;
            OutputStream os = null;
            BufferedWriter bw = null;
            BufferedReader br = null;

            try {
                is = socket.getInputStream();
                os = socket.getOutputStream();
            } catch (Exception e) {
                Log.e(TAG, "heartBeating: get ins/outs error:" + e);
                return;
            }

//            while (isHeartBeating) {
                try {
//                    TimeUnit.SECONDS.sleep(SOCKET_HEART_BEATING);
                    // Log.d(TAG, "actionPerformed: lock released");
                    bw = new BufferedWriter(new OutputStreamWriter(os));
                    bw.write(Constants.COMMAND_HEART_BEATING);
                    bw.flush();
                    Log.d(TAG, "heartBeating: 发送命令结束，等待结果");

                    //读取服务器返回的消息
                    br = new BufferedReader(new InputStreamReader(is));
                    String mess = br.readLine();
                    Log.d(TAG, "heartBeating: 服务器：" + mess);
                } catch (IOException e) {
                    Log.e(TAG, "heartBeating: e:" + e);
//                    break;
                }
//            }
//            IOUtil.silenceClose(is);
//            IOUtil.silenceClose(os);
        });
    }

    /**
     * 后台任务，需要子线程处理的，理论上跑线程池更好
     * 目前测试阶段的话，就直接新建Thread吧
     * @param work 需要执行的任务
     */
    private void doWorkBackground(Runnable work) {
        service.submit(work);
        Log.v(TAG, "doWorkBackground: " + work);
    }

    private boolean socketUnavailable() {
        boolean unavailable = socket == null || socket.isClosed();
        if (unavailable) {
            sendBtn.post(() -> {
                Toast.makeText(MainActivity.this, "请先点击连接Socket", Toast.LENGTH_SHORT).show();
            });
        }
        return unavailable;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clear();
        service.shutdownNow();
    }

    private void clear() {
        isHeartBeating = false;
        if (socket != null) {
            IOUtil.silenceClose(socket);
            socket = null;
        }
    }
}
