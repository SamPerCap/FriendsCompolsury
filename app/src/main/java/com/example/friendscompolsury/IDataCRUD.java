package com.example.friendscompolsury;

public interface IDataCRUD {

    long addPerson(BEPerson p);
    void deleteAll();
    void deleteById(long id);
    void updatePerson(BEPerson p);
    List<BEPerson> getAllPersons();
    BEPerson getPersonById(long id);

}
