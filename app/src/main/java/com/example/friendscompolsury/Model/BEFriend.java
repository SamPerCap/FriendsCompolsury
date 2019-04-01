package com.example.friendscompolsury.Model;


import android.location.Location;

import java.io.Serializable;

public class BEFriend implements Serializable {

    public long m_id;
    private String m_name;
    private String m_address;
    private String m_phone;
    private String m_email;
    private String m_webSite;
    private String m_birthday;
    private double m_location_x;
    private double m_location_y;
    private int m_img;

    public BEFriend(long m_id, String m_name, String m_address, String m_phone, String m_email,
                    String m_webSite, String m_birthday, double m_location_x, double m_location_y,
                    int m_img) {
        this.m_id = m_id;
        this.m_name = m_name;
        this.m_address = m_address;
        this.m_phone = m_phone;
        this.m_email = m_email;
        this.m_webSite = m_webSite;
        this.m_birthday = m_birthday;
        this.m_location_x = m_location_x;
        this.m_location_y = m_location_y;
        this.m_img = m_img;
    }

    public BEFriend(String m_name, String m_address, String m_phone, String m_email,
                    String m_webSite, String m_birthday, double m_location_x,
                    double m_location_y, int m_img) {

        this.m_name = m_name;
        this.m_address = m_address;
        this.m_phone = m_phone;
        this.m_email = m_email;
        this.m_webSite = m_webSite;
        this.m_birthday = m_birthday;
        this.m_location_x = m_location_x;
        this.m_location_y = m_location_y;
        this.m_img = m_img;
    }

    public long getM_id() {
        return m_id;
    }

    public void setM_id(long m_id) {
        this.m_id = m_id;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public String getM_address() {
        return m_address;
    }

    public void setM_address(String m_address) {
        this.m_address = m_address;
    }

    public String getM_phone() {
        return m_phone;
    }

    public void setM_phone(String m_phone) {
        this.m_phone = m_phone;
    }

    public String getM_email() {
        return m_email;
    }

    public void setM_email(String m_email) {
        this.m_email = m_email;
    }

    public String getM_webSite() {
        return m_webSite;
    }

    public void setM_webSite(String m_webSite) {
        this.m_webSite = m_webSite;
    }

    public String getM_birthday() {
        return m_birthday;
    }

    public void setM_birthday(String m_birthday) {
        this.m_birthday = m_birthday;
    }

    public double getM_location_x() {
        return m_location_x;
    }
    public double getM_location_y(){
        return m_location_y;
    }

    public void setM_location(double m_location_x, double m_location_y) {
        this.m_location_x = m_location_x;
        this.m_location_y = m_location_y;
    }

    public int getM_img() {
        return m_img;
    }

    public void setM_img(int m_img) {
        this.m_img = m_img;
    }

    @Override
    public String toString() {
        return "BEFriend{" +
                "m_name='" + m_name + '\'' +
                ", m_phone='" + m_phone + '\'' +
                ", m_email='" + m_email + '\'' +
                ", m_locationV1=" + m_location_x + '\'' +
                ", m_img=" + m_img +
                '}';
    }
}
