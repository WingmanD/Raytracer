package Raytracer;

import java.util.List;

public class Mesh extends Object3D {
    private final List<Triangle> triangles;

    public Mesh(String name, List<Triangle> triangles) {
        super(name);
        this.triangles = triangles;
    }

    @Override
    public HitResult RayIntersection(Ray3D ray) {
        HitResult result = null;
        for (Triangle triangle : triangles) {
            HitResult hit = triangle.intersect(ray);
            if (hit != null) result = hit;
        }

        return result;
    }
}
