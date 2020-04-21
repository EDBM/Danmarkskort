package BFST20Project;

import java.util.*;


public class KDTree {
    private int size;
    private int bucketSize = 100;
    private Node rootNode;


    public KDTree(List<Drawable> drawables) {
        Drawable[] drawableArray = drawables.toArray(new Drawable[0]);

        rootNode = insertIntoTree(drawableArray, 0, drawableArray.length - 1, 0);

   /*     for (Drawable drawable: drawables) {
            insertDrawable(drawable);
        }
*/
    }

    private Node insertIntoTree(Drawable[] drawables, int start, int end, int height) {
        if (end - start <= bucketSize) {
            return new Node(0, 0, height, Arrays.copyOfRange(drawables, start, end + 1));
        }

        Comparator<Drawable> comparator;
        if (height % 2 == 0) {
            comparator = new XComparator();
        } else {
            comparator = new YComparator();
        }

        // sorts smallest maxX first
        Arrays.sort(drawables, start, end, comparator); // using Quickselect could cut the running time by a factor log(n)

        int middle = start + (end - start) / 2;
        float splitMax; // All left children have maxX/maxY lower than splitMax. All right children have higher than.
        float splitMin;
        if(height % 2 == 0)
             splitMin = drawables[middle + 1].getMinX();  // The lowest minX of any right child.
        else
            splitMin = drawables[middle + 1].getMinY();

        if (height % 2 == 0) {
            splitMax = drawables[middle].getMaxX();
            for (int i = middle + 1; i <= end; i++) {
                if (drawables[i].getMinX() < splitMin)
                    splitMin = drawables[i].getMinX();
            }
        } else {
            splitMax = drawables[middle].getMaxY();
            for (int i = middle + 1; i <= end; i++) {
                if (drawables[i].getMinY() < splitMin)
                    splitMin = drawables[i].getMinY();
            }
        }
        Node node = new Node(splitMin, splitMax, height, null);

        node.leftChild = insertIntoTree(drawables, start, middle, height + 1);
        node.rightChild = insertIntoTree(drawables, middle + 1, end, height + 1);
        return node;

    }

    public Map<WayType, List<Drawable>> query(float minX, float minY, float maxX, float maxY) {
        Map<WayType, List<Drawable>> queriedDrawables = new EnumMap<>(WayType.class);

        query(rootNode, minX, minY, maxX, maxY, queriedDrawables);

        Map<WayType, List<Drawable>> toReturn = new EnumMap<>(WayType.class);

        for(WayType wayType : queriedDrawables.keySet()){
            toReturn.put(wayType, new ArrayList<>(queriedDrawables.get(wayType)));
        }

        return toReturn;
    }

    private void query(Node kNode, float minX, float minY, float maxX, float maxY, Map<WayType, List<Drawable>> queriedDrawables) {
        if(kNode == null)
            return;

        if(kNode.drawables != null) {
            for(Drawable drawable : kNode.drawables) {
                WayType wayType = drawable.getWayType();
                if (!queriedDrawables.containsKey(wayType))
                    queriedDrawables.put(wayType, new ArrayList<>());
                queriedDrawables.get(wayType).add(drawable);
            }
        }

        float min, max;

        if(kNode.height % 2 == 0) {
            min = minX;
            max = maxX;
        }
        else{
            min = minY;
            max = maxY;
        }

        if(min <= kNode.splitMax) {
            query(kNode.leftChild, minX, minY, maxX, maxY, queriedDrawables);
        }
        if(max >= kNode.splitMin){
            query(kNode.rightChild, minX, minY, maxX, maxY, queriedDrawables);
        }

    }
 /*   public void insertDrawable(Drawable drawable){
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





    private boolean nodeInBox(Node node, float minX, float minY, float maxX, float maxY) {
        return node.getX() >= minX &&
                node.getX() <= maxX &&
                node.getY() >= minY &&
                node.getY() <= maxY;
    }*/


    private class Node{
        Drawable[] drawables;  // null if not a leaf node.
        float splitMin, splitMax;
        public Node leftChild, rightChild;
        public int height;

        public Node(float splitMin, float splitMax, int height, Drawable[] drawables) {
            this.splitMin = splitMin;
            this.splitMax = splitMax;
            this.height = height;
            this.drawables = drawables;
        }

    }

    private class XComparator implements Comparator<Drawable>{
        @Override
        public int compare(Drawable d1, Drawable d2) {
            return Float.compare(d1.getMaxX(), d2.getMaxX());
        }
    }

    private class YComparator implements Comparator<Drawable>{
        @Override
        public int compare(Drawable d1, Drawable d2) {
            return Float.compare(d1.getMaxY(), d2.getMaxY());
        }
    }

}





