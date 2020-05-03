package BFST20Project;

import BFST20Project.Parser.OSMNode;
import BFST20Project.Parser.OSMRelation;
import BFST20Project.Parser.OSMWay;
import BFST20Project.Routeplanner.Vertex;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.FillRule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MultiPolygon extends OSMRelation implements Drawable, Serializable {
    private List<List<Point>> rings = new ArrayList<>();
    private float minX, minY, maxX, maxY;
    private WayType type;

    public MultiPolygon(long id){
        super(id);
    }

    @Override
    public void stroke(GraphicsContext gc, boolean shouldFill) { fill(gc); }


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

    //Code inspired by Algorithm: https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm#Multipolygon_Creation
    public boolean RingAssignment() {

        List<OSMWay> unassigned = new ArrayList<>();
        List<OSMWay> assigned = new ArrayList<>();

        unassigned.addAll(ways);

        ringAssignment:
        while (true) {

            //Ring Assignment-2 Take one unassigned way and mark it assigned to the current ring
            if (!unassigned.isEmpty()) {
                OSMWay osmWay = unassigned.remove(0);
                assigned.add(osmWay);
                if(osmWay == null){
                    System.out.println("Ring Assignment failed, way = null " + this.id);
                    return false;
                }

                //RingAssignment-3
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
                                continue RA3;

                            } else if(ringEndNode.getId() == way.last().getId()){
                                assigned.add(way);
                                unassigned.remove(way);
                                Collections.reverse(way.getAll());
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

    @Override
    public Vertex getVertex() {
        return null;
    }

    @Override
    public float distanceTo(Point from) {
        float minDist = Float.POSITIVE_INFINITY;
        for(List<Point> ring : rings){
            for(int i = 0; i < ring.size() - 1; i++) {
                float dist = from.distanceToLine(ring.get(i), ring.get(i + 1));
                if (dist < minDist)
                    minDist = dist;
            }
        }
        return minDist;
    }

    public List<Point> getCoordinates() {
        List<Point> points = new ArrayList<>();

        for (List<Point> ring : rings) {
            points.addAll(ring);
        }
        return points;
    }

    //Hopefully multipolygons dont have names
    @Override
    public String getName() {
        return null;
    }
}
