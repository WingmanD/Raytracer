package Raytracer;

import Raytracer.Util.Vector3;

public class Camera {
    private Vector3 position;
    private Vector3 lookAt;
    private Vector3 forward;
    private Vector3 viewUp;
    private Vector3 right;
    private double fov;
    private int width;
    private int height;

    public Camera(Vector3 position, Vector3 lookAt, double fov, int width, int height) {
        this.position = position;
        this.lookAt = lookAt;
        this.fov = fov;
        this.width = width;
        this.height = height;

        calculateForwardVector();
    }

    public Vector3 getPosition() {
        return position;
    }

    public double getFov() {
        return fov;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
        calculateForwardVector();
    }

    public Vector3 getLookAt() {
        return lookAt;
    }

    public void setLookAt(Vector3 lookAt) {
        this.lookAt = lookAt;
        calculateForwardVector();
    }

    private void calculateForwardVector() {
        this.forward = lookAt.sub(position).normalize();

        Vector3 worldUp = new Vector3(0, 0, 1);
        right = Vector3.cross(forward, worldUp).normalize();
        viewUp = Vector3.cross(right, forward).normalize();
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Vector3 getForward() {
        return forward;
    }

    public void setForward(Vector3 forward) {
        this.forward = forward;
    }

    public Vector3 getViewUp() {
        return viewUp;
    }

    public void setViewUp(Vector3 viewUp) {
        this.viewUp = viewUp;
    }

    public Vector3 getRight() {
        return right;
    }

    public void setRight(Vector3 right) {
        this.right = right;
    }
}
