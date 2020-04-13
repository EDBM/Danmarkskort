package BFST20Project;

import java.util.*;


public class KDTree {
    private int size;
    private int bucketSize;
    private Node rootNode;


    public KDTree(){

    }

    public KDTree(List<Drawable> drawables) {
        for (Drawable drawable: drawables) {
            insertDrawable(drawable);
        }

    }

    public void insertDrawable(Drawable drawable){
        for (Point point: drawable.getCoordinates()) {
            insertNode(rootNode, point, drawable);
        }
    }

    private void insertNode(Node kNode, Point coordinates, Drawable parentWay){
        if(rootNode == null){
            rootNode = new Node(coordinates, 0, parentWay);
            return;
        }

        if(considerLeftNode(kNode, coordinates)){
            if(kNode.leftChild == null) {
                kNode.leftChild = new Node(coordinates, kNode.height + 1, parentWay);
                size++;
            }
            else
                insertNode(kNode.leftChild, coordinates, parentWay);
        }
        else{
            if(kNode.rightChild == null) {
                kNode.rightChild = new Node(coordinates, kNode.height + 1, parentWay);
                size++;
            }
            else
                insertNode(kNode.rightChild, coordinates, parentWay);
        }
    }

    private boolean considerLeftNode(Node kNode, Point coordinates) {
        if(kNode.height % 2 == 0)
            return kNode.getX() > coordinates.getX();
        else
            return kNode.getY() > coordinates.getY();
    }


    public Map<WayType, List<Drawable>> query(float minX, float minY, float maxX, float maxY) {
        Map<WayType, Set<Drawable>> queriedDrawables = new EnumMap<>(WayType.class);

        query(rootNode, minX, minY, maxX, maxY, queriedDrawables);

        Map<WayType, List<Drawable>> toReturn = new EnumMap<>(WayType.class);

        for(WayType wayType : queriedDrawables.keySet()){
            toReturn.put(wayType, new ArrayList<>(queriedDrawables.get(wayType)));
        }

        return toReturn;
    }

    private void query(Node kNode, float minX, float minY, float maxX, float maxY, Map<WayType, Set<Drawable>> queriedDrawables) {
        if(kNode == null)
            return;

        if(kNode.height % 2 == 0){
            if(kNode.getX() <= maxX)
                query(kNode.rightChild, minX, minY, maxX, maxY, queriedDrawables);
            if(kNode.getX() >= minX)
                query(kNode.leftChild, minX, minY, maxX, maxY, queriedDrawables);
        }
        else{
            if(kNode.getY() <= maxY)
                query(kNode.rightChild, minX, minY, maxX, maxY, queriedDrawables);
            if(kNode.getY() >= minY)
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
        return node.getX() >= minX &&
                node.getX() <= maxX &&
                node.getY() >= minY &&
                node.getY() <= maxY;
    }


    private class Node{
        Drawable parentWay;
        Point coordinates;
        public Node leftChild, rightChild;
        public int height;

        public Node(Point coordinates, int height, Drawable parentWay) {
            this.coordinates = coordinates;
            this.height = height;
            this.parentWay = parentWay;
        }

        public float getX(){
            return coordinates.getX();
        }

        public float getY(){
            return coordinates.getY();
        }
    }
}





