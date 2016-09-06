package edu.blooddonor.model;

public class User {

    private int _id;
    private String _nick;
    private String _bloodTypeID;

    //constructors
    public User() {

    }

    public User(String nick, String bloodTypeID) {
        this._nick = nick;
        this._bloodTypeID = bloodTypeID;
    }

    //setters
    public void set_id(int _id) { this._id = _id; }

    public void set_bloodTypeID(String _bloodTypeID) {
        this._bloodTypeID = _bloodTypeID;
    }

    public void set_nick(String _nick) {
        this._nick = _nick;
    }

    //getters
    public int get_id() { return _id; }

    public String get_bloodTypeID() {
        return _bloodTypeID;
    }

    public String get_nick() {
        return _nick;
    }

}
