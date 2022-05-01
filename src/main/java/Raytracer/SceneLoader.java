package Raytracer;

import java.io.IOException;

public interface SceneLoader {
    public Scene loadScene(String fileName) throws IOException;
}
