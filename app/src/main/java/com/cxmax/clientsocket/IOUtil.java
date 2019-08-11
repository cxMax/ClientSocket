package com.cxmax.clientsocket;

import android.util.Log;

import java.io.Closeable;
import java.io.IOException;

/**
 * MobileAccessibility.com.android.support.shell
 *
 * @ClassName: IOUtil
 * @Description:
 * @Author: tanlin
 * @Date: 2019-06-18
 * @Version: 1.0
 */
public class IOUtil {
    private static final String TAG = "IOUtil";

    public static void silenceClose(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                Log.e(TAG, "silenceClose failed, input Stream:" + stream);
            }
        }
    }
}
