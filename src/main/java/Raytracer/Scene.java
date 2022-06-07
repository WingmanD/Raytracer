package Raytracer;

import Raytracer.Util.HitResult;
import Raytracer.Util.Ray3D;
import Raytracer.Util.Vector3;

import java.util.ArrayList;
import java.util.List;

public class Scene {

    public String name;
    public List<Object3D> objects;
    public List<Light> lights;

    public Camera camera = new Camera(new Vector3(0), new Vector3(0, 0, 0), 90, 1280, 720);
    public Vector3 backgroundColor = new Vector3(0.1);

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
        double distance = Double.MAX_VALUE;
        HitResult result = null;
        for (Object3D object : objects) {
            HitResult hitResult = object.RayIntersection(ray);
            if (hitResult != null && ray.origin.distance(hitResult.intersection()) < distance) {
                distance = ray.origin.distance(hitResult.intersection());
                result = hitResult;
            }
        }

        return result;
    }

    public List<HitResult> LineTraceMulti(Ray3D ray) {
        List<HitResult> results = new ArrayList<>();
        for (Object3D object : objects) {
            HitResult hitResult = object.RayIntersection(ray);
            if (hitResult != null) {
                results.add(hitResult);
            }
        }

        return results;
    }

    public List<Object3D> getObjects() {
        return objects;
    }

    public void setObjects(List<Object3D> objects) {
        this.objects = objects;
    }

    public List<Light> getLights() {
        return lights;
    }

    public void setLights(List<Light> lights) {
        this.lights = lights;
    }

    public Camera getCamera() {
        return camera;
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    public Vector3 getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Vector3 backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
