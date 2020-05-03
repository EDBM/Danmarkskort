package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Routeplanner.TemporaryGraph;
import BFST20Project.Trie.Trie;

import java.io.*;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class BinaryParser extends Parser {
    DirectedGraph drivableWaysGraph;
    private EnumMap<ZoomLevel, KDTree> drawables = new EnumMap<>(ZoomLevel.class);
    private Trie trie;

    public BinaryParser(InputStream inputStream) throws IOException, ClassNotFoundException {
        loadBin(inputStream);
    }

    public void loadBin(InputStream inputStream) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream (inputStream);

        List<Polylines> polylines = new ArrayList<>();
        List<MultiPolygon> multipolygons = new ArrayList<>();
        Object object = objectInputStream.readObject();

        while (true) {
            if (object instanceof TemporaryGraph) {
                TemporaryGraph graph = (TemporaryGraph) object;
                graph.init();
                graph.createTemporaryGraph();
                this.drivableWaysGraph = graph.compressedGraph();
            }
            if (object instanceof OSMWay) {
                OSMWay way = (OSMWay) object;
                Polylines poly = new Polylines(way);
                polylines.add(poly);
            }
            if (object instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) object;
                multipolygons.add(multiPolygon);
            }
            if (object instanceof Trie) {
                this.trie = (Trie) object;
            }

            try {
                object = objectInputStream.readObject();
            } catch (EOFException e){
                System.out.println("End of Stream");
                break;
            }
        }
        createDrawables(polylines, multipolygons);
    }

    private void createDrawables(List<Polylines> polylines, List<MultiPolygon> multiPolygons){
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

    @Override
    EnumMap<ZoomLevel, KDTree> getDrawables() {
        return drawables;
    }

    @Override
    float getMinLat() {
        return -55.3041f;
    }

    @Override
    float getMinLon() {
        return 8.218784f;
    }

    @Override
    float getMaxLat() {
        return -54.9264f;
    }

    @Override
    float getMaxLon() {
        return 8.491f;
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
