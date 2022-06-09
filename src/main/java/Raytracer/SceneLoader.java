package Raytracer;

import java.io.IOException;

public interface SceneLoader {
    Scene loadScene(String fileName) throws IOException;
}
