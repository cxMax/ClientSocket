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
    public static String index5 = "{\"index\": 5,\"uiInfo\": {\"index\": -1,\"text\": \"\",\"resource_id\": [\"\"],\"content_desc\": \"et_send\",\"class\": \"android.widget.EditText\"},\"input\": \"我是\"}\n";
    public static String index1 = "{\"index\": 1,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\",\"resource_id\": \"com.cxmax.clientsocket:id/wx_tv11\",\"ui_class\": \"\"}}\n";

    public static String index1003_list_item = "{\"index\": 1003,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\",\"resource_id\": \"com.cxmax.clientsocket:id/item_recyclerview_tv\",\"ui_class\": \"android.widget.TextView\"}}\n";
    public static String index1004_list = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.cxmax.clientsocket:id/item_recyclerview_tv\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"com.cxmax.clientsocket:id/recyclerview\",\"list_ui_class\": \"androidx.recyclerview.widget.RecyclerView\",\"list_text\": []}}\n";

    public static String index1004_toutiao = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.ss.android.article.news:id/title\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"\",\"list_ui_class\": \"android.support.v7.widget.RecyclerView\",\"list_text\": []}}\n";

    public static String index1004_meizu = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.meizu.media.reader:id/a0x\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"com.meizu.media.reader:id/a8f\",\"list_ui_class\": \"flyme.support.v7.widget.RecyclerView\",\"list_text\": []}}\n";


    /**
     * 锁屏
     */
    public static String index12 = "{\"index\":12}\n";
    public static String index27 = "{\"index\":27}\n";
    public static String index26 = "{\"index\":26}\n";

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
    public static String COMMAND_SAVE_SDCARD_IMG = "{\"index\":19, \"image_name\":\"123.png\"}\n";
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
    public static String COMMAND_SS_VIDEO_SCRIPT = "{\"index\":5001}\n";
}
