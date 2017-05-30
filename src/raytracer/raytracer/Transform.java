package raytracer.raytracer;

import raytracer.cgtools.Mat4;

/**
 * @author Roman Duhr
 */
public class Transform {

    public final Mat4 transformation;
    public final Mat4 invertedTransformation;


    public Transform(Mat4 transformation) {
        this.transformation = transformation;
        this.invertedTransformation = transformation.invertFull();
    }
}
