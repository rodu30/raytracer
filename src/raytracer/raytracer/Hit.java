package raytracer.raytracer;

import raytracer.material.Material;

/**
 * @author Roman Duhr
 */
public class Hit implements Comparable<Hit> {

    public final double t;
    public final Vec3 pos;
    public final Material material;
    public final Vec3 uv;
    public final Vec3 normVec;  // n vector: vertical to hit position

    public Hit(double t, Vec3 position, Material material, Vec3 normVec) {
        this.t = t;
        this.pos = position;
        this.material = material;
        this.uv = new Vec3(0,0,0);
        this.normVec = normVec;
    }

    public Hit(double t, Vec3 position, Material material, Vec3 uv, Vec3 normVec) {
        this.t = t;
        this.pos = position;
        this.material = material;
        this.uv = uv;
        this.normVec = normVec;
    }

    /*
     * compares absolute value of z
     */
    @Override
    public int compareTo(Hit h) {
        if (t > h.t) {
            return 1;
        } else if (t == h.t) {
            return 0;
        } else {
            return -1;
        }
    }
}
