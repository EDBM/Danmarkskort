package BFST20Project;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {

    private HashMap<Character,TrieNode> children;
    private String content;
    private boolean isWord;
    private boolean endOfWord;

    public Map<Character,TrieNode> getChildren(){
        return children;
    }

    boolean isEndWord(){
        return endOfWord;
    }

    void setEndOfWord(boolean endOfWord){
        this.endOfWord=endOfWord;
    }
}
