package BFST20Project;

import java.io.Serializable;
import java.util.*;

public class TrieNode implements Serializable {
    private char c;
    private TrieNode parent;
    private String address;
    private boolean isWord;
    private boolean endOfWord;
    public LinkedList<TrieNode> children;

    TrieNode(char c){
        this.c=c;
        children = new LinkedList<>();
        endOfWord = false;
    }

    public TrieNode getParent() {
        return parent;
    }
    public void setParent(TrieNode parent) {
        this.parent = parent;
    }
    public String getAddress() {
        return address;
    }

    public boolean isWord() {
        return isWord;
    }

    public boolean isEndOfWord() {
        return endOfWord;
    }




    public TrieNode getChildren(char c) {
        if (children != null) {
            for (TrieNode child : children) {
                if (child.c == c) {
                    return child;
                }
            }
        }
        return null;
    }

    protected List<String> getWords(){
        List<String> words = new ArrayList<>();
        if (isEndWord()){
            words.add(toString());
        }
        if(children!=null){
            for (int i =0;i<children.size();i++){
                if(children.get(i)!=null){
                    words.addAll(children.get(i).getWords());
                }
            }
        }
        return words;
    }

    public String toString(){
        if(parent==null){
            return "";
        } else {
            return parent.toString() + new String(new char[] {c});
        }
    }

    boolean isEndWord(){
        return endOfWord;
    }

    void setEndOfWord(boolean endOfWord){
        this.endOfWord=endOfWord;
    }

}
