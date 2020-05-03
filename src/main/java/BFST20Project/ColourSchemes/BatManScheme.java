package BFST20Project.ColourSchemes;

import BFST20Project.WayType;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.EnumMap;
import java.util.Map;

public class BatManScheme extends ColorScheme {

    protected Paint defaultColor(){
        return Color.rgb(0, 0, 0, 0.0);
    }

    protected Map<WayType, Paint> createStroke() {
        EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
        resultMap.put(WayType.HIGHWAY, Color.WHITE);
        resultMap.put(WayType.BUILDING, Color.WHITE);
        resultMap.put(WayType.WATER, Color.BLACK);
        resultMap.put(WayType.FOREST, Color.BLACK);
        resultMap.put(WayType.MINIWAY, Color.WHITE);
        resultMap.put(WayType.DIRTROAD, Color.WHITE);
        resultMap.put(WayType.BUSWAY, Color.WHITE);
        resultMap.put(WayType.PARKING, Color.BLACK);
        resultMap.put(WayType.SERVICE, Color.BLACK);
        resultMap.put(WayType.SURFACE, Color.BLACK);
        resultMap.put(WayType.ASPHALT, Color.WHITE);
        resultMap.put(WayType.BEACH, Color.BLACK);
        resultMap.put(WayType.SAND, Color.BLACK);
        resultMap.put(WayType.WATERWAY, Color.BLACK);
        resultMap.put(WayType.AMENITY, Color.BLACK);
        resultMap.put(WayType.BRIDGE, Color.WHITE);
        resultMap.put(WayType.ISLAND, Color.BLACK);
        resultMap.put(WayType.HEATH, Color.BLACK);
        resultMap.put(WayType.SCRUB, Color.BLACK);
        resultMap.put(WayType.MEADOW, Color.BLACK);
        resultMap.put(WayType.RESIDENTIAL, Color.BLACK);
        resultMap.put(WayType.WETLAND, Color.BLACK);
        resultMap.put(WayType.LEISURE, Color.BLACK);
        resultMap.put(WayType.BREAKWATER, Color.BLACK);
        resultMap.put(WayType.PIER, Color.BLACK);
        resultMap.put(WayType.TOURISM, Color.BLANCHEDALMOND);
        resultMap.put(WayType.MOTORWAY, Color.WHITE);
        resultMap.put(WayType.SECONDARY, Color.WHITE);
        resultMap.put(WayType.SHORTEST_PATH, Color.RED);
        resultMap.put(WayType.FARMLAND, Color.GREY);

        return resultMap;
    }

    protected Map<WayType, Paint> createFill() {
        EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
        resultMap.put(WayType.BUILDING,  Color.WHITE);
        resultMap.put(WayType.WATER, Color.BLACK);
        resultMap.put(WayType.FOREST, Color.web("#33343F"));
        resultMap.put(WayType.BEACH, Color.web("#5F5F62"));
        resultMap.put(WayType.SAND, Color.BLACK);
        resultMap.put(WayType.WATERWAY, Color.BLACK);
        resultMap.put(WayType.AMENITY, Color.BLACK);
        resultMap.put(WayType.BRIDGE, Color.BLACK);
        resultMap.put(WayType.ISLAND, Color.web("#5F5F62"));
        resultMap.put(WayType.HEATH, Color.web("#5F5F62"));
        resultMap.put(WayType.SCRUB, Color.web("#5F5F62"));
        resultMap.put(WayType.MEADOW, Color.web("#5F5F62"));
        resultMap.put(WayType.RESIDENTIAL, Color.web("#5F5F62"));
        resultMap.put(WayType.WETLAND, Color.web("#5F5F62"));
        resultMap.put(WayType.LEISURE, Color.web("#5F5F62"));
        resultMap.put(WayType.BREAKWATER, Color.web("#5F5F62"));
        resultMap.put(WayType.PIER, Color.web("#5F5F62"));
        resultMap.put(WayType.TOURISM, Color.web("#5F5F62"));
        resultMap.put(WayType.FARMLAND, Color.web("#5F5F62"));

        return resultMap;
    }
}
