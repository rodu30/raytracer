package raytracer.textures;

import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public interface Texture {

    public Vec3 sample(Vec3 uv);
}
