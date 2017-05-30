package raytracer.material;

import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class GradiantMaterial implements Material {

    // returns color for background material
    @Override
    public Vec3 emitted(Ray r, Hit h) {
        double dirY = r.dir.y;
        return new Vec3((dirY + 1)/ 2, (dirY + 1)/ 2, 1);
    }

    // no reflection
    @Override
    public Scattered scattered(Ray r, Hit h) {
        return new Scattered(true, false, null, null);
    }

}
