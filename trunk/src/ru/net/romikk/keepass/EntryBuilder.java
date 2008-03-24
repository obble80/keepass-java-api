package ru.net.romikk.keepass;

import java.nio.ByteBuffer;
import java.io.UnsupportedEncodingException;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 24, 2008
 * Time: 12:10:19 AM
 * To change this template use File | Settings | File Templates.
 */
public class EntryBuilder {
    private int groupId;
    private String title;
    private String url;
    private String username;
    private String password;
    private String notes;
    private String binaryDescription;

    public void readField(short fieldType, int fieldSize, ByteBuffer data) throws UnsupportedEncodingException {
        byte[] fieldData;
        switch (fieldType) {
            case 0x0000: // Invalid or comment block, block is ignored
                break;
            case 0x0001: // UUID, uniquely identifying an entry, FIELDSIZE must be 16
                assertFieldSize(16, fieldSize);
                data.getLong();
                data.getLong();
                break;
            case 0x0002: // Group ID, identifying the group of the entry, FIELDSIZE = 4; It can be any 32-bit value except 0 and 0xFFFFFFFF
                assertFieldSize(4, fieldSize);
                this.groupId = data.getInt();
                break;
            case 0x0003: // Image ID, identifying the image/icon of the entry, FIELDSIZE = 4
                assertFieldSize(4, fieldSize);
                data.getInt();
                break;
            case 0x0004: // Title of the entry, FIELDDATA is an UTF-8 encoded string
                fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.title = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x0005: // URL string, FIELDDATA is an UTF-8 encoded string
                fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.url = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x0006: // UserName string, FIELDDATA is an UTF-8 encoded string
                fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.username = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x0007: // Password string, FIELDDATA is an UTF-8 encoded string
                fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.password = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x0008: // Notes string, FIELDDATA is an UTF-8 encoded string
                fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.notes = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x0009: // Creation time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                for (int i = 0; i < 5; i++) data.get();
                break;
            case 0x000A: // Last modification time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                for (int i = 0; i < 5; i++) data.get();
                break;
            case 0x000B: // Last access time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                for (int i = 0; i < 5; i++) data.get();
                break;
            case 0x000C: // Expiration time, FIELDSIZE = 5, FIELDDATA = packed date/time
                assertFieldSize(5, fieldSize);
                for (int i = 0; i < 5; i++) data.get();
                break;
            case 0x000D: // Binary description UTF-8 encoded string
                fieldData = new byte[fieldSize];
                data.get(fieldData);
                this.binaryDescription = new String(fieldData, 0, fieldData.length - 1, "utf8");
                break;
            case 0x000E: // Binary data
                for (int i = 0; i < fieldSize; i++) data.get();
                break;
            default: // Entry terminator, FIELDSIZE must be 0
                break;
        }
    }

    private static void assertFieldSize(int expected, int provided) {
        if (expected != provided) {
            throw new IllegalArgumentException("Invalid field size: " + provided);
        }
    }

    public Entry buildEntry() {
        Entry toReturn = new Entry();
        toReturn.setGroupId(this.groupId);
        toReturn.setTitle(this.title);
        toReturn.setUrl(this.url);
        toReturn.setUsername(this.username);
        toReturn.setPassword(this.password);
        toReturn.setNotes(this.notes);
        toReturn.setBinaryDescription(this.binaryDescription);
        return toReturn;
    }

}
