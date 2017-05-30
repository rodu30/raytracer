package raytracer.shapes;


import raytracer.material.Material;
import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class Background implements Shape {

    public final Material material;

    public Background(Material material) {
        this.material = material;
    }

    @Override
    public Hit intersect(Ray r) throws Exception {
        Vec3 uv = getUV(r.dir);
        return new Hit(Double.POSITIVE_INFINITY, r.pointAt(Double.POSITIVE_INFINITY), material, uv, r.dir);
    }

    private Vec3 getUV(Vec3 normal) {
        double inclination = Math.acos(normal.y);
        double azimuth = Math.atan2(normal.x, normal.z);
        double u = (azimuth + Math.PI) / (2 * Math.PI);
        double v = inclination / Math.PI;
        return new Vec3(u, v,0);
    }

}
