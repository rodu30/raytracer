package raytracer.material;

import raytracer.cgtools.Random;
import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;
import raytracer.textures.ConstantTexture;
import raytracer.textures.Texture;

/**
 * @author Roman Duhr
 */
public class MattReflectiveMaterial implements Material {

    public final Texture texture;
    public final double roughness;

    /*
     * creates colored reflective material
     */
    public MattReflectiveMaterial(Texture texture, double roughness) {
        this.texture = texture;
        this.roughness = roughness;
    }

    /*
     * creates colorless matt reflective material
     */
    public MattReflectiveMaterial(double roughness) {
        this.roughness = roughness;
        this.texture = new ConstantTexture(new Vec3(1, 1, 1));
    }

    /*
     * creates colored reflective material with random roughness
     */
    public MattReflectiveMaterial(Texture texture) {
        this.texture = texture;
        this.roughness = Random.random();
    }

    /*
     * creates colorless matt reflective material with random roughness
     */
    public MattReflectiveMaterial() {
        this.roughness = Random.random();
        this.texture = new ConstantTexture(new Vec3(1, 1, 1));
    }

    /*
     * returns no constant color/shine (light needed), returns only shadow
     */
    @Override
    public Vec3 emitted(Ray ray, Hit hit) {
        return new Vec3(0, 0, 0);
    }

    /*
     * returns matt reflected ray from hit point
     */
    @Override
    public Scattered scattered(Ray ray, Hit hit) {
        // direction of reflexion
        Vec3 dir = ray.dir.reflection(hit.normVec);
        // random Vec between -1 and 1 in norm sphere
        Vec3 random = new Vec3(
                new Random().random() * 2 - 1,
                new Random().random() * 2 - 1,
                new Random().random() * 2 - 1);
        // matt reflection formula: dir + s * xRandom
        Vec3 dirS = dir.add(random.multiplyScalar(roughness));
        // checks if point is under surface
        if (dirS.multiplyVec(hit.normVec) < 0) {
            return new Scattered(true, true, null, null);
        }

        return new Scattered(false, true, texture.sample(hit.uv), new Ray(hit.pos, dirS));
    }
}
