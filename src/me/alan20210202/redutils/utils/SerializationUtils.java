package me.alan20210202.redutils.utils;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SerializationUtils {
    public static ArrayList<Map<String, Object>> serializeLocsAndStates(List<Location> locs, List<Boolean> states) {
        ArrayList<Map<String, Object>> ret = new ArrayList<>();
        for (int i = 0; i < locs.size(); i++) {
            HashMap<String, Object> tmp = new HashMap<>();
            tmp.put("state", states.get(i));
            tmp.put("loc", locs.get(i).serialize());
            ret.add(tmp);
        }
        return ret;
    }

    public static void deserializeLocsAndStates(ArrayList<Map<String, Object>> data, List<Location> locs, List<Boolean> states) {
        for (Map<String, Object> input : data) {
            locs.add(Location.deserialize((Map<String, Object>)input.get("loc")));
            states.add((Boolean)input.get("state"));
        }
    }
}
