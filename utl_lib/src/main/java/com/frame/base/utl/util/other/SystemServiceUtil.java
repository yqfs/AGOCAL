package com.frame.base.utl.util.other;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.TrafficStats;
import android.os.Environment;
import android.os.StatFs;
import android.text.ClipboardManager;
import android.widget.Toast;

import java.io.File;

/**
 * 系统服务工具类：sd卡是否可用、相机是否可用
 * Created by YANGQIYUN on 14-3-1.
 */
public class SystemServiceUtil {

    /**
     * 判断sd卡是否可用，并且可检查剩余空间大小，为0时不检查
     *
     * @param checkAvaiableSize 剩余空间大小，单位byte
     * @return
     */
    public static boolean isSDUseable(int checkAvaiableSize) {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return false;
        }
        if (checkAvaiableSize > 0) {
            File root = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(root.getPath());
            long blockSize = statFs.getBlockSize();
            long availableBlocks = statFs.getAvailableBlocks();
            long availableSize = availableBlocks * blockSize;
            return availableSize > checkAvaiableSize;
        } else {
            return true;
        }
    }

    /**
     * 相机是否可用
     *
     * @param context
     * @return
     */
    public static boolean isCamaraUseable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 系统复制
     *
     * @param context
     * @param content 复制的内容
     */
    public static void systemCopy(Context context, String content) {
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            clipboardManager.setText(content);
            Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取app当前的流量消耗值
     *
     * @return
     */
    public static long getCurrentAppConsumeTraffic() {
        int myUid = android.os.Process.myUid();
        long recevied = TrafficStats.getUidRxBytes(myUid);
        long sended = TrafficStats.getUidTxBytes(myUid);
        return recevied + sended;
    }
}
