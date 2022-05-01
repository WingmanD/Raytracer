package Raytracer.Util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TextureObject {
    private String name;
    private final BufferedImage image;

    public TextureObject(String path) {
        this.name = path;

        try {
            image = ImageIO.read(new File(path));
        } catch (IOException e) {
            //TODO: handle exception
            throw new RuntimeException(e);
        }
    }

    public Vector3 getColor(Vector2 uv) {
        int x = (int) (uv.x * image.getWidth());
        int y = (int) (uv.y * image.getHeight());

        if (x >= image.getWidth()) {
            x = image.getWidth() - 1;
        }
        if (y >= image.getHeight()) {
            y = image.getHeight() - 1;
        }

        int clr = image.getRGB(x, y);
        int red = (clr & 0x00ff0000) >> 16;
        int green = (clr & 0x0000ff00) >> 8;
        int blue = clr & 0x000000ff;

        return new Vector3(red / 255.0f, green / 255.0f, blue / 255.0f);
    }


}
