package Raytracer.Util;

import Raytracer.Material;

import java.util.Arrays;

public class Triangle {
    private Vertex[] vertices = new Vertex[3];
    private Vector3 normal;

    private final Vector3 e0;
    private final Vector3 e1;
    private final Vector3 e2;
    private final double D;
    private final double area;
    private Material material;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
        normal = Vector3.cross(v2.position.sub(v1.position), v3.position.sub(v1.position)).normalize();

        e0 = vertices[1].position.sub(vertices[0].position);
        e1 = vertices[2].position.sub(vertices[1].position);
        e2 = vertices[2].position.sub(vertices[0].position);
        area = 0.5 * Vector3.cross(e0, e1).length();
        D = -normal.dot(vertices[0].position);
    }

    public Triangle(Vertex[] vertices, Material material) {
        this.vertices = vertices;
        this.material = material;

        normal = vertices[0].normal.add(vertices[1].normal).add(vertices[2].normal).div(3);
        normal = normal.normalize();

        D = -normal.dot(vertices[0].position);

        e0 = vertices[1].position.sub(vertices[0].position);
        e1 = vertices[2].position.sub(vertices[1].position);
        e2 = vertices[2].position.sub(vertices[0].position);
        area = 0.5 * Vector3.cross(e0, e2).length();
    }

    public HitResult intersect(Ray3D ray) {
        double Vd = Vector3.dot(ray.direction, normal);
        if (Vd == 0) {
            //System.out.println("line is parallel to triangle");
            return null;
        }

        double t = -(normal.dot(ray.origin) + D) / Vd;
        if (t < 0 || t > ray.length) {
            // System.out.println("intersection is behind ray origin or too far, t = " + t);
            return null;
        }

        Vector3 intersection = ray.getPoint(t);

        Vector3 eP = intersection.sub(vertices[0].position);

        double t1 = Vector3.cross(e0, eP).length() * 0.5 / area;
        double t2 = Vector3.cross(e1, intersection.sub(vertices[1].position)).length() * 0.5 / area;
        double t3 = Vector3.cross(eP, e2).length() * 0.5 / area;


        if (t1 < 0 || t1 > 1 || t2 < 0 || t2 > 1 || t3 < 0 || t3 > 1 || t1 + t2 + t3 < 0.99 || t1 + t2 + t3 > 1.01) {
            //System.out.println("intersection is outside of triangle " + Arrays.toString(this.vertices) + " normal " + this.normal + ", u = " + t1 + ", v = " + t2 + ", w = " + t3 + ", intersection: " + intersection);
            return null;
        }

        //System.out.println("hit triangle: " + this.toString());
        //System.out.println("u = " + t1 + ", v = " + t2 + ", w = " + t3);
        return new HitResult(intersection, normal, material, new Vector2(t1, t2));
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }

    public Vertex[] getVertices() {
        return vertices;
    }

    public Vector3 getNormal() {
        return normal;
    }

    public void setVertices(Vertex[] vertices) {
        this.vertices = vertices;
    }

    public void setNormal(Vector3 normal) {
        this.normal = normal;
    }

    @Override
    public String toString() {
        return "Triangle{" +
                "vertices=" + Arrays.toString(vertices) +
                ", normal=" + normal +
                ", area=" + area +
                '}';
    }
}
