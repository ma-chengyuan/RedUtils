package me.alan20210202.redutils.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class SliceUtils {
    private static final double EPSILON = 1e-3;

    private static boolean neq(double a, double b) {
        return Math.abs(a - b) > EPSILON;
    }

    public static List<Location> getLocationsFromSlice(Location high, Location low, Predicate<Location> filter) throws InvalidSliceException {
        if (high.getWorld() != low.getWorld())
            throw new InvalidSliceException(high, low, "Two endpoints are at two different world");
        List<Location> ret = new ArrayList<>();
        Vector delta = high.clone().subtract(low).toVector().normalize();
        if (neq(1, Math.abs(delta.getX()) + Math.abs(delta.getY()) + Math.abs(delta.getZ())))
            throw new InvalidSliceException(high, low, "A slice should be 1 * 1 * n");
        Location end = low.clone().subtract(delta);
        for (Location loc = high.clone();
             neq(0, end.clone().subtract(loc).length());
             loc.subtract(delta)) {
            if (filter.test(loc))
                ret.add(loc.clone());
        }
        return ret;
    }
}
