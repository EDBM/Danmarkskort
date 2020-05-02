package BFST20Project;

import BFST20Project.Routeplanner.DirectedGraph;

import java.util.EnumMap;
import java.util.List;

public abstract class Parser {


    abstract EnumMap<ZoomLevel, KDTree> getDrawables();

    abstract float getMinLat();
    abstract float getMinLon();
    abstract float getMaxLat();
    abstract float getMaxLon();

    abstract List<Drawable> getIslands();
    abstract DirectedGraph getDrivableWayGraph();


    abstract Trie getTrie();
}