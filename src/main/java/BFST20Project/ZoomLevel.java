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
            case HARBOUR:
            case ISLAND:
            case WATER:
                return LEVEL_1;

            case BEACH:
            case BRIDGE:
            case DIRT:
            case FOREST:
            case GRASS:
            case HEATH:
            case MEADOW:
            case MOTORWAY:
            case NATURAL:
            case PIER:
            case RAILWAY:
            case RESIDENTIAL:
            case SCRUB:
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
            case WATERWAY:
                return LEVEL_3;
        }
        return LEVEL_3;
    }
}
