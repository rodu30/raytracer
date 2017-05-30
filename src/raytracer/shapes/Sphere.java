package raytracer.shapes;

import raytracer.material.Material;
import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class Sphere implements Shape {

    public final Vec3 center;
    public final double radius;
    public final Material material;

    public Sphere(Vec3 center, double radius, Material material) {
        this.center = center;
        this.radius = radius;
        this.material = material;
//        System.out.println("Sphere created: center " + center.printVec() + " radius: " + this.radius);
    }

    @Override
    public Hit intersect(Ray ray) throws Exception {
        // p/q values:
        double p = (ray.dir.multiplyScalar(2)).multiplyVec(ray.origin.substract(center)); // 2 * d * (x0−c)
        double q = ray.origin.substract(center).multiplyVec(ray.origin.substract(center)) - Math.pow(radius, 2); // (x0−c)^2−ray^2
        double discr = Math.pow(p / 2, 2) - q; // (p/2)^2 - q

        // check if 0, 1 or 2 intersections
        double t1 = ((-1) * (p / 2)) + Math.sqrt(discr);
        double t2 = ((-1) * (p / 2)) - Math.sqrt(discr);

        if (discr > 0) {                                            // Sekante
            if (t1 > ray.t0 + 0.0001 && t2 > ray.t0 + 0.0001) {
                double tMin = Math.min(t1, t2);
                Vec3 pos = ray.pointAt(tMin);
                Vec3 normal = pos.substract(center).normalise();
                return new Hit(tMin, pos, material, getUV(normal), normal);
            } else if (t1 > ray.t0 + 0.0001) {
                Vec3 pos = ray.pointAt(t1);
                Vec3 normal = pos.substract(center).normalise();
                return new Hit(t1, pos, material, getUV(normal), normal);
            } else if (t2 > ray.t0 + 0.0001) {
                Vec3 pos = ray.pointAt(t2);
                Vec3 normal = pos.substract(center).normalise();
                return new Hit(t2, pos, material, getUV(normal), normal);
            }
        } else if (discr == 0) {                                    // Tangente
            Vec3 pos = ray.pointAt(t1);
            Vec3 normal = pos.substract(center).normalise();
            return new Hit(t1, pos, material, getUV(normal), normal);
        }
        return null;                                                // Passante, no hit
    }

    private Vec3 getUV(Vec3 normal) {
        double inclination = Math.acos(normal.y);
        double azimuth = Math.atan2(normal.x, normal.z);
        double u = (azimuth + Math.PI) / (2 * Math.PI);
        double v = inclination / Math.PI;
        return new Vec3(u, v,0);
    }

}
