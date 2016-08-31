package info.sqlite.model;

/**
 * Created by magda on 31.08.16.
 */
public class User {

    public enum blood_type {
        //@TODO complete types
        ARhplus, Arhminus
    }

    private int _id;
    private String _nick;
    private String _bloodtype; //@TODO ENUM?

    //constructors
    public User() {

    }

    public User(String nick, String bloodtype) {
        this._nick = nick;
        this._bloodtype = bloodtype;
    }

    //setters
    public void set_id(int _id) { this._id = _id; }

    public void set_bloodtype(String _bloodtype) {
        this._bloodtype = _bloodtype;
    }

    public void set_nick(String _nick) {
        this._nick = _nick;
    }

    //getters
    public int get_id() { return _id; }

    public String get_bloodtype() {
        return _bloodtype;
    }

    public String get_nick() {
        return _nick;
    }
}
