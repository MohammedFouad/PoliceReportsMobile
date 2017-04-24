package com.keniobyte.bruino.minsegapp.models;

/**
 * @author bruino
 * @version 23/04/17.
 */

public class PoliceBoss {
    private int id;
    private String name;
    private String pathProfile;
    private String phone;

    public PoliceBoss(int id, String name, String pathProfile, String phone) {
        this.id = id;
        this.name = name;
        this.pathProfile = pathProfile;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPathProfile() {
        return pathProfile;
    }

    public void setPathProfile(String pathProfile) {
        this.pathProfile = pathProfile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
