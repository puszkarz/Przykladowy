package info.sqlite.model;

public class User {

    private int _id;
    private String _nick;
    private String _bloodType;

    //constructors
    public User() {

    }

    public User(String nick, String bloodType) {
        this._nick = nick;
        this._bloodType = bloodType;
    }

    //setters
    public void set_id(int _id) { this._id = _id; }

    public void set_bloodType(String _bloodType) {
        this._bloodType = _bloodType;
    }

    public void set_nick(String _nick) {
        this._nick = _nick;
    }

    //getters
    public int get_id() { return _id; }

    public String get_bloodType() {
        return _bloodType;
    }

    public String get_nick() {
        return _nick;
    }

}
