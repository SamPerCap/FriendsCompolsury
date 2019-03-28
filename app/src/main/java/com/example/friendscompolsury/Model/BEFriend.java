package com.example.friendscompolsury.Model;



import java.io.Serializable;

public class BEFriend implements Serializable {

    private int m_id;
    private String m_name;
    private String m_phone;
    private String m_email;
    private double m_locationV1;
    private double m_locationV2;
    private Boolean m_isFavorite;
    private int m_img;

    public BEFriend(int id, String name, String phone, String email, double location,
                    double locationv2 , int img) {
        this(id, name, phone, email, location, locationv2 ,false, img);
    }

    public BEFriend(int id, String name, String phone, String email, double locationv1,
                    double locationV2, Boolean isFavorite, int img) {
        m_id = id;
        m_name = name;
        m_phone = phone;
        m_email = email;
        m_img = img;
        m_locationV1 = locationv1;
        m_locationV2 = locationV2;
        m_isFavorite = isFavorite;
    }

    public String getPhone() {
        return m_phone;
    }

    public String getEmail() {
        return m_email;
    }

    public double getFriendLocationV1() {
        return m_locationV2;
    }
    public double getFriendLocationV2() {
        return m_locationV1;
    }

    public int getID(){ return m_id}

    public String getName() {
        return m_name;
    }

    public int getPicture() { return m_img; }

    public Boolean isFavorite() { return m_isFavorite; }

    @Override
    public String toString() {
        return "BEFriend{" +
                "m_name='" + m_name + '\'' +
                ", m_phone='" + m_phone + '\'' +
                ", m_email='" + m_email + '\'' +
                ", m_locationV1=" + m_locationV1 +
                ", m_locationV2=" + m_locationV2 +
                ", m_isFavorite=" + m_isFavorite +
                ", m_img=" + m_img +
                '}';
    }
}
