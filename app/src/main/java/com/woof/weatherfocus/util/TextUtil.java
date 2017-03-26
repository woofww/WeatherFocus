package com.woof.weatherfocus.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * Created by Woof on 2/28/2017.
 */

public class TextUtil {
    /**
     * 该部分用于从数据库中取城市名称时候，通过正则表达式去除无用信息
     * update: 修改正则表达式导致城市显示出错问题，改用条件判断
     * @param city
     * @return
     */
    public static String replaceCityName(String city){
        if (city.endsWith("省")) {
            return city.substring(0, city.lastIndexOf("省"));
        } else if (city.endsWith("市")) {
            return city.substring(0, city.lastIndexOf("市"));
        } else if (city.endsWith("自治区")) {
            return city.substring(0, city.lastIndexOf("自治区"));
        } else if (city.endsWith("县")) {
            if (city.length() <= 2) {
                return city;
            }
            return city.substring(0, city.lastIndexOf("县"));
        } else if (city.endsWith("区")) {
            return city.substring(0, city.lastIndexOf("区"));
        } else if (city.endsWith("旗")) {
            return city.substring(0, city.lastIndexOf("旗"));
        } else if (city.endsWith("盟")) {
            return city.substring(0, city.lastIndexOf("盟"));
        } else if (city.endsWith("特别行政区")) {
            return city.substring(0, city.lastIndexOf("特别行政区"));
        }
        return "";
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

    public static String setText(String text) {
        if (text == null){return "";}
        return text;
    }

    public static String setText(String prefix, String text){
        if (TextUtils.isEmpty(text)){ return "";}
        // 合并操作
        return TextUtils.concat(prefix, text).toString();
    }
}
