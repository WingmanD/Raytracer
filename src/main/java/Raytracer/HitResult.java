package Raytracer;

public record HitResult(Vector3 intersection, Vector3 normal, Material material, Vector2 uv) { }
