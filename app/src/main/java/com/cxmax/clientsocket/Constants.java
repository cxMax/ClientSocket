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
    public static String index5 = "{\"index\": 5,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"resource_id\": \"\",\"content_desc\": \"\", \"focused\": \"true\", \"ui_class\": \"android.widget.EditText\"},\"input\": \"我是\",\"input_type\": \"append\",\"inject_enter_event\": true}\n";

    public static String index2 = "{\"index\": 2,\"uiInfo\": {\"index\": 0,\"text\": \"findAndClick\",\"resource_id\": \"find_tv\",\"content_desc\": \"\",\"class\": \"\"},\"input\": \"我是\"}\n";

    public static String index2_weixin = "{\"index\": 2,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"resource_id\": \"\",\"content_desc\": \"微信\",\"exact\": false,\"class\": \"\"},\"input\": \"\"}\n";

    public static String index1 = "{\"index\": 1003,\"uiInfo\": {\"index\": 0,\"text\": \"微信\",\"content_desc\": \"\",\"resource_id\": \"wx_tv|wx_tv2|wx_tv3\",\"ui_class\": \"\"}}\n";

    public static String index1003_list_item = "{\"index\": 1,\"uiInfo\": {\"index\": 0,\"text\": \"这是\",\"content_desc\": \"\",\"resource_id\": \"com.cxmax.clientsocket:id/item_recyclerview_tv\",\"exact\": \"false\",\"ui_class\": \"android.widget.TextView\"}}\n";
    public static String index1004_list = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.cxmax.clientsocket:id/item_recyclerview_tv\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"com.cxmax.clientsocket:id/recyclerview\",\"list_ui_class\": \"androidx.recyclerview.widget.RecyclerView\",\"list_text\": []}}\n";

    public static String index1004_toutiao = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.ss.android.article.news:id/title\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"\",\"list_ui_class\": \"android.support.v7.widget.RecyclerView\",\"list_text\": []}}\n";

    public static String index1004_meizu = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.meizu.media.reader:id/a0x\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"com.meizu.media.reader:id/a8f\",\"list_ui_class\": \"flyme.support.v7.widget.RecyclerView\",\"list_text\": []}}\n";


    public static String index1004_weixin = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.tencent.mm:id/baj\",\"ui_class\": \"android.view.View\"," +
            "\"list_resource_id\": \"com.tencent.mm:id/dcf\",\"list_ui_class\": \"android.widget.ListView\",\"list_text\": []}}\n";

    public static String index3_weixin_longclick = "{\"index\": 3,\"uiInfo\": {\"index\": 2,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.tencent.mm:id/bah\",\"ui_class\": \"android.widget.LinearLayout\"," + "list_text\": []}}\n";

    public static String index1_weixin_liebiao = "{\"index\": 1,\"uiInfo\": {\"index\": 2,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.tencent.mm:id/bah\",\"ui_class\": \"android.widget.LinearLayout\"," + "list_text\": []}}\n";

    public static String index1004_douyin = "{\"index\": 1004,\"uiInfo\": {\"index\": 0,\"text\": \"\",\"content_desc\": \"\"," +
            "\"resource_id\": \"com.ss.android.ugc.aweme:id/f5p\",\"ui_class\": \"android.widget.TextView\"," +
            "\"list_resource_id\": \"com.ss.android.ugc.aweme:id/dgo\",\"list_ui_class\": \"android.support.v7.widget.RecyclerView\",\"list_text\": []}}\n";


    /**
     * 锁屏
     */
    public static String index12 = "{\"index\":12}\n";
    public static String index14 = "{\"index\":14}\n";
    public static String index28 = "{\"index\":28}\n";
    public static String index27 = "{\"index\":27}\n";
    public static String index26 = "{\"index\":26}\n";

    public static String index5001 = "{\"index\":5001}\n";
    public static String index5002 = "{\"index\":5002}\n";

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
    public static String COMMAND_SAVE_SDCARD_IMG = "{\"index\":19, \"image_name\":\"123.jpeg\"}\n";
    /**
     * 获取无水印地址
     */
    public static String COMMAND_VIDEO = "{\"index\":29, \"video_url\":\"https://v.douyin.com/7Mosfw/\"}\n";
    /**
     * 开/关静音
     */
    public static String COMMAND_MUTE_ON = "{\"index\":8}\n";
    public static String COMMAND_MUTE_OFF = "{\"index\":9}\n";
    public static String COMMAND_SEND_SMS = "{\"index\":24, \"sms\": {\"number\":\"10086\",\"message\":\"10086 txt2\"}}\n";
    public static String COMMAND_GET_VERIFY_CODE = "{\"index\":25}\n";
    public static String COMMAND_STORAGE_INFO = "{\"index\":101}\n";
    public static String COMMAND_PERMISSION = "{\"index\":102}\n";
    public static String COMMAND_SHUABAO_JOB= "{\"index\":103}\n";
    public static String COMMAND_GET_PHONE_NUMBER = "{\"index\":15}\n";
    /**
     * 心跳 command
     */
    public static String COMMAND_HEART_BEATING = "{\"index\":2001}\n";
    public static String COMMAND_SS_VIDEO_SCRIPT = "{\"index\":5001}\n";
}
