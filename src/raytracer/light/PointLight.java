package raytracer.light;

import raytracer.raytracer.Ray;
import raytracer.raytracer.Vec3;
import raytracer.shapes.Group;

/**
 * @author Roman Duhr
 */
public class PointLight implements Light {

    Vec3 position;
    Vec3 intensity;

    public PointLight(Vec3 position, Vec3 intensity) {
        this.position = position;
        this.intensity = intensity;
    }

    @Override
    public LightSample sample(Vec3 fromPoint, Group scene) {

        Vec3 incident = position.substract(fromPoint).normalise();
        LightSample result = new LightSample( new Vec3(0.0, 0.0, 0.0), incident);

        try {
            if(scene.visible(new Ray(fromPoint, incident))) {
                double squaredDistance = Math.pow(fromPoint.substract(position).length(), 2);
                result.radiance = intensity.multiplyScalar(1/squaredDistance);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
