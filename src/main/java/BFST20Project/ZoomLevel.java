package BFST20Project;

public enum ZoomLevel {
    LEVEL_1,
    LEVEL_2,
    LEVEL_3
    ;

    public static ZoomLevel levelForWayType(WayType wayType){
        switch (wayType){
            case BREAKWATER:
            case COASTLINE:
            case FOREST:
            case HARBOUR:
            case ISLAND:
            case MOTORWAY:
            case WATER:
            case WATERWAY:
            case FARMLAND:
            case NATURAL:
                return LEVEL_1;

            case BEACH:
            case SAND:
            case BRIDGE:
            case DIRT:
            case GRASS:
            case HEATH:
            case MEADOW:
            case PIER:
            case RAILWAY:
            case RESIDENTIAL:
            case SCRUB:
            case SECONDARY:
            case WETLAND:
                return LEVEL_2;

            case AMENITY:
            case ASPHALT:
            case BICYCLE:
            case BUILDING:
            case BUSWAY:
            case CROSSING:
            case CYCLEWAY:
            case DIRTROAD:
            case HIGHWAY:
            case LANDUSE:
            case LEISURE:
            case MINIWAY:
            case PARKING:
            case PITCH:
            case SERVICE:
            case SIDEWALK:
            case SUBWAY:
            case SURFACE:
            case TOURISM:
            case UNKNOWN:
                return LEVEL_3;
        }
        return LEVEL_3;
    }
}
