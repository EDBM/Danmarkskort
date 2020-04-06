package BFST20Project;

import java.util.*;


public class KDTree {
    private int size;
    private int bucketSize;
    private Node rootNode;
    private List<Drawable> islands = new ArrayList<>();


    public KDTree(List<Drawable> drawables) {
        for (Drawable drawable: drawables) {
            if(drawable.getWayType().equals(WayType.ISLAND)){
                islands.add(drawable);
            }
            else{
                for (Point point: drawable.getCoordinates()) {
                    insertNode(rootNode, point.getX(), point.getY(), drawable);
                }
            }
        }

    }



    public Map<WayType, List<Drawable>> query(float minX, float minY, float maxX, float maxY) {
        Map<WayType, Set<Drawable>> queriedDrawables = new EnumMap<>(WayType.class);

        query(rootNode, minX, minY, maxX, maxY, queriedDrawables);

        Map<WayType, List<Drawable>> toReturn = new EnumMap<>(WayType.class);
        toReturn.put(WayType.ISLAND, islands);

        for(WayType wayType : queriedDrawables.keySet()){
            toReturn.put(wayType, new ArrayList<>(queriedDrawables.get(wayType)));
        }

        return toReturn;
    }

    private void query(Node kNode, float minX, float minY, float maxX, float maxY, Map<WayType, Set<Drawable>> queriedDrawables) {
        if(kNode == null)
            return;

        if(kNode.height % 2 == 0){
            if(kNode.x <= maxX)
                query(kNode.rightChild, minX, minY, maxX, maxY, queriedDrawables);
            if(kNode.x >= minX)
                query(kNode.leftChild, minX, minY, maxX, maxY, queriedDrawables);
        }
        else{
            if(kNode.y <= maxY)
                query(kNode.rightChild, minX, minY, maxX, maxY, queriedDrawables);
            if(kNode.y >= minY)
                query(kNode.leftChild, minX, minY, maxX, maxY, queriedDrawables);
        }

        if(nodeInBox(kNode, minX, minY, maxX, maxY)) {
            WayType wayType = kNode.parentWay.getWayType();
            if (!queriedDrawables.containsKey(wayType))
                queriedDrawables.put(wayType, new HashSet<>());
            queriedDrawables.get(wayType).add(kNode.parentWay);
        }
    }

    private boolean nodeInBox(Node node, float minX, float minY, float maxX, float maxY) {
        return node.x >= minX && node.x <= maxX && node.y >= minY && node.y <= maxY;
    }

    public void insertNode(Node kNode, float x, float y, Drawable parentWay){
        if(rootNode == null){
            rootNode = new Node(x, y, 0, parentWay);
            return;
        }

        if(considerLeftNode(kNode, x, y)){
            if(kNode.leftChild == null) {
                kNode.leftChild = new Node(x, y, kNode.height + 1, parentWay);
                size++;
            }
            else
                insertNode(kNode.leftChild, x, y, parentWay);
        }
        else{
            if(kNode.rightChild == null) {
                kNode.rightChild = new Node(x, y, kNode.height + 1, parentWay);
                size++;
            }
            else
                insertNode(kNode.rightChild, x, y, parentWay);
        }
/*
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
        }*/
    }

    private boolean considerLeftNode(Node kNode, float x, float y) {
        if(kNode.height % 2 == 0)
            return kNode.x > x;
        else
            return kNode.y > y;
    }

    private class Node{

        Drawable parentWay;
        float x, y;
        public Node leftChild, rightChild;
        public int height;

        public Node(float x, float y, int height, Drawable parentWay) {
            this.x = x;
            this.y = y;
            this.height = height;
            this.parentWay = parentWay;
        }
    }
}





