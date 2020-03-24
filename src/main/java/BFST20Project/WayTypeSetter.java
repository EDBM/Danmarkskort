package BFST20Project;

import java.util.Map;

public class WayTypeSetter {
    public static WayType typeFromTags(Map<String, String> tags) {
        for(String k : tags.keySet()) {
            switch (k) {
                case "building":
                    switch (tags.get(k)) {
                        case "yes":
                            return WayType.BUILDING;
                    }
                    break;

                case "place":
                    switch (tags.get(k)) {
                        case "island":
                            return WayType.ISLAND;
                    }


                case "highway":
                    switch (tags.get(k)) {
                        case "trunk":
                        case "motorway":
                        case "motorway_link":
                        case "trunk_link":
                        case "motorway_junction":
                        case "primary":
                        case "primary_link":
                            return WayType.MOTORWAY;

                        case "secondary":
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
                    break;

                case "natural":
                    switch (tags.get(k)) {
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
                    }
                    break;
                case "cycleway":
                    return WayType.HIGHWAY;


                case "parking":
                    return WayType.PARKING;



                case "service":
                    return WayType.SERVICE;

                case "surface":
                    switch (tags.get(k)) {
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
                    break;

                case "landuse":
                    switch (tags.get(k)) {
                        case "grass":
                        case "farmland":
                            return WayType.NATURAL;

                        case "harbour":
                            return WayType.HARBOUR;

                        case "forest":
                            return WayType.FOREST;

                        case "meadow":
                            return WayType.MEADOW;

                        case "residential":
                            return WayType.RESIDENTIAL;
                    }
                    break;


                case "leisure":
                    return WayType.LEISURE;

                case "tourism":
                    return WayType.TOURISM;

                case "sidewalk":
                    return WayType.SIDEWALK;


                case "shop":
                    return WayType.BUILDING;


                case "bridge":
                    return WayType.BRIDGE;

                case "network":
                    return WayType.SUBWAY;

                case "water":
                    switch (tags.get(k)) {
                        case "lake":
                            return WayType.WATER;
                    }
                    break;

                case "waterway":
                    return WayType.WATERWAY;


                case "man_made":
                    switch (tags.get(k)) {
                        case "breakwater":
                            return WayType.BREAKWATER;
                        case "pier":
                            return WayType.PIER;
                    }
                    break;

                case "amenity":
                    return WayType.AMENITY;
            }

        }

        return WayType.UNKNOWN;
    }

}
