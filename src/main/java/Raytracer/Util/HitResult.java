package Raytracer.Util;

import Raytracer.Material;

public record HitResult(Vector3 intersection, Vector3 normal, Material material, Vector2 uv) { }
