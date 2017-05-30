package raytracer.raytracer;

/**
 * @author Roman Duhr
 */
public class Ray {

    public final Vec3 origin;
    public final Vec3 dir;
    public final double t0;
    public final double t1;

    public Ray(Vec3 origin, Vec3 dir) {
        this.origin = origin;
        this.dir = dir.normalise();
        this.t0 = 0.0000;
        this.t1 = Double.POSITIVE_INFINITY;
    }

    /*
     * returns the point where ray hits an object: x(t)
     */
    public Vec3 pointAt(double t) throws Exception {
        if (t < t0 || t > t1) throw new IllegalArgumentException("t is out of range");
        return origin.add(dir.multiplyScalar(t));
    }

}
