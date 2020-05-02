import BFST20Project.AddressParser;
import BFST20Project.Trie;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
//import org.junit.Test;

import java.util.List;

public class TrieTest {
    AddressParser a;
    AddressParser b;
    AddressParser c;
    AddressParser d;
    AddressParser e;
    AddressParser f;
    Trie t = new Trie();

    public TrieTest(){}

    @Before
            public void setUp() {
        AddressParser a = new AddressParser("Torpenvej", "23", "3050", "København");
        AddressParser b = new AddressParser("Torpengade", "25", "3040", "Købenby");
        AddressParser c = new AddressParser("Torpenbro", "28", "3590", "Kødby");
        AddressParser d = new AddressParser("123", "12", "123123", "123123");
        AddressParser e = new AddressParser("æøåÆØÅ", "æøåæøå", "æøåæøå", "æøåæøå");
        AddressParser f = new AddressParser("", "", "", "");
        t.insert(a);
        t.insert(b);
        t.insert(c);
        t.insert(d);
        t.insert(e);
        t.insert(f);
    }

    @Test
    public void TrieAutoCompleteTest1() { //tests if autocomplete on streets works
      setUp();
      //Test that it returns all roads where name starts with Torpen
        List l = t.autocomplete("Torpen");
        Assertions.assertEquals(String.valueOf(l.get(0)), "Torpenvej 23 3050 København");
        Assertions.assertEquals(String.valueOf(l.get(1)), "Torpengade 25 3040 Købenby");
        Assertions.assertEquals(String.valueOf(l.get(2)),"Torpenbro 28 3590 Kødby");
    }

    @Test
    public void TrieAutoComplete2(){ //Too short prefix
        setUp();
        //should return an empty list;
        List l = t.autocomplete("");
        Assertions.assertEquals(l.size(),0);
    }

    @Test
    public void TrieAutoCompleteTest3() {
        //tests if we only complete on street names and not cities, Test -> supposed to fail
        // destructive testing, expected to fail.
        Trie t = new Trie();
        t.insert(a);
        t.insert(b);
        List l = t.autocomplete("Torpen");
        Assertions.assertEquals(String.valueOf(l.get(0)), "Torpenvej 23 3050 København");
        Assertions.assertEquals(String.valueOf(l.get(1)), "Købengade 25 3040 Torpenby");
    }

    @Test
    public void TrieAutoComplete4(){
        //Test that non ASCII characters work
        //Failed!
        //æøå seems to cause trouble
        setUp();
        List l = t.autocomplete("æøå");
        Assertions.assertEquals(String.valueOf(l.get(0)), "æøåÆØÅ æøåæøå æøåæøå æøåæøå");
    }

    @Test
    public void TrieAutoComplete5(){
        //Test that Trie does not accept empty text
        AddressParser g = new AddressParser("      ", "  ", "  ", "   ");
        t.insert(g);
        List l = t.autocomplete("   ");
        System.out.println(g);
        Assertions.assertEquals(l.size(),0);
    }

    @After
    public void cleanUp(){
        a=null;
        b=null;
        c=null;
        d=null;
        e=null;
        f=null;
    }
}
