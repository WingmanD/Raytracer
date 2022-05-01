package Raytracer;

public class Light {
    public Vector3 position;
    public Vector3 color;
    public double intensity;

    public Light(Vector3 position, Vector3 color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

}
