package Raytracer;

public class Ray3D {
    public Vector3 origin;
    public Vector3 direction;

    public double length;

    public Ray3D(Vector3 origin, Vector3 direction, double length) {
        this.origin = origin;
        this.direction = direction;
        this.length = length;
    }

    public Vector3 getPoint(double t) {
        return origin.add(direction.mul(t));
    }
}
