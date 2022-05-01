package Raytracer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Renderer {
    private final Scene scene;

    public Renderer(Scene scene) {
        this.scene = scene;
    }

    public void render(int samples) {
        BufferedImage image = new BufferedImage(scene.camera.getWidth(), scene.camera.getHeight(), BufferedImage.TYPE_INT_RGB);

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
                Ray3D ray = new Ray3D(scene.camera.getPosition(), pixel.sub(scene.camera.getPosition()),1000);

                image.setRGB(i, j, new Color(0, 0, 0).getRGB());

            }
        }
    }

    private Color TraceRay(Ray3D ray) {

        return new Color(0, 0, 0);
    }

}
