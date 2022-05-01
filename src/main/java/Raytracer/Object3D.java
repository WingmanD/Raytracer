package Raytracer;

public abstract class Object3D {
    private String name;

    public Object3D(String name) {
        this.name = name;
    }

    public abstract HitResult RayIntersection(Ray3D ray);

}
