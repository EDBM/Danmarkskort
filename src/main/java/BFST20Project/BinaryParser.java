package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.ShortestPath;
import BFST20Project.Routeplanner.TemporaryGraph;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BinaryParser extends Parser {


    DirectedGraph drivableWaysGraph;
    private EnumMap<ZoomLevel, KDTree> drawables = new EnumMap<>(ZoomLevel.class);
    private List<Drawable> islands = new ArrayList<>();
    private Trie trie;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        BinaryParser parser = new BinaryParser(new File("C:\\Users\\Lucas\\IdeaProjects\\BFST20Gruppe8\\src\\main\\resources\\test.bin"));
    }



    public BinaryParser(File file) throws IOException, ClassNotFoundException {
        loadDefaultBin();
    }




    public void loadDefaultBin() throws IOException, ClassNotFoundException {
        File file = new File(getClass().getClassLoader().getResource("test.bin").getFile());
        FileInputStream input = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream (input);

        ArrayList polylines = new ArrayList<Polylines>();
        ArrayList multipolygons = new ArrayList<MultiPolygon>();


        Object object = objectInputStream.readObject();
        while (true) {

            if (object instanceof OSMWay) {
                OSMWay way = (OSMWay) object;
                Polylines poly = new Polylines(way);
                polylines.add(poly);
            }

            if (object instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) object;
                multipolygons.add(multiPolygon);
            }

            if (object instanceof TemporaryGraph) {
                TemporaryGraph graph = (TemporaryGraph) object;
                graph.createTemporaryGraph();
                this.drivableWaysGraph = graph.compressedGraph();
            }

            if (object instanceof Trie) {
                this.trie = (Trie) object;
            }

            try {
                object = objectInputStream.readObject();
            } catch (EOFException e){
                System.out.println("End of Stream");
                e.printStackTrace();
                break;
            }
        }

        System.out.println("done");
        createDrawables(polylines, multipolygons);


    }


    private void createDrawables(ArrayList<Polylines> polylines, ArrayList<MultiPolygon> multiPolygons){
        Map<ZoomLevel, List<Drawable>> tempDrawableStorage = new EnumMap<>(ZoomLevel.class);

        for(ZoomLevel zoomLevel : ZoomLevel.values()){
            tempDrawableStorage.put(zoomLevel, new ArrayList<>());
        }

        for(Polylines polyline : polylines){
            rememberDrawable(polyline, tempDrawableStorage);
        }

        for(MultiPolygon multiPolygon : multiPolygons){
            rememberDrawable(multiPolygon, tempDrawableStorage);
        }

        for(ZoomLevel zoomLevel : ZoomLevel.values()) {
            drawables.put(zoomLevel, new KDTree(tempDrawableStorage.get(zoomLevel)));
        }
    }

    private void rememberDrawable(Drawable drawable, Map<ZoomLevel, List<Drawable>> tempDrawableStorage) {
        if(drawable.getWayType() == WayType.ISLAND){
            islands.add(drawable);
        }
        else if(drawable.getWayType() != WayType.BEACH) {
            ZoomLevel zoomLevel = ZoomLevel.levelForWayType(drawable.getWayType());
            tempDrawableStorage.get(zoomLevel).add(drawable);
        }
    }



    @Override
    EnumMap<ZoomLevel, KDTree> getDrawables() {
        return drawables;
    }

    @Override
    float getMinLat() {
        return 0;
    }

    @Override
    float getMinLon() {
        return 0;
    }

    @Override
    float getMaxLat() {
        return 0;
    }

    @Override
    float getMaxLon() {
        return 0;
    }

    @Override
    List<Drawable> getIslands() {
        return islands;
    }

    @Override
    DirectedGraph getDrivableWayGraph() {
        return drivableWaysGraph;
    }

    @Override
    Trie getTrie() {
        return trie;
    }

}
