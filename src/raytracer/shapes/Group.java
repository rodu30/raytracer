package raytracer.shapes;

import raytracer.raytracer.Hit;
import raytracer.raytracer.Ray;
import raytracer.cgtools.Mat4;
import raytracer.raytracer.Transform;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author Roman Duhr
 */
public class Group implements Shape {

    public Transform transformation;
    private ArrayList<Shape> shapes = new ArrayList<>();

    /*
     * constructer with single shape and single transformation
     */
    public Group(Shape shape, Transform transformation) {
        this.transformation = transformation;
        this.shapes.add(shape);
    }

    /*
     * constructer with array list of shapes
     */
    public Group(ArrayList<Shape> shapes) {
        this.transformation = new Transform(Mat4.identity);
        this.shapes = shapes;
    }

    /*
     * simple constructor
     */
    public Group() {
        this.transformation = new Transform(Mat4.identity);
    }


    @Override
    public Hit intersect(Ray r) throws Exception {

        Ray transformedRay = new Ray(
                transformation.invertedTransformation.transformPoint(r.origin),
                transformation.invertedTransformation.transformDirection(r.dir).normalise());
        // transformation to local object coordinate system

        ArrayList<Hit> hits = new ArrayList<>();
        for (Shape s : shapes) {
            Hit hit = s.intersect(transformedRay);
            if (hit != null) {
                hits.add(hit);
            }
        }
        if (hits.size() == 0) {
            return null;        // no hits
        } else {
            Collections.sort(hits);
            Hit h = hits.get(0); // returns hit with smallest t value (absolute value)
            return new Hit(
                    h.t,
                    transformation.transformation.transformPoint(h.pos),
                    h.material,
                    h.uv,
                    transformation.invertedTransformation.transpose().transformNormal(h.normVec).normalise());
            // transformation back to world coordinate system
        }
    }

    /*
     * Checks if one shape of the group is behind light source (and therefore not visible)
     */
    public boolean visible(Ray lightRay) throws Exception {

        for(Shape shape : shapes) {
            Hit hit = shape.intersect(lightRay);
            if(hit != null && hit.t < Double.POSITIVE_INFINITY) {
                return false;
            }
        }
        return true;
    }

    /*
     * add more shapes to group
     */
    public void addShape(Shape s) {
        shapes.add(s);
    }

    /*
     * add transformation to group
     */
    public void addTransformation(Transform t) {
        transformation = t;
    }


}
