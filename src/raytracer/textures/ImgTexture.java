package raytracer.textures;

import raytracer.raytracer.Vec3;
import raytracer.raytracer.Transform;
import raytracer.cgtools.ImageTexture;
import raytracer.cgtools.Mat4;

import java.io.IOException;

/**
 * @author Roman Duhr
 */
public class ImgTexture implements Texture {

    ImageTexture imageTexture = null;
    Transform transformation;

    public ImgTexture(String filePath, Transform transformation) {
        this.transformation = transformation;
        try {
            imageTexture = new ImageTexture(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ImgTexture(String filePath) {
        this.transformation = new Transform(Mat4.identity);
        try {
            imageTexture = new ImageTexture(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Vec3 sample(Vec3 uv) {
        Vec3 transformedUV = transformation.transformation.transformPoint(uv);
        return imageTexture.samplePoint(transformedUV.x, transformedUV.y);
    }
}
