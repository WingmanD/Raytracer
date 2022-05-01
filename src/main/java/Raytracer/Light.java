package Raytracer;

import Raytracer.Util.Vector3;

public class Light {
    public Vector3 position;
    public Vector3 color;
    public double intensity;

    public Light(Vector3 position, Vector3 color, double intensity) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getColor() {
        return color;
    }

    public void setColor(Vector3 color) {
        this.color = color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}
