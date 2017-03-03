package com.woof.weatherfocus.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by Woof on 2/28/2017.
 */

public class TextUtil {
    /**
     * 该部分用于从数据库中取城市名称时候，通过正则表达式去除无用信息
     * @param city
     * @return
     */
    public static String replaceCityName(String city){
        city = city.replaceAll("(?:省|市|自治区|县|区|旗|盟|自治区|特别行政区)", "");
        return city;
    }

    /**
     * 拷贝帮助方法
     * @param info
     * @param context
     */
    public static void copyToClipboard(String info, Context context) {
        ClipboardManager manager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("msg", info);
        manager.setPrimaryClip(clipData);
        Toast.makeText(context, String.format("[%s] 已经复制到剪切板啦( •̀ .̫ •́ )✧", info), Toast.LENGTH_SHORT).show();
    }
}
