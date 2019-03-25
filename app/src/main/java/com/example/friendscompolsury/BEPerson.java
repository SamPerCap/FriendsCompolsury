package com.example.friendscompolsury;

import android.widget.ImageView;

public class BEPerson {

    long m_id;
    String m_name;
    String m_phone;
    String m_address;
    float m_location;
    String m_mail;
    String m_webSite;
    String m_Birthday;
    int m_picture;

    public BEPerson(long id, String name, String phone, String address, Float location, String mail,
                    String webSite, String birthday, int picture) {
        m_id = id;
        m_name = name;
        m_phone = phone;
        m_address = address;
        m_location = location;
        m_mail = mail;
        m_webSite = webSite;
        m_Birthday = birthday;
        m_picture = picture;
    }

    public String toString() {
        return "" + m_id + ": " + m_name;
    }
}
