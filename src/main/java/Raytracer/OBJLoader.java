package Raytracer;

import Raytracer.Util.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJLoader implements SceneLoader {
    @Override
    public Scene loadScene(String fileName) throws IOException {
        if (!fileName.endsWith(".obj")) throw new IllegalArgumentException("File must be an .obj file");

        Path path = Path.of(fileName);
        if (!Files.exists(path)) throw new IllegalArgumentException("File does not exist");

        List<Object3D> meshes = new ArrayList<>();

        int meshIndex = -1;
        String name = null;

        List<Vertex> vertices = new ArrayList<>();
        List<Vector2> UVs = new ArrayList<>();
        List<Vector3> normals = new ArrayList<>();
        List<Triangle> faces = new ArrayList<>();

        Map<String, Material> materials = null;
        Material currentMaterial = null;

        try (var reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");

                switch (tokens[0]) {
                    case "mtllib" -> {
                        materials = loadMaterials(getMaterialFilePath(fileName, tokens[1]));
                    }
                    case "o" -> {
                        meshIndex++;

                        if (meshIndex == 0) {
                            name = tokens[1];
                        } else {
                            meshes.add(new Mesh(name, faces));
                            faces = new ArrayList<>();
                            name = tokens[1];
                        }

                    }
                    case "v" -> vertices.add(new Vertex(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));

                    case "vt" -> UVs.add(new Vector2(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2])
                    ));

                    case "vn" -> normals.add(new Vector3(
                            Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]),
                            Float.parseFloat(tokens[3])
                    ));
                    case "f" -> {
                        String[] v1tokens = tokens[1].split("/");
                        String[] v2tokens = tokens[2].split("/");
                        String[] v3tokens = tokens[3].split("/");

                        Vertex v1 = vertices.get(Integer.parseInt(v1tokens[0]) - 1);
                        Vertex v2 = vertices.get(Integer.parseInt(v2tokens[0]) - 1);
                        Vertex v3 = vertices.get(Integer.parseInt(v3tokens[0]) - 1);


                        try {
                            int uv1Index = Integer.parseInt(v1tokens[1]) - 1;
                            if (uv1Index < UVs.size())
                                v1.uv = UVs.get(uv1Index);
                            else
                                v1.uv = new Vector2(0, 0);
                        } catch (NumberFormatException e) {
                            v1.uv = new Vector2(0, 0);
                        }

                        try {
                            int uv2Index = Integer.parseInt(v2tokens[1]) - 1;
                            if (uv2Index < UVs.size())
                                v2.uv = UVs.get(uv2Index);
                            else
                                v2.uv = new Vector2(0, 0);
                        } catch (NumberFormatException e) {
                            v2.uv = new Vector2(0, 0);
                        }

                        try {
                            int uv3Index = Integer.parseInt(v3tokens[1]) - 1;
                            if (uv3Index < UVs.size())
                                v3.uv = UVs.get(uv3Index);
                            else
                                v3.uv = new Vector2(0, 0);
                        } catch (NumberFormatException e) {
                            v3.uv = new Vector2(0, 0);
                        }

                        Vector3 normal1 = normals.get(Integer.parseInt(v1tokens[2]) - 1);
                        Vector3 normal2 = normals.get(Integer.parseInt(v2tokens[2]) - 1);
                        Vector3 normal3 = normals.get(Integer.parseInt(v3tokens[2]) - 1);

                        v1.normal = normal1;
                        v2.normal = normal2;
                        v3.normal = normal3;

                        faces.add(new Triangle(new Vertex[]{v1, v2, v3}, currentMaterial));
                    }
                    case "usemtl" -> {
                        if (materials == null) throw new RuntimeException("Invalid mtl format");
                        if (materials.containsKey(tokens[1]))
                            currentMaterial = materials.get(tokens[1]);
                    }

                }

            }
        }
        meshes.add(new Mesh(name, faces));

        Scene scene = new Scene();
        scene.objects = meshes;
        scene.name = fileName;

        return scene;
    }

    public Map<String, Material> loadMaterials(String fileName) throws IOException {
        if (!fileName.endsWith(".mtl")) throw new IllegalArgumentException("File must be an .mtl file");

        Path path = Path.of(fileName);
        if (!Files.exists(path)) throw new IllegalArgumentException("File does not exist");

        Map<String, Material> materials = new HashMap<>();

        try (var reader = Files.newBufferedReader(path)) {
            Material currentMaterial = null;

            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                switch (tokens[0]) {
                    case "newmtl" -> {
                        currentMaterial = new Material(tokens[1]);
                        materials.put(tokens[1], currentMaterial);
                    }
                    case "Kd" ->
                            currentMaterial.setDiffuse(new Vector3(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                    case "Ka" ->
                            currentMaterial.setAmbient(new Vector3(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                    case "Ks" ->
                            currentMaterial.setSpecular(new Vector3(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                    case "Ke" ->
                            currentMaterial.setEmissive(new Vector3(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3])));
                    case "Ns" -> currentMaterial.setSpecularExponent(Float.parseFloat(tokens[1]));
                    case "Ni" -> currentMaterial.setIOR(Float.parseFloat(tokens[1]));
                    case "d" -> currentMaterial.setOpacity(Float.parseFloat(tokens[1]));
                    case "map_Kd" ->
                            currentMaterial.setDiffuseMap(new TextureObject(getTextureFilePath(fileName, tokens[2])));
                    case "map_Ks" ->
                            currentMaterial.setSpecularMap(new TextureObject(getTextureFilePath(fileName, tokens[2])));
                    case "map_Ka" ->
                            currentMaterial.setAmbientMap(new TextureObject(getTextureFilePath(fileName, tokens[2])));
                    case "map_Ke" ->
                            currentMaterial.setEmissiveMap(new TextureObject(getTextureFilePath(fileName, tokens[2])));
                    case "map_Ns" ->
                            currentMaterial.setSpecularExponentMap(new TextureObject(getTextureFilePath(fileName, tokens[2])));
                    case "map_d" ->
                            currentMaterial.setOpacityMap(new TextureObject(getTextureFilePath(fileName, tokens[2])));
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Invalid mtl format");
        }

        return materials;
    }

    private String getMaterialFilePath(String mainFileName, String otherfileName) {
        return Path.of(new File(mainFileName).getParent(), otherfileName).toString();
    }

    private String getTextureFilePath(String mainFileName, String otherfileName) {
        return Path.of(new File(mainFileName).getParent(), new File(otherfileName).getName()).toString();
    }
}
