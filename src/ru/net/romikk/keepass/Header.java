package ru.net.romikk.keepass;

import java.nio.ByteBuffer;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 22, 2008
 * Time: 4:08:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Header {
    public static int LENGTH = 124;
    int dwSignature1;
    int dwSignature2;
    int dwFlags;
    int dwVersion;
    byte[] aMasterSeed;
    byte[] aEncryptionIV;
    int dwGroups;
    int dwEntries;
    byte[] aContentsHash;
    byte[] aMasterSeed2;
    int dwKeyEncRounds;

    public Header(ByteBuffer bb) {
        bb.rewind();
        dwSignature1 = bb.getInt();
        dwSignature2 = bb.getInt();
        dwFlags = bb.getInt();
        dwVersion = bb.getInt();
        bb.get(aMasterSeed = new byte[16]);
        bb.get(aEncryptionIV = new byte[16]);
        dwGroups = bb.getInt();
        dwEntries = bb.getInt();
        bb.get(aContentsHash = new byte[32]);
        bb.get(aMasterSeed2 = new byte[32]);
        dwKeyEncRounds = bb.getInt();
    }

    public int getSignature1() {
        return dwSignature1;
    }

    public int getSignature2() {
        return dwSignature2;
    }

    public int getFlags() {
        return dwFlags;
    }

    public int getVersion() {
        return dwVersion;
    }

    public byte[] getMasterSeed() {
        return aMasterSeed;
    }

    public byte[] getEncryptionIV() {
        return aEncryptionIV;
    }

    public int getGroups() {
        return dwGroups;
    }

    public int getEntries() {
        return dwEntries;
    }

    public byte[] getContentsHash() {
        return aContentsHash;
    }

    public byte[] getMasterSeed2() {
        return aMasterSeed2;
    }

    public int getKeyEncRounds() {
        return dwKeyEncRounds;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("dwVersion=").append(Integer.toHexString(this.dwVersion));
        sb.append(", ");
        sb.append("dwGroups=").append(this.dwGroups);
        sb.append(", ");
        sb.append("dwEntries=").append(this.dwEntries);
        sb.append(", ");
        sb.append("dwKeyEncRounds=").append(this.dwKeyEncRounds);
        sb.append('}');
        return sb.toString();
    }
}
