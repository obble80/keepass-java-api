package ru.net.romikk.keepass;

/**
 * Created by IntelliJ IDEA.
 * User: romikk
 * Date: Mar 24, 2008
 * Time: 12:10:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class Entry {
    private int groupId;
    private String title;
    private String url;
    private String username;
    private String password;
    private String notes;
    private String binaryDescription;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getBinaryDescription() {
        return binaryDescription;
    }

    public void setBinaryDescription(String binaryDescription) {
        this.binaryDescription = binaryDescription;
    }
}
