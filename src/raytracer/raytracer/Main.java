package raytracer.raytracer;

import raytracer.cgtools.Image;
import raytracer.cgtools.Mat4;
import raytracer.cgtools.Random;
import raytracer.light.Light;
import raytracer.light.LightSample;
import raytracer.light.PointLight;
import raytracer.material.LambertMaterial;
import raytracer.material.PerfectReflectiveMaterial;
import raytracer.material.SingleColorMaterial;
import raytracer.material.TransparentMaterial;
import raytracer.shapes.Background;
import raytracer.shapes.Group;
import raytracer.shapes.PlaneRectangle;
import raytracer.shapes.Sphere;
import raytracer.textures.ConstantTexture;
import raytracer.textures.ImgTexture;

import java.util.ArrayList;

/**
 * @author Roman Duhr
 */
public class Main {

    static String name = "examples/example-1.png"; // TODO: Put name and directory here
    static final int WIDTH = 480;
    static final int HEIGHT = 270;
    static Image img;

    static Background bg;
    static Group scene;
    static ArrayList<Light> lights;
    static Camera cam;

    static final double GAMMA = 1.9;
    static final double SAMPLING_RATE = 10; // TODO: change as you like, e.g. 30 will give you a good picture but
    // will take a while to render

    public static void main(String[] args) {

        // Build scene
        bg = new Background(new SingleColorMaterial(new ImgTexture("images/stars.jpg")));
        scene = buildScene();

        // Add lights
        lights = new ArrayList<>();
        lights.add(new PointLight(new Vec3(0,3, 2), new Vec3(10,10,10)));
        lights.add(new PointLight(new Vec3(-5,3,-5), new Vec3(15,15,15)));
        lights.add(new PointLight(new Vec3(15,5,2), new Vec3(25,25,25)));
        lights.add(new PointLight(new Vec3(-2,19,0), new Vec3(15,15,15)));

        // Camera transformations
        Mat4 frontView = Mat4.translate(new Vec3(0, 0, 25));
        Mat4 frontViewRaised = Mat4.translate(new Vec3(0, 10, 15));
        Mat4 leftSideView = Mat4.translate(new Vec3(-25, 15, -2)).mult(Mat4.rotate(new Vec3(0, 1, 0), -90));
        Mat4 aboveView = Mat4.translate(new Vec3(0, 35, -5)).mult(Mat4.rotate(new Vec3(1, 0, 0), -90));
        Mat4 frontDiagonalAboveView = Mat4.translate(new Vec3(0, 25, 15)).mult(Mat4.rotate(new Vec3(1, 0, 0), -45));
        Mat4 halfLeftSideDiagonalAboveView = Mat4.translate(new Vec3(-20, 35, -10)).mult(Mat4.rotate(new Vec3(0, 1,
                0), -135)).mult(Mat4.rotate(new Vec3(1, 0, 0), -45));
        Mat4 halfRightSideDiagonalAboveView = Mat4.translate(new Vec3(45, 40, -40)).mult(Mat4.rotate(new Vec3(0,
                1, 0), 135)).mult(Mat4.rotate(new Vec3(1, 0, 0), -45));
        Mat4 halfLeftSideDiagonalBelowView = Mat4.translate(new Vec3(-10, 1, -10)).mult(Mat4.rotate(new Vec3(0,
                1,0), -135)).mult(Mat4.rotate(new Vec3(1, 0, 0), 45));

        // Create camera
        cam = new Camera(Math.PI / 2, WIDTH, HEIGHT, frontViewRaised);

        // Create image
        img = new Image(WIDTH, HEIGHT);
        try {
            for (double x = 0; x != WIDTH; x++) {
                for (double y = 0; y != HEIGHT; y++) {
                    Vec3 sampCol = gammaCorrection(sample(x, y));
                    img.setPixel((int) x, (int) y, sampCol);
                }
            }
            img.write(name);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }


    //    ###################### Image creation methods #######################

    /*
     * Makes gamma correction for every RGB material value
     */
    static Vec3 gammaCorrection(Vec3 rgb) {
        return new Vec3(
                Math.pow(rgb.x / 1, 1 / GAMMA),
                Math.pow(rgb.y / 1, 1 / GAMMA),
                Math.pow(rgb.z / 1, 1 / GAMMA)
        );
    }

    /*
     * Applies super (stratified) sampling to pixel and returns corrected RGB values
     */
    static Vec3 sample(double x, double y) throws Exception {
        double red = 0;
        double green = 0;
        double blue = 0;
        for (int xi = 0; xi < SAMPLING_RATE; xi++) {
            for (int yi = 0; yi < SAMPLING_RATE; yi++) {
                double xs = x + (xi + new Random().random()) / SAMPLING_RATE;
                double ys = y + (yi + new Random().random()) / SAMPLING_RATE;
                Vec3 color = raytrace(xs, ys);
                red += color.x;
                green += color.y;
                blue += color.z;
            }
        }
        return new Vec3(
                red / Math.pow(SAMPLING_RATE, 2),
                green / Math.pow(SAMPLING_RATE, 2),
                blue / Math.pow(SAMPLING_RATE, 2)
        );
    }

    /*
     * Helper method: intersects ray with shape and returns hit or background hit
     */
    static Hit getHit(Ray ray) throws Exception {
        Hit hit = scene.intersect(ray);
        if (hit != null) {
            return hit;  // hit for shape
        } else {
            return bg.intersect(ray);  // hit for background
        }
    }

    /*
     * Gets color for material of shape (reflection or not)
     */
    static Vec3 calculateRadiance(Ray ray, int depth) throws Exception {
        Hit hit = getHit(ray);
        Vec3 emitted = hit.material.emitted(ray, hit); // emitted constant radiance
        Scattered scattered = hit.material.scattered(ray, hit); // reflected part

        if (!scattered.absorbed && depth > 0) { // Ray was scattered

            Vec3 direct = new Vec3(0,0,0); // direct light

            if (!scattered.specular) {
                for (Light light : lights) {
                    LightSample s = light.sample(hit.pos, scene);
                    direct = direct.add(s.radiance.multiplyScalar(hit.normVec.multiplyVec(s.incident)));
                }
            }
            // add direct light to albedo of scattered ray
            return scattered.albedo.multiplyComp(direct.add(calculateRadiance(scattered.scatteredRay, depth-1))).add
                    (emitted);
        } else {        // Ray was absorbed
            return emitted;
        }
    }

    /*
     *  Creates Ray for pixel and returns color of shape
     */
    static Vec3 raytrace(double x, double y) throws Exception {
        Ray ray = cam.generateRay(x, y);
        return calculateRadiance(ray, 10);
    }


    //    ###################### Scene creation methods #######################

    /*
     * Builds group with shapes - NOTE: this is only an example
     */
    private static Group buildScene() {

        // cone:
        Group cone = new Group();
        for (int i = 1; i <= 40; i++) {
            cone.addShape(new Sphere(new Vec3(0, 0.2 * i, 0), 0.1 * i, new LambertMaterial(new
                    ConstantTexture(new Vec3(1, 0.6, 0)))));
        }

        // ice cream scoops:
        Group scoops = new Group();
        // vanilla:
        scoops.addShape(new Sphere(new Vec3(0, 0, -3.5), 4, new LambertMaterial(new
                ConstantTexture(new Vec3(0.89, 0.79,
                0.55)))));
        // strawberry:
        scoops.addShape(new Sphere(new Vec3(2.5, 0, 1.5), 4, new LambertMaterial(new
                ConstantTexture(new Vec3(1, 0.3, 0.5)))));
        // chocolate:
        scoops.addShape(new Sphere(new Vec3(-2.5, 0, 1.5), 4, new LambertMaterial(new
                ConstantTexture(new Vec3(0.3, 0.2,
                0.1)))));

        // cream on top:
        Group cream = new Group();
        for (int i = 0; i <= 5; i++) {
            cream.addShape(new Sphere(new Vec3(0, i, 0), 4 - i * 0.5, new LambertMaterial(new
                    ConstantTexture(new Vec3(1, 1,
                    1)))));
        }

        // cherry on top:
        Group cherry = new Group();
        cherry.addShape(new Sphere(new Vec3(0, 1.5, 0), 1.5, new PerfectReflectiveMaterial(new
                ConstantTexture(new Vec3(1, 0, 0)))));
        cherry.addTransformation(new Transform(Mat4.translate(new Vec3(0, 5, 0))));

        // cream and cherry:
        Group topping = new Group();
        topping.addShape(cream);
        topping.addShape(cherry);
        topping.addTransformation(new Transform(Mat4.translate(new Vec3(0, 4, 0))));

        // topping and scoops:
        Group iceCreamGroup = new Group();
        iceCreamGroup.addShape(scoops);
        iceCreamGroup.addShape(topping);
        iceCreamGroup.addTransformation(new Transform(Mat4.translate(new Vec3(0, 11, 0))));

        // ice cream and cone (complete):
        Group iceCreamCone = new Group();
        iceCreamCone.addShape(cone);
        iceCreamCone.addShape(iceCreamGroup);
        iceCreamCone.addTransformation(new Transform(Mat4.translate(new Vec3(0, 2, -5)))); // bring all to back

        // add a copy and place it on the left
        Group iceCreamCone3 = new Group(iceCreamCone, new Transform(Mat4.translate(new Vec3(-20,0,-10)).mult
                (Mat4.rotate(new Vec3(0, 1, 0), -90))));

        Group mirror = new Group();
        mirror.addShape(new PlaneRectangle(10, 10, new PerfectReflectiveMaterial()));
        mirror.addShape(new PlaneRectangle(10.5, 10.5, new LambertMaterial(new ConstantTexture(new Vec3(0.9, 0.9,0.9)))));
        mirror.addTransformation(new Transform(Mat4.translate(10,11,-15).mult(Mat4.rotate(new Vec3(0,1,0),45)).mult
                (Mat4.rotate(new Vec3(0,0,1), 85))));

        Group scene = new Group();
        scene.addShape(iceCreamCone);
        scene.addShape(iceCreamCone3);
        scene.addShape(mirror);
        // add underground:
        scene.addShape(new PlaneRectangle(
                60, 40, new LambertMaterial(new ImgTexture(
                        "images/moon.jpg",
                        new Transform(Mat4.scale(new Vec3(0.009,0.015, 1)).mult(Mat4.translate(new Vec3(20,-10,0))))))));
        // add glass object:
        scene.addShape(new Sphere(new Vec3(-10,7,2), 5, new TransparentMaterial()));
        // add earth textured object:
        scene.addShape(new Sphere(new Vec3(50, 25, -30), 10, new SingleColorMaterial(new ImgTexture
                ("images/world-topo.png"))));

        return scene;
    }


}
