package BFST20Project;

import java.util.Map;

public class WayTypeSetter {
    public static WayType typeFromTags(Map<String, String> tags) {
        if(tags.containsKey("highway"))
            return WayType.HIGHWAY;
        else if(tags.containsKey("building"))
            return WayType.BUILDING;
        else if(tags.containsKey("natural")){
            if(tags.get("natural").equals("water"))
                return WayType.WATER;
            if(tags.get("natural").equals("forest") || tags.get("natural").equals("scrub"))
                return WayType.FOREST;
        }
        return WayType.UNKNOWN;
    }
}
