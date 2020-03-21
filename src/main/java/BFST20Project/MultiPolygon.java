package BFST20Project;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


//TODO Do ring grouping to determine which rings are nested inside other rings


public class MultiPolygon extends OSMRelation implements Drawable, Serializable {

    private ArrayList<ArrayList<OSMWay>> rings = new ArrayList<>();;

    public MultiPolygon(long id){
        super(id);
        //RingAssignment();
    }

    @Override
    public void stroke(GraphicsContext gc) {

        System.out.println("Number of rings: " + rings.size());
        for (ArrayList<OSMWay> list : rings) {
            System.out.println("Number of ways in list: " + list.size());
            for (OSMWay way: list) {
                //fill(gc, way);
                //way.printWay();
                //gc.beginPath();
                //trace(gc, getCoordinates(way));
                //gc.stroke();


                new Polylines(way).stroke(gc);
                System.out.println(way.getId());
            }
        }
    }

    //@Override
    public void fill(GraphicsContext gc, OSMWay way) {
        double[][] coords = getCoordinates(way);
        gc.setFill(Color.DARKRED);
        gc.fillPolygon(coords[0], coords[1], coords[0].length);
    }

    public double[][] getCoordinates(OSMWay way){
        double[][] coordinates = coordinates = new double[2][way.size()];
        for(int i = 0; i < way.size(); i++){
            coordinates[0][i] = 0.56f * way.get(i).getLon();
            coordinates[1][i] = -way.get(i).getLat();
        }
        return coordinates;
    }


    public void trace(GraphicsContext gc, double[][] coordinates){
        double[] startCoord = coordinates[0];
        gc.moveTo(startCoord[0], startCoord[1]);
        for (int i = 1; i<coordinates.length ; i++){
            gc.lineTo(coordinates[i][0], coordinates[i][1]);
        }
    }




    //TODO Check if current ring is a valid geometry
    //Code build from this Algorithm: https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm#Multipolygon_Creation
    public boolean RingAssignment() {

        ArrayList<OSMWay> unassigned = new ArrayList<>();
        ArrayList<OSMWay> assigned = new ArrayList<>();
        int ringCount = 0;

        unassigned.addAll(ways);
        System.out.println("number of ways in relation: " + unassigned.size());

        ringAssignment:
        while (true) {

            //RA-2 Take one unassigned way and mark it assigned to the current ring
            if (!unassigned.isEmpty()) {
                OSMWay osmWay = unassigned.remove(0);
                assigned.add(osmWay);
                if(osmWay == null){
                    System.out.println("Ring Assignment failed " + this.id);
                    return false;
                }


                //RA-3
                RA3:
                while (true) {
                    //System.out.println("RA-3");


                    //Check if current ring is closed
                    if (assigned.get(0).first().getId() == assigned.get(assigned.size() - 1).last().getId()) {
                        if (unassigned.isEmpty()) {
                            System.out.println("id of way added: " + assigned.get(0).getId() + " with a size: " + assigned.size());
                            rings.add(assigned);
                            ringCount++;
                            System.out.println("number of ways in assigned: " + assigned.size());
                            System.out.println("Ring Assignment succeeded 1");
                            return true;
                        }
                        //if the current ring is a valid geometry TODO make it actually check if it is valid
                        else if (true) {
                            if (!unassigned.isEmpty()) {
                                rings.add(assigned);
                                ringCount++;
                                System.out.println("id of way added: " + assigned.get(0).getId());
                                assigned = new ArrayList<>();
                                System.out.println("Ring " + ringCount + " done, moving on to the next");
                                continue ringAssignment;
                            } else {
                                System.out.println("number of ways in unassigned: " + unassigned.size());
                                System.out.println("Ring Assignment succeeded 2");
                                return true;
                            }
                        }
                    } else {
                        for (OSMWay way : unassigned) {
                            OSMNode ringEndNode = assigned.get(assigned.size() - 1).last();
                            OSMNode ringStartNode = assigned.get(0).first();


                            if (ringEndNode.getId() == way.first().getId()) {
                                System.out.println("add way to ring");
                                assigned.add(way);
                                unassigned.remove(way);
                                continue RA3;
                            } else if(ringEndNode.getId() == way.last().getId()){
                                System.out.println("add reversed way to ring");
                                unassigned.remove(way);
                                Collections.reverse(way.getAll());
                                assigned.add(way);
                                continue RA3;
                            }
                        }
                        System.out.println("Ring Assignment failed " + this.id);
                        return false;
                    }
                }
            }
        }
    }


    public void ringGrouping(){
        Boolean[][] nested = new Boolean[rings.size()][rings.size()];
        int polygonCounter = 0;




    }




}
