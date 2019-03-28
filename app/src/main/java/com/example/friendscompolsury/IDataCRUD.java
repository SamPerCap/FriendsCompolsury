package com.example.friendscompolsury;

import com.example.friendscompolsury.Model.BEFriend;

import java.util.List;

public interface IDataCRUD {

    long addPerson(BEFriend p);
    void deleteAll();
    void deleteById(long id);
    void updatePerson(BEFriend p);
    List<BEFriend> getAllPersons();
    BEFriend getPersonById(long id);

}
