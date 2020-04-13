package BFST20Project;

import java.util.Map;

public class WayTypeSetter {
    public static WayType typeFromTags(Map<String, String> tags) {

            if(tags.containsKey("building"))
                return WayType.BUILDING;
            
            switch (tags.getOrDefault("place", "")) {
                case "island":
                    return WayType.ISLAND;
            }


            switch (tags.getOrDefault("highway", "")) {
                case "trunk":
                case "motorway":
                case "motorway_link":
                case "trunk_link":
                case "motorway_junction":
                case "primary":
                case "primary_link":
                    return WayType.MOTORWAY;

                case "secondary":
                    return WayType.SECONDARY;
                case "tertiary":
                case "unclassified":
                case "service":
                case "residential":
                case "secondary_link":
                case "tertiary_link":
                case "mini_roundabout":
                case "crossing":
                case "turning_circle":
                case "passing_place":
                case "rest_area":
                case "cycleway":
                    return WayType.HIGHWAY;

                case "pedestrian":
                case "living_street":
                case "footway":
                    return WayType.MINIWAY;

                case "track":
                case "path":
                    return WayType.DIRTROAD;

                case "bus_guideway":
                    return WayType.BUSWAY;
            }

            switch (tags.getOrDefault("natural", "")) {
                case "scrub":
                    return WayType.SCRUB;
                case "water":
                    return WayType.WATER;
                case "coastline":
                    return WayType.COASTLINE;
                case "beach":
                    return WayType.BEACH;
                case "heath":
                    return WayType.HEATH;
                case "wetland":
                    return WayType.WETLAND;
                case "sand":
                    return WayType.SAND;
            }

            if(tags.containsKey("cycleway"))
                return WayType.HIGHWAY;


            if(tags.containsKey("parking"))
                return WayType.PARKING;



            if(tags.containsKey("service"))
                return WayType.SERVICE;

            switch (tags.getOrDefault("surface", "")) {
                case "asphalt":
                    return WayType.ASPHALT;
                case "grass":
                    return WayType.NATURAL;

                case "concrete":
                    return WayType.ASPHALT;

                case "paved":
                    return WayType.ASPHALT;

                case "sand":
                    return WayType.BEACH;
            }

            switch (tags.getOrDefault("landuse", "")) {
                case "grass":
                    return WayType.NATURAL;

                case "farmland":
                    return WayType.FARMLAND;

                case "harbour":
                    return WayType.HARBOUR;

                case "forest":
                    return WayType.FOREST;

                case "meadow":
                    return WayType.MEADOW;

                case "residential":
                    return WayType.RESIDENTIAL;
            }

            if(tags.containsKey("leisure"))
                return WayType.LEISURE;

            if(tags.containsKey("tourism"))
                return WayType.TOURISM;

            if(tags.containsKey("sidewalk"))
                return WayType.SIDEWALK;


            if(tags.containsKey("shop"))
                return WayType.BUILDING;


            if(tags.containsKey("bridge"))
                return WayType.BRIDGE;

            if(tags.containsKey("network"))
                return WayType.SUBWAY;

            switch (tags.getOrDefault("water", "")) {
                case "lake":
                    return WayType.WATER;
            }

            if(tags.containsKey("waterway"))
                return WayType.WATERWAY;


            switch (tags.getOrDefault("man_made", "")) {
                case "breakwater":
                    return WayType.BREAKWATER;
                case "pier":
                    return WayType.PIER;
            }

            if(tags.containsKey("amenity"))
                return WayType.AMENITY;


        return WayType.UNKNOWN;
    }

}
