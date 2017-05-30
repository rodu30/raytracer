package raytracer.material;

import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class StripedMaterial implements Material {

    // returns color for background material
    @Override
    public Vec3 emitted(Ray r, Hit h) {
        double dirY = r.dir.y;
        if ((int) (dirY * 20) % 2 == 0) {
            return new Vec3(0.38, 0.69, 0.96);  // light blue
        } else {
            return new Vec3(1, 1, 0);  // yellow
        }
    }

    // no reflection
    @Override
    public Scattered scattered(Ray r, Hit h) {
        return new Scattered(true, false, null, null);
    }

}
