package raytracer.material;

import raytracer.cgtools.Random;
import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.raytracer.Scattered;
import raytracer.raytracer.Vec3;
import raytracer.textures.ConstantTexture;
import raytracer.textures.Texture;

/**
 * @author Roman Duhr
 */
public class TransparentMaterial implements Material {

    private final Texture texture;
    private final double n1 = 1.0;    // air
    private final double n2 = 1.5;  // glass
    private final double r0;

    /*
     * creates colorless transparent material
     */
    public TransparentMaterial() {
        this.texture = new ConstantTexture(new Vec3(1,1,1));
        this.r0 = Math.pow((n1-n2) / (n1+n2), 2);
    }

    /*
     * returns no constant color/shine (light needed), returns only shadow
     */
    @Override
    public Vec3 emitted(Ray ray, Hit hit) {
        return new Vec3(0, 0, 0);
    }

    /*
     * returns perfect reflected ray from hit point
     */
    @Override
    public Scattered scattered(Ray r, Hit h) {

        // checks if total reflexion
        if(refract(r.dir, h.normVec, h.pos) != null) {

            if(schlickAppr(r.dir, h.normVec) < Random.random()) {
                return new Scattered(false, true, texture.sample(h.uv), refract(r.dir, h.normVec, h.pos));
            } else {
                return new Scattered(false, true, texture.sample(h.uv), reflect(r.dir, h.normVec, h.pos));
            }
        } else { // Totale Reflexion
            return new Scattered(false, true, texture.sample(h.uv), reflect(r.dir, h.normVec, h.pos));
        }
    }

    /*
     * Schlick Approximation
     */
    private double schlickAppr(Vec3 dir, Vec3 hitN) {
        if (hitN.multiplyVec(dir) > 0) return 0; // no reflection inside sphere
        // reflection coefficient
        return r0 + (1 - r0) * Math.pow(1 + hitN.multiplyVec(dir), 5);
    }


    /*
     * Transmission through glass
     */
    private Ray refract(Vec3 dir, Vec3 hitN, Vec3 pos) {

        double r = n1/n2;
        Vec3 n = hitN;

        // checks if ray comes from inside or outside
        if (-hitN.multiplyVec(dir) < 0) {
            n = hitN.multiplyScalar(-1);
            r = n2/n1;
        }

        double c = -n.multiplyVec(dir);
        double discr = 1 - Math.pow(r, 2) * (1 - Math.pow(c, 2));

        if (discr >= 0) {
            Vec3 dirTrans = dir.multiplyScalar(r).add(n.multiplyScalar(r * c - Math.sqrt(discr)));
            return new Ray(pos, dirTrans.normalise());
        }
        return null; // discr < 0
    }


    /*
     * Reflection
     */
    private Ray reflect(Vec3 dir, Vec3 hitN, Vec3 pos) {
        // direction of reflexion
        Vec3 dirR = dir.reflection(hitN);

        return new Ray(pos, dirR);
    }

}
