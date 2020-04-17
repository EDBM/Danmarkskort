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

    private ArrayList<ArrayList<OSMWay>> rings = new ArrayList<>();
    private int numberOfNodes = 0;
    private WayType type;
    private float[][] coordinates;

    public MultiPolygon(long id){
        super(id);
    }

    @Override
    public void stroke(GraphicsContext gc) {} //NOT USED


    @Override
    public void fill(GraphicsContext gc){
        fill(gc, rings);
    }

    public void fill(GraphicsContext gc, ArrayList<ArrayList<OSMWay>> rings) {
        gc.setFillRule(FillRule.EVEN_ODD);
        float[][] coordinates = new float[2][numberOfNodes];
        int count = 0;
        gc.beginPath();
        for (ArrayList<OSMWay> list : rings) {
            gc.moveTo(0.56f * list.get(0).get(0).getLon(), -list.get(0).get(0).getLat());
            for (OSMWay way : list) {
                for(int i = 0; i < way.size(); i++){
                    coordinates[0][count] = 0.56f * way.get(i).getLon();
                    coordinates[1][count] = -way.get(i).getLat();

                    gc.lineTo(coordinates[0][count], coordinates[1][count]);
                    count++;
                }
            }
        }
        gc.closePath();
        gc.stroke();
        gc.fill();
    }

    //Code build from this Algorithm: https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm#Multipolygon_Creation
    public boolean RingAssignment() {

        ArrayList<OSMWay> unassigned = new ArrayList<>();
        ArrayList<OSMWay> assigned = new ArrayList<>();

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
                            rings.add(assigned);
                            return true;
                        } else{
                            rings.add(assigned);
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



    @Override
    public WayType getWayType() {
        return type;
    }

    @Override
    public void setWayType(WayType type) {
        this.type = type;
    }

    public List<Point> getCoordinates() {

        ArrayList<Point> points = new ArrayList<>();

        for (ArrayList<OSMWay> list : rings) {
            for (OSMWay way : list) {
                for (OSMNode node: way.getAll()) {
                    points.add(new Point(0.56f * node.getLon(), -node.getLat()));
                }
            }
        }
        return points;
    }
}
