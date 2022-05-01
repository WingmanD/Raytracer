package Raytracer;

public class Camera {
    private Vector3 position;
    private Vector3 rotation;
    private Vector3 forward;
    private Vector3 viewUp;
    private Vector3 right;
    private double fov;
    private int width;
    private int height;

    public Camera(Vector3 position, Vector3 rotation, double fov, int width, int height) {
        this.position = position;
        this.rotation = rotation;
        this.fov = fov;
        this.width = width;
        this.height = height;

        forward = new Vector3(0, 0, -1);
        forward.rotate(rotation);
        forward = forward.normalize();

        Vector3 worldUp = new Vector3(0, 0, 1);
        right = Vector3.cross(forward, worldUp);
        viewUp = Vector3.cross(right, forward);
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
    }

    public Vector3 getRotation() {
        return rotation;
    }

    public void setRotation(Vector3 rotation) {
        this.rotation = rotation;
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
