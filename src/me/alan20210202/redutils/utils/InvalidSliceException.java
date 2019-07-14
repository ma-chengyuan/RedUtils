package me.alan20210202.redutils.utils;

import org.bukkit.Location;

public class InvalidSliceException extends Exception {
    private Location high, low;

    InvalidSliceException(Location high, Location low, String reason) {
        super("Invalid Slice from " + high + " to " + low + ": " + reason);
    }
}
