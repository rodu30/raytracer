package raytracer.material;

import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public interface Material {

    Vec3 emitted(Ray r, Hit h);
    Scattered scattered(Ray r, Hit h);

}
