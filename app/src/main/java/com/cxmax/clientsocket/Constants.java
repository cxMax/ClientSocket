package com.cxmax.clientsocket;

/**
 * @describe : 这里是socket指令， 在这里直接调用， 方便调试就好了
 * @usage :
 * <p>
 * </p>
 * Created by caixi on 2019-08-28.
 */
public class Constants {

    public static final String TAG = "WxMobile";
    /**
     * 往edit text 输入值
     */
    public static String index5 = "{\"index\": 5,\"ui\": {\"index\": -1,\"text\": \"\",\"resource_id\": \"\",\"content_desc\": \"et_send\",\"class\": \"android.widget.EditText\"},\"input\": \"我是\"}\n";

    /**
     * 锁屏
     */
    public static String index12 = "{\"index\":12}\n";

    /**
     * 解锁屏幕
     */
    public static String index13 = "{\"index\":13}\n";

    /**
     * 清空通讯录
     */
    public static String index23 = "{\"index\":23}\n";
    public static String COMMAND_CKB_TO_SELECT = "{\"index\":16}\n";
    public static String COMMAND_CKB_UN_SELECT = "{\"index\":17}\n";
    /**
     * 开/关静音
     */
    public static String COMMAND_MUTE_ON = "{\"index\":8}\n";
    public static String COMMAND_MUTE_OFF = "{\"index\":9}\n";
    public static String COMMAND_SEND_SMS = "{\"index\":24, \"sms\": {\"number\":\"10086\",\"message\":\"10086 txt2\"}}\n";
    public static String COMMAND_GET_VERIFY_CODE = "{\"index\":25}\n";
    public static String COMMAND_STORAGE_INFO = "{\"index\":101}\n";
    public static String COMMAND_GET_PHONE_NUMBER = "{\"index\":15}\n";
    /**
     * 心跳 command
     */
    public static String COMMAND_HEART_BEATING = "{\"index\":2001}\n";
}
