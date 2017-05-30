package raytracer.textures;

import raytracer.raytracer.Vec3;

/**
 * @author Roman Duhr
 */
public class ConstantTexture implements Texture {

    Vec3 color;

    public ConstantTexture(Vec3 color) {
        this.color = color;
    }

    @Override
    public Vec3 sample(Vec3 uv) {
        return color;
    }
}
