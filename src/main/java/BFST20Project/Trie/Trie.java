package BFST20Project.Trie;

import BFST20Project.Address;
import BFST20Project.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
//Credit to https://www.lavivienpost.com/autocomplete-with-trie-code/?utm_source=youtube&utm_medium=description
public class Trie implements Serializable {
    private TrieNode root;

    public Trie(){root = new TrieNode(' ');}

    public void insert(Address a, Point p){
        String address = a.toString();

        TrieNode current = root;
        TrieNode previousNode;

        for (char c : address.toCharArray()){
            previousNode = current;
            TrieNode child = current.getChildren(c);
            if (child!=null){
                current = child;
                child.setParent(previousNode);
            } else{
                current.getChildren().add(new TrieNode(c));
                current = current.getChildren(c);
                current.setParent(previousNode);
            }
        }
        current.setEndOfWord(true);
        current.setPoint(p);
    }

    public Point searchTrie(String address) {
        TrieNode current = root;

        for (char c : address.toCharArray()) {          /* B1 */
            if (current.getChildren(c) == null) {       /* B2 */
                return null;
            } else {
                current = current.getChildren(c);
            }
        }
        if (current.isEndWord()) {                      /* B3 */
            return current.getPoint();
        }
        return null;
    }

    public List<String> autocomplete(String prefix) {
        TrieNode lastNode = root;
        if (prefix.length() > 3) {                      /* C1 */
            for (int i = 0; i < prefix.length(); i++) { /* C2 */
                lastNode = lastNode.getChildren(prefix.charAt(i));
                if (lastNode == null)                   /* C3 */
                    return new ArrayList<>();
            }
        }
        else{
            return new ArrayList<>();
        }
        return lastNode.getWords();
    }
}
