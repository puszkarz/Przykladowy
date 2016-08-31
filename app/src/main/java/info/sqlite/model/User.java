package info.sqlite.model;

/**
 * Created by magda on 31.08.16.
 */
public class User {

    public enum blood_type {
        //@TODO complete types
        ARhplus, Arhminus
    }

    private String _nick;
    private blood_type _bloodtype;

    //constructors
    public User() {

    }

    public User(String nick, blood_type bloodtype) {
        this._nick = nick;
        this._bloodtype = bloodtype;
    }

    //setters
    public void set_bloodtype(blood_type _bloodtype) {
        this._bloodtype = _bloodtype;
    }

    public void set_nick(String _nick) {
        this._nick = _nick;
    }

    //getters
    public blood_type get_bloodtype() {
        return _bloodtype;
    }

    public String get_nick() {
        return _nick;
    }
}
