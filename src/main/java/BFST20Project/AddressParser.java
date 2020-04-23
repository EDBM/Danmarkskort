package BFST20Project;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.regex.*;

public class AddressParser {
    private final String street, house, postcode, city;

    /*static String regex1 = "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) +(?<postcode>[0-9]{4}) +(?<city>[a-zA-ZæøåÆØÅ ]*?)$";
    static String regex2 = "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) +(?<city>[a-zA-ZæøåÆØÅ ]*?)$";
    static String regex3 = "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?) +(?<house>[0-9]+) *";
    static String regex4 = "^ *(?<street>[a-zA-ZæøåÆØÅ ]*?)";
    static Pattern pattern1 = Pattern.compile(regex1);
    static Pattern pattern2 = Pattern.compile(regex2);
    static Pattern pattern3 = Pattern.compile(regex3);
    static Pattern pattern4 = Pattern.compile(regex4);
    */
    final static String[] regex = {
            "^ *(?<street>[A-ZÆØÅa-zæøå ]+)",
            "^ *(?<street>[A-ZÆØÅa-zæøå ]+) +(?<house>[0-9]+ *[A-Za-z]*) *$",
            "^ *(?<street>[A-ZÆØÅa-zæøå ]+) +(?<house>[0-9]+ *[A-Za-z]*)[, ]*(?<postcode>[0-9]{4}) *(?<city>[A-ZÆØÅa-zæøå ]+) *$"
    };
    final static Pattern[] patterns =
            Arrays.stream(regex).map(Pattern::compile).toArray(Pattern[]::new);

    public AddressParser(String street, String house, String postcode, String city){
        this.street = street;
        this.house=house;
        this.postcode=postcode;
        this.city=city;
        /*patterns = new ArrayList<>();
        patterns.add(pattern1);
        patterns.add(pattern2);
        patterns.add(pattern3);
        patterns.add(pattern4);

         */
    }

    public static AddressParser parse(String s) {
        Builder b = new Builder();
        for (Pattern pattern : patterns) {
            Matcher match = pattern.matcher(s);
            if (match.matches()) {
                extract(match, "street", b::street);
                extract(match, "house", b::house);
                extract(match, "postcode", b::postcode);
                extract(match, "city", b::city);
                return b.build();
            }
        }
        return b.build();
    }
 /*   public static AddressParser parse(String input) {
        Builder b = new Builder();
        for (Pattern regex : patterns){
         Matcher matcher = regex.matcher(input);
         if (matcher.matches()){
             return new Builder()
                     .street(matcher.group("street"))
                     .house(matcher.group("house"))
                     .postcode(matcher.group("postcode"))
                     .city(matcher.group("city"))
                     .build();
         }
     }
     return b.build();
    }
  */

    private static void extract(Matcher m, String group, Consumer<String> c) {
        try {
            c.accept(m.group(group));
        } catch (IllegalArgumentException e) {
            System.out.println("Parser cannot parse");
        }
    }

    public String toString(){return street + " " + house + " " + postcode + " "+ city;}

    public static class Builder{
        private String street,house,postcode,city;

        public Builder street(String _street) {
            street = _street;
            return this;
        }

        public Builder house(String _house) {
            house = _house;
            return this;
        }

        public Builder postcode(String _postcode){
            postcode = _postcode;
            return this;
        }

        public Builder city(String _city){
            city = _city;
            return this;
        }

        public AddressParser build(){
            return new AddressParser(street,house,postcode,city);
        }

    }
    public String getStreet(){return street;}
    public String getHouse(){return house;}
    public String getPostcode(){return postcode;}
    public String getCity(){return city;}

}
