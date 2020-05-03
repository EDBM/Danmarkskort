package BFST20Project;

public class Address {
    private final String street, house, postcode, city;

    public Address(String street, String house, String postcode, String city){
        this.street = street;
        this.house = house;
        this.postcode = postcode;
        this.city = city;
    }

    public String toString(){
        return street + " " + house + " " + postcode + " "+ city;
    }
}
