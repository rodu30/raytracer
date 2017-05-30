package raytracer.raytracer;

/**
 * @author Roman Duhr
 */
public class Scattered {

    public final boolean absorbed;
    public final boolean specular;
    public final Vec3 albedo;
    public final Ray scatteredRay;

    public Scattered(boolean absorbed, boolean specular, Vec3 albedo, Ray scatteredRay) {
        this.absorbed = absorbed;
        this.specular = specular;
        this.albedo = albedo;
        this.scatteredRay = scatteredRay;
    }
}
