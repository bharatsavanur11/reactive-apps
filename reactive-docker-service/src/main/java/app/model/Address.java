package app.model;

public class Address {

    String streetName;
    String city;
    String state;

    public Address(String streetName, String city, String state) {
        this.streetName = streetName;
        this.city = city;
        this.state = state;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
