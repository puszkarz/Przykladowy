package info.sqlite.model;

import android.location.Location;

public class Station {
    private int _id;
    private String _name;
    private String _address;
    private int _coordinate_x;
    private int _coordinate_y;

    //constructors
    public Station() {

    }

    public Station(String name,String address, int x, int y) {
        this._name = name;
        this._address = address;
        this._coordinate_x = x;
        this._coordinate_y = y;
    }

    public Station(int id, String name,String address, int x, int y) {
        this._id = id;
        this._name = name;
        this._address = address;
        this._coordinate_x = x;
        this._coordinate_y = y;
    }

    //setters
    public void set_id(int _id) { this._id = _id; }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public void set_coordinate_x(int _coordinate_x) {
        this._coordinate_x = _coordinate_x;
    }

    public void set_coordinate_y(int _coordinate_y) {
        this._coordinate_y = _coordinate_y;
    }

    //getters
    public int get_id() { return _id; }

    public String get_name() {
        return _name;
    }

    public String get_address() {
        return _address;
    }

    public int get_coordinate_x() {
        return _coordinate_x;
    }

    public int get_coordinate_y() {
        return _coordinate_y;
    }
}
