package ru.net.romikk.keepass;

import java.nio.ByteBuffer;

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
    private long creationTime;
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

    public long getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(long creationTime) {
        this.creationTime = creationTime;
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
        sb.append('}');
        return sb.toString();
    }
}
