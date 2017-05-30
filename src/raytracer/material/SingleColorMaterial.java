package raytracer.material;


import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;
import raytracer.textures.Texture;

/**
 * @author Roman Duhr
 */
public class SingleColorMaterial implements Material {

    public final Texture texture;

    public SingleColorMaterial(Texture texture) {
        this.texture = texture;
    }

    // returns color of shape for constant single color material (no light)
    @Override
    public Vec3 emitted(Ray ray, Hit hit) {
        return texture.sample(hit.uv);
    }

    // no reflection: returns null for albedo and scatteredRay
    @Override
    public Scattered scattered(Ray ray, Hit hit) {
        return new Scattered(true, false, null, null);
    }
}
