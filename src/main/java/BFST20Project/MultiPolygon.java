package BFST20Project;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.FillRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


//TODO Do ring grouping to determine which rings are nested inside other  (maybe not needed?)


public class MultiPolygon extends OSMRelation implements Drawable, Serializable {

    private List<List<Point>> rings = new ArrayList<>();
    private int numberOfNodes = 0;
    private float minX, minY, maxX, maxY;
    private WayType type;

    public MultiPolygon(long id){
        super(id);
    }

    @Override
    public void stroke(GraphicsContext gc) {} //NOT USED

    public void fill(GraphicsContext gc) {
        gc.setFillRule(FillRule.EVEN_ODD);
        gc.beginPath();
        for (List<Point> points : rings) {
            gc.moveTo(points.get(0).getX(), points.get(0).getY());
            for (int i = 1; i < points.size(); i++) {
                gc.lineTo(points.get(i).getX(), points.get(i).getY());
            }
        }
        gc.closePath();
        gc.stroke();
        gc.fill();
    }

    //Code build from this Algorithm: https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm#Multipolygon_Creation
    public boolean RingAssignment() {

        List<OSMWay> unassigned = new ArrayList<>();
        List<OSMWay> assigned = new ArrayList<>();

        unassigned.addAll(ways);

        ringAssignment:
        while (true) {

            //RA-2 Take one unassigned way and mark it assigned to the current ring
            if (!unassigned.isEmpty()) {
                OSMWay osmWay = unassigned.remove(0);
                assigned.add(osmWay);
                if(osmWay == null){
                    System.out.println("Ring Assignment failed, way = null " + this.id);
                    return false;
                }
                numberOfNodes += osmWay.size();


                //RA-3
                RA3:
                while (true) {

                    //Check if current ring is closed
                    if (assigned.get(0).first().getId() == assigned.get(assigned.size() - 1).last().getId()) {

                        if (unassigned.isEmpty()) {
                            addRing(assigned);
                            return true;
                        } else{
                            addRing(assigned);
                            assigned = new ArrayList<>();
                            continue ringAssignment;
                        }

                    } else {
                        for (OSMWay way : unassigned) {
                            OSMNode ringEndNode = assigned.get(assigned.size() - 1).last();

                            if (ringEndNode.getId() == way.first().getId()) {
                                assigned.add(way);
                                unassigned.remove(way);
                                numberOfNodes += way.size();
                                continue RA3;

                            } else if(ringEndNode.getId() == way.last().getId()){
                                assigned.add(way);
                                unassigned.remove(way);
                                Collections.reverse(way.getAll());
                                numberOfNodes += way.size();
                                continue RA3;

                            }
                        }
                        System.out.println("Ring Assignment failed, no ways match " + this.id);
                        return false;
                    }
                }
            }
        }
    }

    private void addRing(List<OSMWay> ways) {
        List<Point> ring = new ArrayList<>();
        for(OSMWay way : ways){
            for(OSMNode node : way.getAll()){
                ring.add(Point.fromLonLat(node.getLon(), node.getLat()));
            }
        }
        rings.add(ring);
    }

    // almost duplicate of Polylines method. Maybe merge to one method?
    public void setMinMax(){
        minX = Float.POSITIVE_INFINITY; // might give funky result if a MultiPolygon exists with no points
        minY = Float.POSITIVE_INFINITY;
        maxX = Float.NEGATIVE_INFINITY;
        maxY = Float.NEGATIVE_INFINITY;

        for(List<Point> ring : rings){
            for (Point point : ring){
                if(point.getX() < minX)
                    minX = point.getX();
                else if(point.getX() > maxX)
                    maxX = point.getX();
                if(point.getY() < minY)
                    minY = point.getY();
                else if(point.getY() > maxY)
                    maxY = point.getY();
            }
        }
    }


    @Override
    public WayType getWayType() {
        return type;
    }

    @Override
    public void setWayType(WayType type) {
        this.type = type;
    }

    @Override
    public float getMaxX() {
        return maxX;
    }

    @Override
    public float getMinX() {
        return minX;
    }

    @Override
    public float getMaxY() {
        return maxY;
    }

    @Override
    public float getMinY() {
        return minY;
    }

    public List<Point> getCoordinates() {
        List<Point> points = new ArrayList<>();

        for (List<Point> ring : rings) {
            points.addAll(ring);
        }
        return points;
    }
}
