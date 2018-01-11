package com.gigaiot.nlostserver.utils;

/**
 * Created by zz on 2017/6/7.
 */
public class Naming {


    public static String convent(String name) {
        StringBuilder n = new StringBuilder();
        int len = name.length();
        for (int i=0; i<len; ++i) {

            char c = name.charAt(i);

            if (c == '_' && i != (len-1)) {
                n.append(String.valueOf(name.charAt(++i)).toUpperCase());

            }else {
                if ( c != '_' ) {
                    n.append(c);
                }
            }
        }
        return n.toString();
    }

}
