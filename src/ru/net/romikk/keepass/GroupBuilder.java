package ru.net.romikk.keepass;

import java.nio.ByteBuffer;

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
    private long creationTime;
    private short level;

    public void addField(short fieldType, ByteBuffer fieldData) {
        switch (fieldType) {
            case 0x0000: // Invalid or comment block, block is ignored
                break;
            case 0x0001: // Group ID, FIELDSIZE must be 4 bytes; It can be any 32-bit value except 0 and 0xFFFFFFFF
                this.groupId = fieldData.getInt();
                break;
            case 0x0002: // Group name, FIELDDATA is an UTF-8 encoded string
                this.groupName = new String(fieldData.array(), 0, fieldData.limit() - 1);
                break;
            case 0x0003: // Creation time, FIELDSIZE = 5, FIELDDATA = packed date/time
//                this.creationTime = fieldData.getLong()
                break;
            case 0x0004: // Last modification time, FIELDSIZE = 5, FIELDDATA = packed date/time
                break;
            case 0x0005: // Last access time, FIELDSIZE = 5, FIELDDATA = packed date/time
                break;
            case 0x0006: // Expiration time, FIELDSIZE = 5, FIELDDATA = packed date/time
                break;
            case 0x0007: // Image ID, FIELDSIZE must be 4 bytes
                break;
            case 0x0008: // Level, FIELDSIZE = 2
                this.level = fieldData.getShort();
                break;
            case 0x0009: // Flags, 32-bit value, FIELDSIZE = 4
                break;
            default: // Group entry terminator, FIELDSIZE must be 0
                break;
        }
    }

    public Group buildGroup() {
        Group toReturn = new Group();
        toReturn.setGroupId(this.groupId);
        toReturn.setGroupName(this.groupName);
        toReturn.setLevel(this.level);
        return toReturn;
    }
}
