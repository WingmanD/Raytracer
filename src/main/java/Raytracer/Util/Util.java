package Raytracer.Util;

import java.awt.*;

public class Util {
    public static double clamp(double x, double min, double max) {
        return Math.min(Math.max(x, min), max);
    }
    public static double lerp(double a, double b, double t) {
        return a + (b - a) * t;
    }

    public static Color Vector3ToColor(Vector3 v) {
        return new Color((int)(255 * clamp(v.x, 0, 1)), (int)(255 * clamp(v.y, 0, 1)), (int)(255 * clamp(v.z, 0, 1)));
    }
}
