package Raytracer;

import Raytracer.Util.HitResult;
import Raytracer.Util.Ray3D;
import Raytracer.Util.Triangle;

import java.util.List;

public class Mesh extends Object3D {
    public final List<Triangle> triangles;

    public Mesh(String name, List<Triangle> triangles) {
        super(name);
        this.triangles = triangles;
    }

    @Override
    public HitResult RayIntersection(Ray3D ray) {
        HitResult result = null;
        for (Triangle triangle : triangles) {
            HitResult hit = triangle.intersect(ray);
            if (hit != null) {
                if (result == null) result = hit;
                else if (hit.intersection().distance(ray.origin) < result.intersection().distance(ray.origin)) result = hit;
            }
        }

        return result;
    }
}
