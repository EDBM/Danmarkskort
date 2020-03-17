package BFST20Project;

import javafx.scene.canvas.GraphicsContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiPolygon extends OSMRelation implements Drawable, Serializable {

    List<ArrayList<OSMWay>> rings;

    public MultiPolygon(long id){
        super(id);
        List<ArrayList<OSMWay>> rings = new ArrayList<>();
        RingAssignment();
    }

    @Override
    public void stroke(GraphicsContext gc) {

    }

    @Override
    public void fill(GraphicsContext gc) {

    }

    //Code build from this Algorithm: https://wiki.openstreetmap.org/wiki/Relation:multipolygon/Algorithm#Multipolygon_Creation
    public void RingAssignment(){
        ArrayList<OSMWay> unassigned = new ArrayList<>();
        ArrayList<OSMWay> assigned = new ArrayList<>();
        int ringCount = 0;

        unassigned.addAll(ways);

        ringAssignment: while(true) {
            //RA-2 Take one unassigned way and mark it assigned to the current ring
            if (!unassigned.isEmpty()) {
                assigned.add(unassigned.remove(0));

                //Check if current ring is closed
                if (assigned.get(0).first().getId() == assigned.get(assigned.size() - 1).last().getId()) {
                    if(unassigned.isEmpty()){
                        System.out.println("Ring Assignment succeeded");
                        break ringAssignment;
                    } else {
                        rings.add(assigned);
                        ringCount++;
                        continue ringAssignment;
                    }
                } else {
                    for (OSMWay way : unassigned) {
                        OSMNode ringEndNode = assigned.get(assigned.size()-1).last();
                        if(ringEndNode.getId() == way.last().getId() || ringEndNode.getId() == way.first().getId()){
                            assigned.add(way);
                            unassigned.remove(way);
                        } else {
                            System.out.println("Ring Assignment failed");
                            break ringAssignment;
                        }
                    }
                }

            }
        }





    }




}
