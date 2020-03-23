package BFST20Project;
// Made with help https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/trie/Trie.java

public class Trie {
    private TrieNode root;

    public void insert(String word){
        TrieNode current = root;

        for (int i = 0; i<word.length();i++){
            current=current.getChildren().computeIfAbsent(word.charAt(i), c -> new TrieNode());
        }
        current.setEndOfWord(true);
    }

    boolean delete(String word){
        return delete(root,word,0);
    }

    boolean containsNode(String word){
        TrieNode current = root;

        for (int i = 0; i < word.length(); i++){
            char ch = word.charAt(i);
            TrieNode node = current.getChildren().get(ch);
            if(node == null){
                return false;
            }
            current = node;
        }
        return current.isEndWord();
    }

    boolean isEmpty(){
        return root == null;
    }

    private boolean delete(TrieNode current, String word, int index) {
        if (index == word.length()) {
            if (!current.isEndWord()) {
                return false;
            }
            current.setEndOfWord(false);
            return current.getChildren().isEmpty();
        }
        char ch = word.charAt(index);
        TrieNode node = current.getChildren().get(ch);
        if (node == null) {
            return false;
        }
        boolean shouldDeleteCurrentNode = delete(node, word, index + 1) && !node.isEndWord();

        if (shouldDeleteCurrentNode){
            current.getChildren().remove(ch);
            return current.getChildren().isEmpty();
        }
        return false;
    }
}
