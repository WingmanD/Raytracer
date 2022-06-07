package Raytracer.Util;

public class Vertex {
    public Vector3 position;
    public Vector2 uv;
    public Vector3 normal;

    public Vertex(double x, double y, double z) {
        position = new Vector3(x, y, z);
        normal = new Vector3(0, 0, 0);
    }

    public Vertex(Vector3 position, Vector2 uv, Vector3 normal) {
        this.position = position;
        this.uv = uv;
        this.normal = normal;
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
