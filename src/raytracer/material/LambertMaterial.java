package raytracer.material;

import raytracer.cgtools.Random;
import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;
import raytracer.textures.Texture;

/**
 * @author Roman Duhr
 */
public class LambertMaterial implements Material {

    public final Texture texture;

    public LambertMaterial(Texture texture) {
        this.texture = texture;
    }


    // no constant color/shine (light needed), returns only shadow
    @Override
    public Vec3 emitted(Ray ray, Hit hit) {
        return new Vec3(0, 0, 0);
    }

    @Override
    public Scattered scattered(Ray ray, Hit hit) {
        // random Vec between -1 and 1
        Vec3 random = new Vec3(
                new Random().random() * 2 - 1,
                new Random().random() * 2 - 1,
                new Random().random() * 2 - 1);
        Vec3 dir = hit.normVec.add(random).normalise(); // direction vector for new vector from hit
        Ray scatteredRay = new Ray(hit.pos, dir); // build reflected ray from hit

        return new Scattered(false, false, texture.sample(hit.uv).multiplyScalar(dir.multiplyVec(hit.normVec)),
                scatteredRay);
    }
}
