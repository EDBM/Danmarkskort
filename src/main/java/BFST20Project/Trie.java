package BFST20Project;
// Made with help https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/trie/Trie.java

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trie implements Serializable {
    private TrieNode root;

    public Trie(){root = new TrieNode(' ');}

    public void insert(AddressParser a){
        String address = a.toString();

        if(searchTrie(address)==true)
            return;

        TrieNode current = root;
        TrieNode previousNode;

        for (char c : address.toCharArray()){
            previousNode = current;
            TrieNode child = current.getChildren(c);
            if (child!=null){
                current = child;
                child.setParent(previousNode);
            } else{
                current.children.add(new TrieNode(c));
                current = current.getChildren(c);
                current.setParent(previousNode);
            }
        }
        current.setEndOfWord(true);
    }

    private boolean searchTrie(String address) {
        TrieNode current = root;

        for (char c : address.toCharArray()) {
            if (current.getChildren(c) == null) {
                return false;
            } else {
                current = current.getChildren(c);
            }
        }
        if (current.isEndWord() == true) {
            return true;
        }
        return false;
    }

    public List<String> autocomplete(String prefix){
        TrieNode lastNode = root;
        for (int i = 0; i<prefix.length(); i++){
            lastNode = lastNode.getChildren(prefix.charAt(i));
            if(lastNode == null)
                break;
        }
        if (lastNode==null){
            return new ArrayList<>();
        }
        return lastNode.getWords();
    }
}
