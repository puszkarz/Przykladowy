package edu.blooddonor.model;

public class Donation {

    private int _id;
    private String _date;
    private String _type;
    private int _volume;
    private int _blood_volume;
    private int _user_id;
    private int _station_id;

    public Donation() {

    }

    public Donation(String date, String type, int volume, int blood_volume, int user_id, int station_id) {
        this._date = date;
        this._type = type;
        this._volume = volume;
        this._blood_volume = blood_volume;
        this._user_id = user_id;
        this._station_id = station_id;
    }

    //setters
    public void set_id(int _id) {
        this._id = _id;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public void set_type(String _type) {
        this._type = _type;
    }

    public void set_volume(int _volume) {
        this._volume = _volume;
    }

    public void set_blood_volume(int _blood_volume) { this._blood_volume = _blood_volume; }

    public void set_user_id(int _user_id) { this._user_id = _user_id; }

    public void set_station_id(int _station_id) { this._station_id = _station_id;}

    //getters
    public int get_id() {
        return _id;
    }

    public String get_date() {
        return _date;
    }

    public String get_type() {
        return _type;
    }

    public int get_volume() {
        return _volume;
    }

    public int get_blood_volume() { return _blood_volume; }

    public int get_user_id() { return _user_id; }

    public int get_station_id() { return _station_id; }

    public String toString() {
        return "Donation id: " + Integer.toString(get_id())
                + ", stID: " + Integer.toString(get_station_id())
                + ", uID: " + Integer.toString(get_user_id());
    }
}
