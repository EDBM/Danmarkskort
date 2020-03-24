package BFST20Project;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.EnumMap;
import java.util.Map;

public class DefaultColorScheme extends ColorScheme {

    protected Paint defaultColor(){
        return Color.rgb(0, 0, 0, 0.0);
    }

    protected Map<WayType, Paint> createStroke() {
        EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
        resultMap.put(WayType.HIGHWAY, Color.web("#FFFFFF"));
        resultMap.put(WayType.BUILDING, Color.web("#EDEDED"));
        resultMap.put(WayType.WATER, Color.web("#AADAFE"));
        resultMap.put(WayType.FOREST, Color.web("#C9EFBB"));
        resultMap.put(WayType.MINIWAY, Color.web("#FFFFFF"));
        resultMap.put(WayType.DIRTROAD, Color.web("#FFFFFF"));
        resultMap.put(WayType.BUSWAY, Color.web("#FFFFFF"));
        resultMap.put(WayType.PARKING, Color.web("#FFFFFF"));
        resultMap.put(WayType.SERVICE, Color.web("#FFFFFF"));
        resultMap.put(WayType.SURFACE, Color.web("#FFFFFF"));
        resultMap.put(WayType.ASPHALT, Color.web("#FFFFFF"));
        resultMap.put(WayType.BEACH, Color.web("#EDEDED"));
        resultMap.put(WayType.WATERWAY, Color.web("#EDEDED"));
        resultMap.put(WayType.AMENITY, Color.web("#EDEDED"));
        resultMap.put(WayType.LEISURE, Color.web("#EDEDED")) ;
        resultMap.put(WayType.BRIDGE, Color.web("#FFFFFF"));



        return resultMap;
    }

    protected Map<WayType, Paint> createFill() {
        EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
        resultMap.put(WayType.BUILDING, Color.web("#EDEDED"));
        resultMap.put(WayType.WATER, Color.web("#AADAFE"));
        resultMap.put(WayType.FOREST, Color.web("#C9EFBB"));
        resultMap.put(WayType.MINIWAY, Color.BLACK);
        resultMap.put(WayType.DIRTROAD, Color.BLACK);
        resultMap.put(WayType.BUSWAY, Color.BLACK);
        resultMap.put(WayType.PARKING, Color.BLACK);
        resultMap.put(WayType.SERVICE, Color.BLACK);
        resultMap.put(WayType.SURFACE, Color.BLACK);
        resultMap.put(WayType.ASPHALT, Color.BLACK);
        resultMap.put(WayType.BEACH, Color.BLACK);
        resultMap.put(WayType.WATERWAY, Color.BLACK);
        resultMap.put(WayType.AMENITY, Color.BLACK);
        resultMap.put(WayType.LEISURE, Color.BLACK);
        resultMap.put(WayType.BRIDGE, Color.BLACK);



        return resultMap;
    }
}
