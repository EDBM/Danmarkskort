import BFST20Project.AddressParser;
import BFST20Project.Trie;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
//import org.junit.Test;

import java.util.List;

public class TrieTest {

    @Test
    public void TrieAutoCompleteTest() { //tests if autocomplete on streets works
        Trie t = new Trie();
        AddressParser a = new AddressParser("Torpenvej", "23", "3050", "København");
        AddressParser b = new AddressParser("Torpengade", "25", "3040", "Købenby");
        t.insert(a);
        t.insert(b);
        List l = t.autocomplete("Torpen");
        Assertions.assertEquals(String.valueOf(l.get(0)), "Torpenvej 23 3050 København");
        Assertions.assertEquals(String.valueOf(l.get(1)), "Torpengade 25 3040 Købenby");
    }

    @Test
    public void TrieAutoCompleteTest2() {
        //tests if we only complete on street names and not cities, Test -> supposed to fail
        // destructive testing, expected to fail.
        Trie t = new Trie();
        AddressParser a = new AddressParser("Torpenvej", "23", "3050", "København");
        AddressParser b = new AddressParser("Købengade", "25", "3040", "Torpenby");
        t.insert(a);
        t.insert(b);
        List l = t.autocomplete("Torpen");
        Assertions.assertEquals(String.valueOf(l.get(0)), "Torpenvej 23 3050 København");
        Assertions.assertEquals(String.valueOf(l.get(1)), "Købengade 25 3040 Torpenby");
    }
}
