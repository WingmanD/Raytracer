package Raytracer;

public class Material {
    public String name;

    private Vector3 ambient;
    private Vector3 diffuse;
    private Vector3 specular;

    private Vector3 emissive;
    private double specularExponent;

    private double IOR;
    private double opacity;

    private TextureObject diffuseMap;
    private TextureObject specularMap;
    private TextureObject normalMap;
    private TextureObject opacityMap;
    private TextureObject ambientMap;
    private TextureObject emissiveMap;
    private TextureObject specularExponentMap;

    public Material(String name) {
        this.name = name;
    }

    public Material(String name, Vector3 ambient, Vector3 diffuse, Vector3 specular, double specularExponent, double IOR, double opacity) {
        this.name = name;
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.specularExponent = specularExponent;
        this.IOR = IOR;
        this.opacity = opacity;
    }

    public Vector3 getAmbient() {
        return ambient;
    }

    public Vector3 getDiffuse() {
        return diffuse;
    }

    public Vector3 getSpecular() {
        return specular;
    }

    public double getSpecularExponent() {
        return specularExponent;
    }

    public double getIOR() {
        return IOR;
    }

    public double getOpacity() {
        return opacity;
    }

    public TextureObject getDiffuseMap() {
        return diffuseMap;
    }

    public TextureObject getSpecularMap() {
        return specularMap;
    }

    public TextureObject getNormalMap() {
        return normalMap;
    }

    public TextureObject getOpacityMap() {
        return opacityMap;
    }

    public void setAmbient(Vector3 ambient) {
        this.ambient = ambient;
    }

    public void setDiffuse(Vector3 diffuse) {
        this.diffuse = diffuse;
    }

    public void setSpecular(Vector3 specular) {
        this.specular = specular;
    }

    public void setSpecularExponent(double specularExponent) {
        this.specularExponent = specularExponent;
    }

    public void setIOR(double IOR) {
        this.IOR = IOR;
    }

    public void setOpacity(double opacity) {
        this.opacity = opacity;
    }

    public void setDiffuseMap(TextureObject diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public void setSpecularMap(TextureObject specularMap) {
        this.specularMap = specularMap;
    }

    public void setNormalMap(TextureObject normalMap) {
        this.normalMap = normalMap;
    }

    public void setOpacityMap(TextureObject opacityMap) {
        this.opacityMap = opacityMap;
    }

    public void setEmissive(Vector3 emissive) {
        this.emissive = emissive;
    }

    public Vector3 getEmissive() {
        return emissive;
    }

    public void setEmissiveMap(TextureObject emissiveMap) {
        this.emissiveMap = emissiveMap;
    }

    public TextureObject getEmissiveMap() {
        return emissiveMap;
    }

    public void setSpecularExponentMap(TextureObject specularExponentMap) {
        this.specularExponentMap = specularExponentMap;
    }

    public TextureObject getSpecularExponentMap() {
        return specularExponentMap;
    }

    public void setAmbientMap(TextureObject ambientMap) {
        this.ambientMap = ambientMap;
    }

    public TextureObject getAmbientMap() {
        return ambientMap;
    }

    public Vector3 getColor(Vector2 uv) {
        Vector3 color;
        if (diffuseMap != null) {
            color = diffuseMap.getColor(uv).mul(diffuse);
        } else {
            color = diffuse;
        }
        return color;
    }

    public double getOpacityAt(Vector2 uv) {
        if (opacityMap != null) {
            return opacityMap.getColor(uv).x;
        } else {
            return opacity;
        }
    }
}
