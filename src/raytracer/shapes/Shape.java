package raytracer.shapes;

import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;

/**
 * @author Roman Duhr
 */
public interface Shape {

    /*
     * returns the point and the color where ray hits the shape
     */
    public Hit intersect(Ray r) throws Exception;
}
