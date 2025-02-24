package Raytracer;

import Raytracer.Util.HitResult;
import Raytracer.Util.Ray3D;

public abstract class Object3D {
    private String name;

    public Object3D(String name) {
        this.name = name;
    }

    public abstract HitResult RayIntersection(Ray3D ray);

    public String getName() {
        return name;
    }

}
