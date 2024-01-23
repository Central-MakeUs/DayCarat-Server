package com.example.daycarat.global.util;

public class StringParser {

    public static String getSubString(String str) {
        int length = 50;

        if (str.length() > length) {
            return str.substring(0, length) + "...";
        }
        return str;
    }
}
