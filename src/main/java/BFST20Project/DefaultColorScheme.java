package BFST20Project;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.EnumMap;
import java.util.Map;

public class DefaultColorScheme extends ColorScheme {
    protected Paint defaultColor(){
        return Color.RED;
    }

    protected Map<WayType, Paint> createStroke() {
        EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
        resultMap.put(WayType.HIGHWAY, Color.BLACK);
        resultMap.put(WayType.BUILDING, Color.BEIGE);
        resultMap.put(WayType.WATER, Color.BLUE);
        resultMap.put(WayType.FOREST, Color.GREEN);

        return resultMap;
    }

    protected Map<WayType, Paint> createFill() {
        EnumMap<WayType, Paint> resultMap = new EnumMap<>(WayType.class);
        resultMap.put(WayType.BUILDING, Color.BEIGE);
        resultMap.put(WayType.WATER, Color.BLUE);
        resultMap.put(WayType.FOREST, Color.GREEN);

        return resultMap;
    }
}
