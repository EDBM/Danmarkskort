package BFST20Project.Trie;

import BFST20Project.Point;

import java.io.Serializable;
import java.util.*;

//Credit to https://www.lavivienpost.com/autocomplete-with-trie-code/?utm_source=youtube&utm_medium=description
public class TrieNode implements Serializable {
    private char c;
    private TrieNode parent;
    private Point point;
    private boolean endOfWord;

    private List<TrieNode> children;

    TrieNode(char c){
        this.c=c;
        children = new LinkedList<>();
        endOfWord = false;
    }

    public void setParent(TrieNode parent) {
        this.parent = parent;
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

    public List<String> getWords(){
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

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint(){
        return point;
    }

    public List<TrieNode> getChildren() { return children; }
}
