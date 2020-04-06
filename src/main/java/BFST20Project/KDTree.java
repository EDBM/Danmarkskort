package BFST20Project;

import java.util.*;


public class KDTree {


    private int size;
    private int height;
    private int bucketSize;
    private Node rootNode;


    public KDTree(ArrayList<Drawable> drawables) {


        for (Drawable drawable: drawables) {
            for (Point point: drawable.getCoordinates()) {
                insertNode(rootNode, point.getX(), point.getY(), drawable);
            }
        }



    }



    public List<Drawable> query(float minX, float minY, float maxX, float maxY) {




        return null;
    }

    public void insertNode(Node kNode, float x, float y, Drawable parentWay){

        if(rootNode == null){
            rootNode = new Node(x, y, 0, parentWay);
        }

        if(this.height % 2 == 0){
            if(kNode.x < x){
                if(kNode.rightChild != null){
                    insertNode(kNode.rightChild, x, y, parentWay);
                }else{
                    kNode.rightChild = new Node(x, y, kNode.height+1, parentWay);
                }
            }

            else{
                if(kNode.leftChild != null){
                    insertNode(kNode, x, y, parentWay);
                }else{
                    kNode.leftChild = new Node(x, y, kNode.height+1, parentWay);
                }
            }
        }
        else{
            if(kNode.y < y){
                if(kNode.rightChild != null){
                    insertNode(kNode.rightChild, x, y, parentWay);
                }else{
                    kNode.rightChild = new Node(x, y, kNode.height+1, parentWay);
                }
            }

            else{
                if(kNode.leftChild != null){
                    insertNode(kNode.leftChild, x, y, parentWay);
                }else{
                    kNode.leftChild = new Node(x, y, kNode.height+1, parentWay);
                }
            }
        }
    }


}





class Node{

    Drawable parentWay;
    float x, y;
    public Node leftChild, rightChild;
    public int height;

    public Node(float x, float y, int height, Drawable parentWay) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.parentWay = way;
    }




/*
    @Override
    public int compareTo(Node other) {
        if(this.height % 2 == 0){
            if(this.x < other.x) return -1;
            if(this.x > other.x) return 1;
        }
        else{
            if(this.y < other.y) return -1;
            if(this.y > other.y) return 1;
        }
        return 0;
    }


 */
}
