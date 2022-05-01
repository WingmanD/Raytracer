package Raytracer;

public class Triangle {
    private Vertex[] vertices = new Vertex[3];
    private Vector3 normal;
    private Material material;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        vertices[0] = v1;
        vertices[1] = v2;
        vertices[2] = v3;
        normal = Vector3.cross(v2.position.sub(v1.position), v3.position.sub(v1.position));
    }

    public Triangle(Vertex[] vertices, Material material) {
        this.vertices = vertices;
        this.material = material;
    }

    public HitResult intersect(Ray3D ray) {
        Vector3 e1 = vertices[1].position.sub(vertices[0].position);
        Vector3 e2 = vertices[2].position.sub(vertices[0].position);
        Vector3 s1 = ray.direction.cross(e2);
        double divisor = s1.dot(e1);
        if (divisor == 0) {
            return null;
        }
        double invDivisor = 1.0 / divisor;
        Vector3 s = ray.origin.sub(vertices[0].position);
        double b1 = s.dot(s1) * invDivisor;
        if (b1 < 0 || b1 > 1) {
            return null;
        }
        Vector3 s2 = s.cross(e1);
        double b2 = ray.direction.dot(s2) * invDivisor;
        if (b2 < 0 || b1 + b2 > 1) {
            return null;
        }
        double t = e2.dot(s2) * invDivisor;
        if (t < 0 || t > ray.length) {
            return null;
        }

        Vector3 intersection = ray.getPoint(t);
        Vector2 uv = getUV(intersection);

        return new HitResult(intersection, normal, material, uv);
    }

    private Vector2 getUV(Vector3 intersection) {
        Vector3 v1 = vertices[1].position.sub(vertices[0].position);
        Vector3 v2 = vertices[2].position.sub(vertices[0].position);
        Vector3 v3 = intersection.sub(vertices[0].position);
        double u = v1.dot(v3) / v1.dot(v2);
        double v = v2.dot(v3) / v1.dot(v2);
        return new Vector2(u, v);
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
}
