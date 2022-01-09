package app.model;

import java.util.List;

public class Person {

    String firstName;
    Integer age;
    String lastName;
    List<Address> addressList;

    public Person(String firstName, Integer age, String lastName, List<Address> addressList) {
        this.firstName = firstName;
        this.age = age;
        this.lastName = lastName;
        this.addressList = addressList;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
