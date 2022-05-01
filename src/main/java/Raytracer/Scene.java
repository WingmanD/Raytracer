package Raytracer;

import java.util.ArrayList;
import java.util.List;

public class Scene {
    public List<Object3D> objects;
    public List<Light> lights;

    public Camera camera = new Camera(new Vector3(0), new Vector3(0, 0, 0), 90, 1920, 1080);

    public Scene() {
        objects = new ArrayList<>();
        lights = new ArrayList<>();
    }

    public Scene(List<Object3D> objects, List<Light> lights) {
        this.objects = objects;
        this.lights = lights;
    }

    public void addObject(Object3D object) {
        objects.add(object);
    }

    public void addLight(Light light) {
        lights.add(light);
    }

    public HitResult LineTraceSingle(Ray3D ray) {
        for (Object3D object : objects) {
            HitResult hitResult = object.RayIntersection(ray);
            if (hitResult != null) return hitResult;
        }

        return null;
    }
}
