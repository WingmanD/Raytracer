package Raytracer;

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

public class Renderer {
    private Scene scene;
    private BufferedImage render;
    private final double THRESHOLD = 0.00001;
    private final double IOR_AIR = 1.000293;

    public Renderer(Scene scene) {
        this.scene = scene;
        render = new BufferedImage(scene.camera.getWidth(), scene.camera.getHeight(), BufferedImage.TYPE_INT_RGB);
        render.setRGB(0, 0, scene.camera.getWidth(), scene.camera.getHeight(), new int[getRender().getWidth() * getRender().getHeight()], 0, scene.camera.getWidth());
    }

    public void render(int samples) {
        render = new BufferedImage(scene.camera.getWidth(), scene.camera.getHeight(), BufferedImage.TYPE_INT_RGB);
        render.setRGB(0, 0, scene.camera.getWidth(), scene.camera.getHeight(), new int[getRender().getWidth() * getRender().getHeight()], 0, scene.camera.getWidth());

        Vector3 topLeftPixel = scene.camera.getRight().mul(-1);
        topLeftPixel = topLeftPixel.mul(Math.tan(scene.camera.getFov() / 2));
        double pixelWidth = topLeftPixel.mul(2).length() / (scene.camera.getWidth() / 2.0f);

        float aspectRatioInv = (float) scene.camera.getHeight() / scene.camera.getWidth();
        topLeftPixel = topLeftPixel.add(scene.camera.getViewUp().mul(topLeftPixel.length() * aspectRatioInv));
        topLeftPixel = topLeftPixel.add(scene.camera.getForward());


        for (int i = 0; i < scene.camera.getHeight(); i++) {
            for (int j = 0; j < scene.camera.getWidth(); j++) {
                Vector3 pixel = topLeftPixel.add(scene.camera.getRight().mul(j * pixelWidth));
                pixel = pixel.sub(scene.camera.getViewUp().mul(i * pixelWidth));

                //todo length?
                Ray3D ray = new Ray3D(scene.camera.getPosition(), pixel.sub(scene.camera.getPosition()), 1000);

                render.setRGB(i, j, Util.Vector3ToColor(TraceRay(ray, 1, IOR_AIR)).getRGB());

            }
        }
    }

    private Vector3 TraceRay(Ray3D ray, double contribution, double IOR) {
        HitResult hitResult = scene.LineTraceSingle(ray);
        if (hitResult == null) return scene.backgroundColor;

        Vector3 color = new Vector3(0, 0, 0);

        for (Light light : scene.lights) {
            Vector3 lightDir = light.getPosition().sub(hitResult.intersection());
            lightDir = lightDir.normalize();

            Ray3D shadowRay = new Ray3D(hitResult.intersection(), lightDir, 1000);
            HitResult shadowIntersection = scene.LineTraceSingle(shadowRay);
            if (shadowIntersection != null) {
                color = color.add(light.getColor().mul(hitResult.material().getColor(hitResult.uv())).mul(lightDir.dot(hitResult.normal())));
            }
        }
        contribution *= hitResult.material().getSpecular().length();
        if (contribution > THRESHOLD) {
            Ray3D reflectionRay = new Ray3D(hitResult.intersection(), hitResult.normal().mul(-2).mul(hitResult.normal().dot(ray.direction)).sub(ray.direction), 1000);
            color = color.add(TraceRay(reflectionRay, contribution, IOR));

            if (hitResult.material().getOpacity() < 1) {
                //todo check this
                Vector3 refractionRayDirection = ray.direction.mul(IOR / hitResult.material().getIOR()).sub(hitResult.normal().mul(hitResult.normal().dot(ray.direction)));

                Ray3D refractionRay = new Ray3D(hitResult.intersection(), refractionRayDirection, 1000);
                color = color.add(TraceRay(refractionRay, contribution * hitResult.material().getOpacity(), hitResult.material().getIOR()));
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
