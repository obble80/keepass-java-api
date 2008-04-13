package ru.net.romikk.keepass;

import java.math.BigInteger;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 24, 2008
 * Time: 12:10:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class Entry {
    private BigInteger uuid;
    private int groupId;
    private String title;
    private String url;
    private String username;
    private String password;
    private String notes;
    private Date creationTime;
    private Date lastModificationTime;
    private Date lastAccessTime;
    private Date expirationTime;
    private String binaryDescription;
    private byte[] binaryData;

    public BigInteger getUuid() {
        return uuid;
    }

    protected void setUuid(BigInteger uuid) {
        this.uuid = uuid;
    }

    public int getGroupId() {
        return groupId;
    }

    protected void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    protected void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    protected void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    protected void setPassword(String password) {
        this.password = password;
    }

    public String getNotes() {
        return notes;
    }

    protected void setNotes(String notes) {
        this.notes = notes;
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

    public String getBinaryDescription() {
        return binaryDescription;
    }

    protected void setBinaryDescription(String binaryDescription) {
        this.binaryDescription = binaryDescription;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    protected void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        sb.append("gid=").append(Integer.toHexString(this.groupId));
        sb.append(", ");
        sb.append("title=").append(this.title);
        sb.append(", ");
        sb.append("url=").append(this.url);
        sb.append(", ");
        sb.append("created=[").append(this.creationTime).append(']');
        sb.append('}');
        return sb.toString();
    }

}
