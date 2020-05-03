package BFST20Project.Parser;

import BFST20Project.Drawable;
import BFST20Project.KDTree;
import BFST20Project.Routeplanner.DirectedGraph;
import BFST20Project.Trie.Trie;
import BFST20Project.WayType;
import BFST20Project.ZoomLevel;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public abstract class Parser {


    public abstract EnumMap<ZoomLevel, KDTree> getDrawables();

    public abstract float getMinLat();
    public abstract float getMinLon();
    public abstract float getMaxLat();
    public abstract float getMaxLon();


    public abstract DirectedGraph getDrivableWayGraph();

    public abstract Trie getTrie();

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