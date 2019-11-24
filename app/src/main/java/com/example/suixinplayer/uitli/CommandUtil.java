package com.example.suixinplayer.uitli;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandUtil {


    //判断字符串是不是以数字开头
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9].*");
        Matcher isNum = pattern.matcher(str.charAt(0)+"");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }
}
