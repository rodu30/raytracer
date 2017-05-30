package raytracer.light;

import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class LightSample {

    public Vec3 radiance; // Light intensity
    public Vec3 incident; // Direction of the hit

    public LightSample(Vec3 radiance, Vec3 incident) {
        this.radiance = radiance;
        this.incident = incident;
    }
}

