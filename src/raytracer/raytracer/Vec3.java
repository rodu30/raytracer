package raytracer.raytracer;

/**
 * @author Roman Duhr
 */
public class Vec3 {

    public final double x;
    public final double y;
    public final double z;

    public Vec3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /*
     * adds another vector
     */
    public Vec3 add(Vec3 b) {
        return new Vec3(x + b.x, y + b.y, z + b.z);
    }

    /*
     * substract another vector
     */
    public Vec3 substract(Vec3 b) {
        return new Vec3(x - b.x, y - b.y, z - b.z);
    }

    /*
     * multiplay with a scalar (single value)
     */
    public Vec3 multiplyScalar(double s) {
        return new Vec3(x * s, y * s, z * s);
    }

    /*
     * returns length of vector
     */
    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /*
     * normalise vector
     */
    public Vec3 normalise() {
        return new Vec3(x / this.length(), y / this.length(), z / this.length());
    }

    /*
     * multiplay with another vector
     */
    public double multiplyVec(Vec3 b) {
        return x * b.x + y * b.y + z * b.z;
    }

    /*
     * returns cartesian product of vector
     */
    public Vec3 cartesianProduct(Vec3 b) {
        return new Vec3(y * b.z - z * b.y, z * b.x - x * b.z, x * b.y - y * b.x);
    }

    /*
     * multiply with another vector component for component
     */
    public Vec3 multiplyComp(Vec3 b) {
        return new Vec3(b.x * x, b.y * y, b.z * z);
    }

    /*
     * reflect this vector on normal vector in point (formula: ray.dir - 2 * (hit.normVec * ray.dir) * hit.normVec)
     */
    public Vec3 reflection(Vec3 normVec) {
        return this.substract(normVec.multiplyScalar(2).multiplyScalar(normVec.multiplyVec(this)));
    }

    /*
     * returns vector as string
     */
    public String printVec() {
        return "(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public boolean equals(Object o) {
        if (!(o instanceof Vec3))
            return false;
        if (o == this)
            return true;
        Vec3 v = (Vec3) o;
        return v.x == x && v.y == y && v.z == z;
    }

}
