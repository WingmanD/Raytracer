package Raytracer;

import Raytracer.UI.RenderViewComponent;
import Raytracer.Util.HitResult;
import Raytracer.Util.Ray3D;
import Raytracer.Util.Util;
import Raytracer.Util.Vector3;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Renderer {
    private Scene scene;
    private BufferedImage render;
    private final double THRESHOLD = 0.1; // 0.00001
    private final double IOR_AIR = 1.000293;

    private List<RenderViewComponent> listeners = new ArrayList<>();

    public Renderer(Scene scene) {
        this.scene = scene;
        render = new BufferedImage(scene.camera.getWidth(), scene.camera.getHeight(), BufferedImage.TYPE_INT_RGB);
        render.setRGB(0, 0, scene.camera.getWidth(), scene.camera.getHeight(), new int[getRender().getWidth() * getRender().getHeight()], 0, scene.camera.getWidth());
    }

    public void render(int samples) {
        render = new BufferedImage(scene.camera.getWidth(), scene.camera.getHeight(), BufferedImage.TYPE_INT_RGB);
        render.setRGB(0, 0, scene.camera.getWidth(), scene.camera.getHeight(), new int[getRender().getWidth() * getRender().getHeight()], 0, scene.camera.getWidth());

        double pixelWidth = (float) 1 / (scene.camera.getHeight() / 2.0f);

        float aspectRatio = (float) scene.camera.getWidth() / scene.camera.getHeight();

        Vector3 topLeftPixel = scene.camera.getPosition().add(scene.camera.getForward());
        topLeftPixel = topLeftPixel.add(scene.camera.getRight().mul(-1).mul(aspectRatio));
        topLeftPixel = topLeftPixel.add(scene.camera.getViewUp());

        final Vector3 topLeftPixelFinal = topLeftPixel;

        for (Object3D object : scene.objects) {
            System.out.println("object: " + object.getName() + " triangles: " + ((Mesh) object).triangles.size());
        }

        int threadCount = Runtime.getRuntime().availableProcessors();
        Thread[] threads = new Thread[threadCount];
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> {
                for (int x = finalI * scene.camera.getWidth() / threadCount; x < (finalI + 1) * scene.camera.getWidth() / threadCount; x++) {
                    for (int y = 0; y < scene.camera.getHeight(); y++) {
                        Vector3 pixel = topLeftPixelFinal.add(scene.camera.getRight().mul(x * pixelWidth));
                        pixel = pixel.sub(scene.camera.getViewUp().mul(y * pixelWidth));

                        Ray3D ray = new Ray3D(scene.camera.getPosition(), pixel.sub(scene.camera.getPosition()), 1000);

                        Vector3 color = TraceRay(ray, 1, IOR_AIR);//.add(scene.backgroundColor);
                        render.setRGB(x, y, Util.Vector3ToColor(color).getRGB());

                        notifyListeners();
                    }
                }
            });
            threads[i].start();
        }

        for (int i = 0; i < threadCount; i++) {
            while (true) {
                try {
                    threads[i].join();
                    break;
                } catch (InterruptedException ignored) {
                }
            }
        }

        System.out.println("Rendering done");

        Ray3D ray = new Ray3D(scene.camera.getPosition(), scene.camera.getForward(), 1000);
        HitResult hit = scene.LineTraceSingle(ray);
        if (hit != null) {
            System.out.println("Hit: " + hit.intersection() + " " + hit.normal());
            Vector3 L = scene.lights.get(0).getPosition().sub(hit.intersection());

            Ray3D shadowRay = new Ray3D(hit.intersection().add(L.mul(0.1)), L, 1000);
            HitResult shadowHit = scene.LineTraceSingle(shadowRay);
            if (shadowHit != null) {
                System.out.println("Shadow hit: " + shadowHit.intersection() + " " + shadowHit.normal());
            } else {
                System.out.println("No shadow hit");
            }

        } else {
            System.out.println("No hit");
        }
    }

    private Vector3 TraceRay(Ray3D ray, double contribution, double IOR) {
        HitResult hitResult = scene.LineTraceSingle(ray);

        if (hitResult == null)
            return scene.backgroundColor;

        // System.out.println("hit: " + hitResult.intersection());

        Vector3 color = new Vector3(0, 0, 0);
        //Vector3 color = hitResult.material().getColor(hitResult.uv());

        for (Light light : scene.lights) {
            Vector3 L = light.getPosition().sub(hitResult.intersection());

            Ray3D shadowRay = new Ray3D(hitResult.intersection().add(L.mul(0.1)), L, 1000);
            HitResult shadowIntersection = scene.LineTraceSingle(shadowRay);
            //todo implement translucent objects here, line trace multi
            if (shadowIntersection == null || shadowIntersection.intersection().distance(hitResult.intersection()) > L.length()) {
                Vector3 lightColor = light.getColor().mul(light.getIntensity());
                Vector3 diffuse = lightColor.mul(hitResult.material().getColor(hitResult.uv())).mul(Math.max(0, L.dot(hitResult.normal())));
                color = color.add(diffuse);

                Vector3 R = hitResult.normal().mul(2 * L.dot(hitResult.normal())).sub(L);
                Vector3 specular = lightColor.mul(hitResult.material().getSpecular()).mul(Math.max(0, Math.pow(R.dot(ray.direction.mul(-1)), hitResult.material().getSpecularExponent())));
                //color = color.add(specular);
            }
        }

        contribution *= hitResult.material().getSpecularExponent() / 1000 * 0.25;
        if (contribution > THRESHOLD) {
            Vector3 U = ray.direction.mul(-1);
            Vector3 R = hitResult.normal().mul(Vector3.dot(U.mul(2), hitResult.normal())).sub(U);
            Ray3D reflectionRay = new Ray3D(hitResult.intersection(), R, 1000);
            Vector3 specularColor = TraceRay(reflectionRay, contribution, IOR).mul(hitResult.material().getSpecular());
            color = color.add(specularColor);

            if (hitResult.material().getOpacity() < 1) {
                U = ray.direction.mul(-1).normalize();
                double b = IOR / hitResult.material().getIOR();
                double alpha = Math.acos(U.dot(hitResult.normal()));
                double D = 4 * (Math.pow(b, 2) * Math.pow(Math.cos(alpha), 2) - Math.pow(b, 2) + 1);
                double a = (-2 * b * Math.cos(alpha) + Math.sqrt(D)) / 2;

                R = hitResult.normal().mul(-a).sub(U.mul(b));

                Ray3D refractionRay = new Ray3D(hitResult.intersection(), R, 1000);
                Vector3 refractionColor = TraceRay(refractionRay, contribution * hitResult.material().getOpacity(), hitResult.material().getIOR()).mul(1 - hitResult.material().getOpacity());
                color = color.add(refractionColor);
            }
        }

        return color;
    }

    public boolean saveRender(String path) {
        if (render == null) return false;

        File outputfile = new File(path);
        if (!outputfile.exists()) {
            try {
                Files.createFile(Path.of(path));
            } catch (IOException e) {
                return false;
            }
        }

        try {
            ImageIO.write(render, "png", outputfile);
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public void addListener(RenderViewComponent listener) {
        listeners.add(listener);
    }

    public void removeListener(RenderViewComponent listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (RenderViewComponent listener : listeners) {
            listener.update();
        }
    }

    public Scene getScene() {
        return scene;
    }

    public BufferedImage getRender() {
        return render;
    }

    public void setRender(BufferedImage render) {
        this.render = render;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }


}
