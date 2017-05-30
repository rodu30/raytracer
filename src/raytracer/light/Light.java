package raytracer.light;

import raytracer.raytracer.Vec3;
import raytracer.shapes.Group;

/**
 * @author Roman Duhr
 */
public interface Light {

    LightSample sample(Vec3 fromPoint, Group scene);

}
