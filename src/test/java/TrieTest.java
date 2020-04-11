import BFST20Project.AddressParser;
import BFST20Project.Trie;
import org.junit.Test;

import java.util.List;

public class TrieTest {

    @Test
    public static void TrieAutoCompleteTest(String[] args) {
        Trie t = new Trie();
        AddressParser a = new AddressParser("Torpenvej", "23", "3050", "København");
        AddressParser b = new AddressParser("Torpengade", "25", "3040", "Købenby");
        t.insert(a);
        t.insert(b);
        List l = t.autocomplete("Torpen");
        for (int i = 0; i < l.size(); i++) {
            System.out.println(l.get(i));
        }
    }
}
