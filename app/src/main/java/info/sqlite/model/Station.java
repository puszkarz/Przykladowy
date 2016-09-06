package info.sqlite.model;

public class Station {
    private int _id;
    private String _name;
    private String _address;
    private double _latitude;
    private double _longitude;

    public Station(String name, String address, double x, double y) {
        this._name = name;
        this._address = address;
        this._latitude = x;
        this._longitude = y;
    }

    public Station(int id, String name, String address, double x, double y) {
        this(name, address, x, y);
        this._id = id;
    }

    //setters
    public void set_id(int _id) { this._id = _id; }

    public void set_name(String _name) {
        this._name = _name;
    }

    public void set_address(String _address) {
        this._address = _address;
    }

    public void set_latitude(double _latitude) {
        this._latitude = _latitude;
    }

    public void set_longitude(double _longitude) {
        this._longitude = _longitude;
    }

    //getters
    public int get_id() { return _id; }

    public String get_name() {
        return _name;
    }

    public String get_address() {
        return _address;
    }

    public double get_latitude() {
        return _latitude;
    }

    public double get_longitude() {
        return _longitude;
    }

    public String toString() {
//        return get_name();
        return "St.id: " + Integer.toString(get_id())
                + ", n: " + get_name()
                + ", a: " + get_address()
                + ", lat: " + get_latitude()
                + ", long: " + get_longitude();
    }
}
