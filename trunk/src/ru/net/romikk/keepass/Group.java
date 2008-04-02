package ru.net.romikk.keepass;

import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 22, 2008
 * Time: 5:07:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class Group {
    private int groupId;
    private String groupName;
    private Calendar creationTime;
    private Calendar lastModificationTime;
    private Calendar lastAccessTime;
    private Calendar expirationTime;
    private short level;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Calendar getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Calendar creationTime) {
        this.creationTime = creationTime;
    }

    public Calendar getLastModificationTime() {
        return lastModificationTime;
    }

    public void setLastModificationTime(Calendar lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public Calendar getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Calendar lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Calendar getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Calendar expirationTime) {
        this.expirationTime = expirationTime;
    }

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("id=").append(Integer.toHexString(this.groupId));
        sb.append(", ");
        sb.append("lvl=").append(this.level);
        sb.append(", ");
        sb.append("name=").append(this.groupName);
        sb.append(", ");
        sb.append("created=[").append(this.creationTime.getTime()).append(']');
        sb.append('}');
        return sb.toString();
    }
}
