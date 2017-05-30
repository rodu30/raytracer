package raytracer.raytracer;

import raytracer.cgtools.Mat4;

/**
 * @author Roman Duhr
 */
public class Camera {

    public final double opening;
    public final double width;
    public final double height;
    public final Vec3 origin;
    public Mat4 transformation;

    /*
     * new Camera object with point of view in origin
     */
    public Camera(double opening, double sizeX, double sizeY, Mat4 transformation) {
        this.opening = opening;  // opening
        this.width = sizeX;
        this.height = sizeY;
        this.origin = new Vec3(0,0,0); // origin
        this.transformation = transformation;
    }

    /*
     * returns a ray which hits the pixel at coordinates x and y
     */
    public Ray generateRay(double x, double y) throws Exception {
        if (x > width || y > height) throw new Exception("Pixel not in picture");
        double rx = x - width / 2;
        double ry = height / 2 - y ;
        double rz = (-1) * ((height / 2) / (Math.tan(opening / 2)));
        Ray normalRay = new Ray(origin, new Vec3(rx, ry, rz)); // ray in local camera coordinate system

        // new transformed ray in world coordinate system
        return new Ray(transformation.transformPoint(normalRay.origin), transformation.transformDirection(normalRay.dir));
    }

}
