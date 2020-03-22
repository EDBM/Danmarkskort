package BFST20Project;

import java.util.Arrays;
import java.util.regex.*;

public class AddressParser {
    private final String street, house, postcode, city;
    /*static String[] regex = {
     "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) +(?<postcode>[0-9]{4}) +(?<city>[a-zA-ZæøåÆØÅ ]*?)$",
     "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) +(?<city>[a-zA-ZæøåÆØÅ ]*?)$",
     "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) *",
     "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?)"};
     */
    static String regex =
            "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) +(?<postcode>[0-9]{4}) +(?<city>[a-zA-ZæøåÆØÅ ]*?)$";
    static Pattern pattern = Pattern.compile((regex));


    public AddressParser(String street, String house, String postcode, String city){
        this.street = street;
        this.house=house;
        this.postcode=postcode;
        this.city=city;
    }

    public static String[] parse(String input) {
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                return new Builder()
                        .street(matcher.group("street"))
                        .house(matcher.group("house"))
                        .postcode(matcher.group("postcode"))
                        .city(matcher.group("city"))
                        .build();
            } else throw new IllegalArgumentException(("Parser can't parse: " + input));
        }


    public String toString(){return street + " " + house + " " + postcode + " "+ city;}

    public static class Builder{
        private String street,house,postcode,city;

        public Builder street(String street) {
            this.street = street;
            return this;
        }

        public Builder house(String house) {
            this.house = house;
            return this;
        }

        public Builder postcode(String postcode){
            this.postcode = postcode;
            return this;
        }

        public Builder city(String city){
            this.city = city;
            return this;
        }

        public String[] build(){
            String[] address = new String[4];
            address[0] = street;
            address[1] = house;
            address[2] = postcode;
            address[3] = city;
            return address;
        }
    }
}
