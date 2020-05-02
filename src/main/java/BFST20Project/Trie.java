package BFST20Project;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Trie implements Serializable {
    private TrieNode root;

    public Trie(){root = new TrieNode(' ');}

    public void insert(AddressParser a, Point p){
        String address = a.toString();

        /*        if(searchTrie(address)==true)
            return;*/

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
        current.setPoint(p);
    }

    public Point searchTrie(String address) {
        TrieNode current = root;

        for (char c : address.toCharArray()) {
            if (current.getChildren(c) == null) {
                return null;
            } else {
                current = current.getChildren(c);
            }
        }
        if (current.isEndWord()) {
            return current.getPoint();
        }
        return null;
    }

    public List<String> autocomplete(String prefix) {
        TrieNode lastNode = root;
        if (prefix.length() > 3) {
            for (int i = 0; i < prefix.length(); i++) {
                lastNode = lastNode.getChildren(prefix.charAt(i));
                if (lastNode == null)
                    break;
            }
            if (lastNode == null) {
                return new ArrayList<>();
            }
        }
        else{
            return new ArrayList<>();
        }
            return lastNode.getWords();
        }

/*
    public List<String> searchTrie(String prefix, String house){
        TrieNode lastNode = root;
        List<String> addresses = new ArrayList<>();

        for (int i = 0; i<prefix.length(); i++) {
            lastNode = lastNode.getChildren(prefix.charAt(i));
            if (lastNode == null) {
                return new ArrayList<>();
            }
        }
        for (String address : lastNode.)
    }
    */

}
