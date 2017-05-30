package raytracer.shapes;


import raytracer.material.Material;
import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class PlaneRectangle implements Shape {

    public final double width;
    public final double depth;
    public final Vec3 center;
    public final Material material;

    public PlaneRectangle(double width, double depth, Material material) {
        this.width = width;
        this.depth = depth;
        center = new Vec3(0, 0, 0);
        this.material = material;
//        System.out.println("PlaneRectangle created: width " + width + " depth: " + depth);
    }

    @Override
    public Hit intersect(Ray r) throws Exception {

        // Normal vector:
        Vec3 normVec = new Vec3(0, 1, 0);

        // plane equation:
        double denominator = r.dir.y;
        if (denominator != 0) { // denominator can not be 0
            double t = -(r.origin.y / denominator);

            if (t >= (r.t0 + 0.0001)) {     // check if point is in front of camera
                Vec3 hitPos = r.pointAt(t);

                if (Math.abs(hitPos.x) > width || Math.abs(hitPos.z) > depth) return null; // check if point
                // lays within rectangle

                // check if view from over or under plane:
                if (r.dir.multiplyScalar(-1).multiplyVec(normVec) < 0) {
                    normVec = normVec.multiplyScalar(-1);
                }
                return new Hit(t, hitPos, material, new Vec3(hitPos.x, hitPos.z, 0),
                        normVec);

            }
        }
        return null;
    }

}
