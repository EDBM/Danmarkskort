import BFST20Project.Address;
import BFST20Project.Point;
import BFST20Project.Trie.Trie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.List;

public class TrieTest {
    Address a;
    Address b;
    Address c;
    Address d;
    Address e;
    Address f;
    Trie t;
    static Point ap = new Point(1,1);
    static Point bp = new Point(2, 2);
    static Point cp = new Point(3,3);
    static Point dp = new Point(4, 4);
    static Point ep = new Point(5,5);
    static Point fp = new Point(6, 6);

    public TrieTest(){}

    @BeforeEach
    public void setUp() {
        t = new Trie();
        Address a = new Address("Torpenvej", "23", "3050", "København");
        Address b = new Address("Torpengade", "25", "3040", "Købenby");
        Address c = new Address("Torpenbro", "28", "3590", "Kødby");
        Address d = new Address("123", "12", "123123", "123123");
        Address e = new Address("æøåÆØÅ", "æøåæøå", "æøåæøå", "æøåæøå");
        Address f = new Address("", "", "", "");
        t.insert(a,ap);
        t.insert(b,bp);
        t.insert(c,cp);
        t.insert(d,dp);
        t.insert(e,ep);
        t.insert(f,fp);
    }

    @Test
    public void testSearchTrieEmptyString(){  // tests B1 0 times and B3 false
        Assertions.assertNull(t.searchTrie(""));
    }

    @Test
    public void testSearchTrieOneChar(){  // tests B1 1 time and B2 false/true
        Assertions.assertNull(t.searchTrie("L"));  // B2 true
        Assertions.assertNull(t.searchTrie("T"));  // B2 false
    }

   @Test
   public void testSearchTrieSuccessful(){   // Tests B1 many times and B2 false and B3 true
        Assertions.assertEquals(ap, t.searchTrie("Torpenvej 23 3050 København"));
        Assertions.assertEquals(ep, t.searchTrie("æøåÆØÅ æøåæøå æøåæøå æøåæøå"));
   }

    @Test
    public void testSearchTriePrefixIn(){
        Assertions.assertNull(t.searchTrie("Torpen"));
    }

    @Test
    public void testAutocompleteShortPrefix(){
        List<String> l = t.autocomplete("Tor");
        Assertions.assertTrue(l.isEmpty());
    }

    @Test
    public void testAutocompletePrefixIn3Matches() { //tests if autocomplete on streets works
      //Test that it returns all roads where name starts with Torpen
        List<String> l = t.autocomplete("Torp");
        Assertions.assertEquals("Torpenvej 23 3050 København", String.valueOf(l.get(0)));
        Assertions.assertEquals("Torpengade 25 3040 Købenby", String.valueOf(l.get(1)));
        Assertions.assertEquals("Torpenbro 28 3590 Kødby", String.valueOf(l.get(2)));
        Assertions.assertEquals(3, l.size());
    }

    @Test
    public void testAutocomplete1Match(){
        List<String> l = t.autocomplete("æøåÆØÅ");
        Assertions.assertEquals("æøåÆØÅ æøåæøå æøåæøå æøåæøå", String.valueOf(l.get(0)));
        Assertions.assertEquals(1, l.size());
    }

    @Test
    public void testAutocompleteNoMatch(){
        List<String> l = t.autocomplete("Torben");
        Assertions.assertTrue(l.isEmpty());
    }

    @Test
    public void testAutocompleteNoMatch2(){
        List<String> l = t.autocomplete("hejsa");
        Assertions.assertTrue(l.isEmpty());
    }

    @Test
    public void TrieAutoCompleteEmptyText(){
        //Test that Trie does not 1 empty text
        Address g = new Address("      ", "  ", "  ", "   ");
        t.insert(g,new Point(7,7));
        List<String> l = t.autocomplete("   ");
        System.out.println(g);
        Assertions.assertEquals(l.size(),0);
    }

    @AfterEach
    public void cleanUp(){
        a=null;
        b=null;
        c=null;
        d=null;
        e=null;
        f=null;
    }
}
