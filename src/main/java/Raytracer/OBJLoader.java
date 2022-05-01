package Raytracer;

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
                    case "mtllib" -> materials = loadMaterials(tokens[1]);
                    case "o" -> {
                        meshIndex++;

                        if (meshIndex == 0) {
                            name = tokens[1];
                        } else {
                            meshes.add(new Mesh(name, faces));
                            vertices.clear();
                            UVs.clear();
                            normals.clear();
                            faces.clear();

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
                        String[] vertexTokens = tokens[1].split("/");
                        String[] uvTokens = tokens[2].split("/");
                        String[] normalTokens = tokens[3].split("/");

                        Vertex v1 = vertices.get(Integer.parseInt(vertexTokens[0]) - 1);
                        Vertex v2 = vertices.get(Integer.parseInt(vertexTokens[1]) - 1);
                        Vertex v3 = vertices.get(Integer.parseInt(vertexTokens[2]) - 1);

                        Vector2 uv1 = UVs.get(Integer.parseInt(uvTokens[0]) - 1);
                        Vector2 uv2 = UVs.get(Integer.parseInt(uvTokens[1]) - 1);
                        Vector2 uv3 = UVs.get(Integer.parseInt(uvTokens[2]) - 1);

                        Vector3 normal1 = normals.get(Integer.parseInt(normalTokens[0]) - 1);
                        Vector3 normal2 = normals.get(Integer.parseInt(normalTokens[1]) - 1);
                        Vector3 normal3 = normals.get(Integer.parseInt(normalTokens[2]) - 1);

                        v1.uv = uv1;
                        v2.uv = uv2;
                        v3.uv = uv3;

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

        Scene scene = new Scene();
        scene.objects = meshes;

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
                    case "map_Kd" -> currentMaterial.setDiffuseMap(new TextureObject(tokens[1]));
                    case "map_Ks" -> currentMaterial.setSpecularMap(new TextureObject(tokens[1]));
                    case "map_Ka" -> currentMaterial.setAmbientMap(new TextureObject(tokens[1]));
                    case "map_Ke" -> currentMaterial.setEmissiveMap(new TextureObject(tokens[1]));
                    case "map_Ns" ->
                            currentMaterial.setSpecularExponentMap(new TextureObject(tokens[1]));
                    case "map_d" -> currentMaterial.setOpacityMap(new TextureObject(tokens[1]));
                }
            }
        } catch (NullPointerException e) {
            System.out.println("Invalid mtl format");
        }

        return materials;
    }
}
