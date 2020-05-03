package BFST20Project.ColourSchemes;

import BFST20Project.WayType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.EnumMap;
import java.util.Map;

public class BarbieScheme extends ColorScheme {

        protected Paint defaultColor(){
            return Color.rgb(0, 0, 0, 0.0);
        }

        protected Map<WayType, Paint> createStroke() {
            EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
            resultMap.put(WayType.HIGHWAY, Color.DEEPPINK);
            resultMap.put(WayType.BUILDING, Color.ROSYBROWN);
            resultMap.put(WayType.WATER, Color.PINK);
            resultMap.put(WayType.FOREST, Color.web("#8E668F"));
            resultMap.put(WayType.MINIWAY, Color.web("#3A023B"));
            resultMap.put(WayType.DIRTROAD, Color.web("#3A023B"));
            resultMap.put(WayType.BUSWAY, Color.web("#3A023B"));
            resultMap.put(WayType.PARKING, Color.DARKRED);
            resultMap.put(WayType.SERVICE, Color.DARKRED);
            resultMap.put(WayType.SURFACE, Color.DARKRED);
            resultMap.put(WayType.ASPHALT, Color.DARKRED);
            resultMap.put(WayType.BEACH, Color.HOTPINK);
            resultMap.put(WayType.SAND, Color.HOTPINK);
            resultMap.put(WayType.WATERWAY, Color.web("#3A023B"));
            resultMap.put(WayType.AMENITY, Color.web("#3A023B"));
            resultMap.put(WayType.BRIDGE, Color.HOTPINK);
            resultMap.put(WayType.ISLAND, Color.web("#DEB9DE"));
            resultMap.put(WayType.HEATH, Color.web("#89325E"));
            resultMap.put(WayType.SCRUB, Color.PALEVIOLETRED);
            resultMap.put(WayType.MEADOW, Color.INDIANRED);
            resultMap.put(WayType.RESIDENTIAL, Color.ORANGERED);
            resultMap.put(WayType.WETLAND, Color.BLACK);
            resultMap.put(WayType.LEISURE, Color.BLUEVIOLET);
            resultMap.put(WayType.BREAKWATER, Color.DARKVIOLET);
            resultMap.put(WayType.PIER, Color.MEDIUMVIOLETRED);
            resultMap.put(WayType.TOURISM, Color.DARKVIOLET);
            resultMap.put(WayType.MOTORWAY, Color.web("#89325E"));
            resultMap.put(WayType.SECONDARY, Color.web("#89325E"));
            resultMap.put(WayType.SHORTEST_PATH, Color.web("#A50000"));
            resultMap.put(WayType.FARMLAND, Color.web("#F3C0DA"));


            return resultMap;
        }

        protected Map<WayType, Paint> createFill() {
            EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
            resultMap.put(WayType.BUILDING,  Color.ROSYBROWN);
            resultMap.put(WayType.WATER, Color.ROSYBROWN);
            resultMap.put(WayType.FOREST, Color.web("#8E668F"));
            resultMap.put(WayType.BEACH, Color.PINK);
            resultMap.put(WayType.SAND, Color.PINK);
            resultMap.put(WayType.WATERWAY, Color.BLUE);
            resultMap.put(WayType.AMENITY, Color.BLACK);
            resultMap.put(WayType.BRIDGE, Color.BLACK);
            resultMap.put(WayType.ISLAND, Color.web("#DEB9DE"));
            resultMap.put(WayType.HEATH, Color.web("#89325E"));
            resultMap.put(WayType.SCRUB, Color.PINK);
            resultMap.put(WayType.MEADOW, Color.PINK);
            resultMap.put(WayType.RESIDENTIAL, Color.PINK);
            resultMap.put(WayType.WETLAND, Color.PINK);
            resultMap.put(WayType.LEISURE, Color.PINK);
            resultMap.put(WayType.BREAKWATER, Color.PINK);
            resultMap.put(WayType.PIER, Color.PINK);
            resultMap.put(WayType.TOURISM, Color.web("#F3C0DA"));
            resultMap.put(WayType.FARMLAND, Color.web("#F3C0DA"));

            return resultMap;
        }
    }
