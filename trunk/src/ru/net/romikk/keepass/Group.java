package ru.net.romikk.keepass;

import java.util.Date;

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
    private Date creationTime;
    private Date lastModificationTime;
    private Date lastAccessTime;
    private Date expirationTime;
    private short level;

    public int getGroupId() {
        return groupId;
    }

    protected void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    protected void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    protected void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getLastModificationTime() {
        return lastModificationTime;
    }

    protected void setLastModificationTime(Date lastModificationTime) {
        this.lastModificationTime = lastModificationTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    protected void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    protected void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public short getLevel() {
        return level;
    }

    protected void setLevel(short level) {
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
        sb.append("created=[").append(this.creationTime).append(']');
        sb.append('}');
        return sb.toString();
    }
}
