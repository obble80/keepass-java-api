package ru.net.romikk.keepass;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 22, 2008
 * Time: 5:54:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class Utils {

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
