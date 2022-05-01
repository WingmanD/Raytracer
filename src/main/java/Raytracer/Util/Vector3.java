package Raytracer.Util;

public class Vector3 {
    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Vector3 v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    public Vector3() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Vector3(int i) {
        this.x = i;
        this.y = i;
        this.z = i;
    }

    public Vector3(double i) {
        this.x = i;
        this.y = i;
        this.z = i;
    }

    public double dot(Vector3 v) {
        return this.x * v.x + this.y * v.y + this.z * v.z;
    }

    public Vector3 cross(Vector3 v) {
        return new Vector3(this.y * v.z - this.z * v.y, this.z * v.x - this.x * v.z, this.x * v.y - this.y * v.x);
    }

    public static Vector3 cross(Vector3 v1, Vector3 v2) {
        return v1.cross(v2);
    }

    public Vector3 add(Vector3 v) {
        Vector3 out = new Vector3(this);

        out.x += v.x;
        out.y += v.y;
        out.z += v.z;

        return out;
    }

    public Vector3 sub(Vector3 v) {
        Vector3 out = new Vector3(this);

        out.x -= v.x;
        out.y -= v.y;
        out.z -= v.z;

        return out;
    }

    public Vector3 mul(double d) {
        Vector3 out = new Vector3(this);

        out.x *= d;
        out.y *= d;
        out.z *= d;

        return out;
    }

    public Vector3 mul(Vector3 v) {
        Vector3 out = new Vector3(this);

        out.x *= v.x;
        out.y *= v.y;
        out.z *= v.z;

        return out;
    }

    public Vector3 div(double d) {
        Vector3 out = new Vector3(this);

        out.x /= d;
        out.y /= d;
        out.z /= d;

        return out;
    }

    public Vector3 rotate(Vector3 rotator) {
        Vector3 out = new Vector3(this);

        out.x = Math.cos(rotator.z) * Math.cos(rotator.y);
        out.y = Math.sin(rotator.z) * Math.cos(rotator.y);
        out.z = Math.sin(rotator.y);

        return out;
    }

    public Vector3 normalize() {
        double length = Math.sqrt(x * x + y * y + z * z);
        return new Vector3(x / length, y / length, z / length);
    }

    public double length() {
        return Math.sqrt(x * x + y * y + z * z);
    }

    public String toString() {
        return "Vector3(" + x + ", " + y + ", " + z + ")";
    }
}
