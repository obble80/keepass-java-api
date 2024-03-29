package ru.net.romikk.keepass;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 23, 2008
 * Time: 10:53:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class GroupBuilder {
    private int groupId;
    private String groupName;
    private Date creationTime;
    private Date lastModificationTime;
    private Date lastAccessTime;
    private Date expirationTime;
    private short level;

    private byte[] packedDate = new byte[5];

    public void readField(short fieldType, int fieldSize, ByteBuffer data) throws UnsupportedEncodingException {
        switch (fieldType) {
            case 0x0000: // Invalid or comment block, block is ignored
                break;
            case 0x0001: // Group ID, FIELDSIZE must be 4 bytes; It can be any 32-bit value except 0 and 0xFFFFFFFF
                assertFieldSize(4, fieldSize);
                this.groupId = data.getInt();
                break;
            case 0x0002: // Group name, FIELDDATA is an UTF-8 encoded string
                byte[] fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.groupName = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x0003: // Creation time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                data.get(packedDate);
                this.creationTime = Utils.unpackDate(packedDate);
                break;
            case 0x0004: // Last modification time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                data.get(packedDate);
                this.lastModificationTime = Utils.unpackDate(packedDate);
                break;
            case 0x0005: // Last access time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                data.get(packedDate);
                this.lastAccessTime = Utils.unpackDate(packedDate);
                break;
            case 0x0006: // Expiration time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                data.get(packedDate);
                this.expirationTime = Utils.unpackDate(packedDate);
                break;
            case 0x0007: // Image ID, FIELDSIZE must be 4 bytes
                assertFieldSize(4, fieldSize);
                data.getInt();
                break;
            case 0x0008: // Level, FIELDSIZE = 2
                assertFieldSize(2, fieldSize);
                this.level = data.getShort();
                break;
            case 0x0009: // Flags, 32-bit value, FIELDSIZE = 4
                assertFieldSize(4, fieldSize);
                data.getInt();
                break;
            default: // Group entry terminator, FIELDSIZE must be 0
                break;
        }
    }

    private static void assertFieldSize(int expected, int provided) {
        if (expected != provided) {
            throw new IllegalArgumentException("Invalid field size: " + provided);
        }
    }

    public Group buildGroup() {
        Group toReturn = new Group();
        toReturn.setGroupId(this.groupId);
        toReturn.setGroupName(this.groupName);
        toReturn.setLevel(this.level);
        toReturn.setCreationTime(this.creationTime);
        toReturn.setLastAccessTime(this.lastAccessTime);
        toReturn.setLastModificationTime(this.lastModificationTime);
        toReturn.setExpirationTime(this.expirationTime);
        return toReturn;
    }
}
