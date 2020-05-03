package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Trie.Trie;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Parser {


    abstract EnumMap<ZoomLevel, KDTree> getDrawables();

    abstract float getMinLat();
    abstract float getMinLon();
    abstract float getMaxLat();
    abstract float getMaxLon();


    abstract DirectedGraph getDrivableWayGraph();

    abstract Trie getTrie();

    protected List<Drawable> islands = new ArrayList<>();

    public List<Drawable> getIslands(){
        return islands;
    }

    protected void rememberDrawable(Drawable drawable, Map<ZoomLevel, List<Drawable>> tempDrawableStorage) {
        if(drawable.getWayType() == WayType.ISLAND){
            islands.add(drawable);
        }
        else if(drawable.getWayType() != WayType.BEACH) {
            ZoomLevel zoomLevel = ZoomLevel.levelForWayType(drawable.getWayType());
            tempDrawableStorage.get(zoomLevel).add(drawable);
        }
    }
}