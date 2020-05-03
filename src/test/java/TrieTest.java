import BFST20Project.Address;
import BFST20Project.Point;
import BFST20Project.Trie;
import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
//import org.junit.Test;

import java.util.List;

public class TrieTest {
    Address a;
    Address b;
    Address c;
    Address d;
    Address e;
    Address f;
    Trie t = new Trie();
    Point p = new Point(1,2);

    public TrieTest(){}

    @Before
    public void setUp() {
        Address a = new Address("Torpenvej", "23", "3050", "København");
        Address b = new Address("Torpengade", "25", "3040", "Købenby");
        Address c = new Address("Torpenbro", "28", "3590", "Kødby");
        Address d = new Address("123", "12", "123123", "123123");
        Address e = new Address("æøåÆØÅ", "æøåæøå", "æøåæøå", "æøåæøå");
        Address f = new Address("", "", "", "");
        t.insert(a,p);
        t.insert(b,p);
        t.insert(c,p);
        t.insert(d,p);
        t.insert(e,p);
        t.insert(f,p);
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
        t.insert(a,p);
        t.insert(b,p);
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
        Address g = new Address("      ", "  ", "  ", "   ");
        t.insert(g,p);
        List l = t.autocomplete("   ");
        System.out.println(g);
        Assertions.assertEquals(l.size(),0);
    }

    @Test
    public void TrieAutoCompletetest6(){
        //Test lower case
        //Will fail. Would be nice to implement
        setUp();
        List l = t.autocomplete("torpen");
        Assertions.assertEquals(String.valueOf(l.get(0)), "Torpenvej 23 3050 København");

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
