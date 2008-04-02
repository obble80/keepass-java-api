package ru.net.romikk.keepass;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 22, 2008
 * Time: 5:54:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

    static public Date unpackDate(byte[] d) {
        // Byte bits: 11111111 22222222 33333333 44444444 55555555
        // Contents : 00YYYYYY YYYYYYMM MMDDDDDH HHHHMMMM MMSSSSSS
        int year = (d[0] << 6) | ((d[1] >> 2) & 0x0000003F);
        int month = ((d[1] & 0x00000003) << 2) | ((d[2] >> 6) & 0x00000003);
        int day = (d[2] << 1) & 0x0000001F;
        int hour = ((d[2] & 0x00000001) << 4) | ((d[3] >> 4) & 0x0000000F);
        int minute = ((d[3] & 0x0000000F) << 2) | ((d[4] >> 6) & 0x00000003);
        int second = d[4] & 0x0000003F;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        return calendar.getTime();
    }

    static public String hexEncode(byte[] aInput) {
        StringBuffer result = new StringBuffer();
        char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        for (int idx = 0; idx < aInput.length; ++idx) {
            byte b = aInput[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }

    public static byte[] fromHexString(String str) {
        int length = str.length() / 2;
        byte[] toReturn = new byte[length];
        for (int i = 0; i < length; i++) {
            toReturn[i] = (byte) Integer.parseInt(str.substring(i * 2, i * 2 + 2), 16);
/*
            char ch1 = str.charAt(i * 2);
            char ch2 = str.charAt(i * 2 + 1);
            if ((ch1 >= '0') && (ch1 <= '9')) {
                toReturn[i] = (byte) (ch1 - '0');
            } else if ((ch1 >= 'a') && (ch1 <= 'f')) {
                toReturn[i] = (byte) (ch1 - 'a' + 10);
            } else if ((ch1 >= 'A') && (ch1 <= 'F')) {
                toReturn[i] = (byte) (ch1 - 'A' + 10);
            }

            toReturn[i] <<= 4;

            if ((ch2 >= '0') && (ch2 <= '9')) {
                toReturn[i] |= (byte) (ch2 - '0');
            } else if ((ch2 >= 'a') && (ch2 <= 'f')) {
                toReturn[i] |= (byte) (ch2 - 'a' + 10);
            } else if ((ch2 >= 'A') && (ch2 <= 'F')) {
                toReturn[i] |= (byte) (ch2 - 'A' + 10);
            }
*/
        }
        return toReturn;
    }
}
