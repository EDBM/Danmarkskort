package BFST20Project;

import java.util.Map;

public enum WayType{
  UNKNOWN,
  AMENITY,
  ASPHALT,
  BEACH,
  BICYCLE,
  BRIDGE,
  BUILDING,
  BUSWAY,
  COASTLINE,
  CROSSING,
  CYCLEWAY,
  DIRT,
  DIRTROAD,
  FOREST,
  GRASS,
  HARBOUR,
  HIGHWAY,
  LANDUSE,
  LEISURE,
  MINIWAY,
  MOTORWAY,
  NATURAL,
  PARKING,
  RAILWAY,
  SERVICE,
  SIDEWALK,
  SUBWAY,
  SURFACE,
  WATER,
  WATERWAY
  ;

    public static WayType typeFromTags(Map<String, String> tags) {
      if(tags.containsKey("highway"))
        return HIGHWAY;
      else if(tags.containsKey("building"))
        return BUILDING;
      else if(tags.containsKey("natural")){
        if(tags.get("natural").equals("water"))
          return WATER;
        if(tags.get("natural").equals("forest") || tags.get("natural").equals("scrub"))
          return FOREST;
      }
      return UNKNOWN;
    }

}
