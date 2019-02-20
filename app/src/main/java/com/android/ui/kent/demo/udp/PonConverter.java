package com.android.ui.kent.demo.udp;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Kent Song on 2019/2/19.
 */
public class PonConverter {
    public static String convertHex2WiFi_MAC(String hexStr) {
        return formatMac(hexStr);
    }

    public static String convertHex2ONU_MAC(String hexStr) {
        return "";
    }

    public static String formatMac(String mac) {
        String regex = "[0-9a-fA-F]{12}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mac);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("mac format is error");
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 12; i++) {
            char c = mac.charAt(i);
            sb.append(c);
            if ((i & 1) == 1 && i <= 9) {
                sb.append(":");
            }
        }

        return sb.toString();
    }

    public static String formatIp(String mac) {
        String regex = "[0-9a-fA-F]{8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(mac);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("ip format is error");
        }

        StringBuilder sb = new StringBuilder();

        int hexUnitLength = 2;
        for (int i = 0; i < 8; i = i + 2) {
            String hex = mac.substring(i, i + hexUnitLength);
            sb.append(Integer.parseInt(hex, 16));
            if (i < 6) {
                sb.append(".");
            }
        }


        return sb.toString();
    }

    public static short parseHex4(String num) {
        if (num.length() != 4) {
            throw new NumberFormatException("Wrong length: " + num.length() + ", must be 4.");
        }
        int ret = Integer.parseInt(num, 16);
        ret = ((ret & 0x8000) > 0) ? (ret - 0x10000) : (ret);
        return (short) ret;
    }
}
