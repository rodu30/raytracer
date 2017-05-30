package raytracer.material;

import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;
import raytracer.textures.ConstantTexture;
import raytracer.textures.Texture;

/**
 * @author Roman Duhr
 */
public class PerfectReflectiveMaterial implements Material {

    public final Texture texture;

    /*
     * creates colored reflective material
     */
    public PerfectReflectiveMaterial(Texture texture) {
        this.texture = texture;
    }

    /*
     * creates colorless perfect reflective material
     */
    public PerfectReflectiveMaterial() {
        this.texture = new ConstantTexture(new Vec3(1,1,1));
    }

    /*
     * returns no constant color/shine (light needed), returns only shadow
     */
    @Override
    public Vec3 emitted(Ray ray, Hit hit) {
        return new Vec3(0, 0, 0);
    }

    /*
     * returns perfect reflected ray from hit point
     */
    @Override
    public Scattered scattered(Ray ray, Hit hit) {
        // direction of reflexion, formula: ray.dir - 2 * (hit.normVec * ray.dir) * hit.normVec
        Vec3 dir = ray.dir.reflection(hit.normVec);

        return new Scattered(false, true, texture.sample(hit.uv), new Ray(hit.pos, dir));
    }
}
