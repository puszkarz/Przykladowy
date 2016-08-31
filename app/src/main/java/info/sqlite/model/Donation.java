package info.sqlite.model;

import java.sql.Date;

/**
 * Created by magda on 31.08.16.
 */
public class Donation {

    public enum donation_type {
        //@TODO complete types
        WHOLE_BLOOD, PLASMA
    }

    private int _id; //@TODO primary key? autoincrement?
    private Date _date;
    private donation_type _type;
    private int _volume;
    private User _donor; //@TODO user's nick?
    private Station _station; //@TODO station's name?

    //constructors
    public Donation(){

    }

    public Donation(int id, Date date, donation_type type, int volume, User donor, Station station) {
        this._id = id;
        this._date = date;
        this._type = type;
        this._volume = volume;
        this._donor = donor;
        this._station = station;
    }

    //setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }

    public void set_type(donation_type _type) {
        this._type = _type;
    }

    public void set_volume(int _volume) {
        this._volume = _volume;
    }

    public void set_donor(User _donor) {
        this._donor = _donor;
    }

    public void set_station(Station _station) {
        this._station = _station;
    }

    //getters
    public int get_id() {
        return _id;
    }

    public Date get_date() {
        return _date;
    }

    public donation_type get_type() {
        return _type;
    }

    public int get_volume() {
        return _volume;
    }

    public User get_donor() {
        return _donor;
    }

    public Station get_station() {
        return _station;
    }
}
