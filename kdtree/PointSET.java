import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

public class PointSET {
    private final SET<Point2D> set;
    private Point2D champion;

    public PointSET()                               // construct an empty set of points
    {
        set = new SET<>();
    }

    public boolean isEmpty()                      // is the set empty?
    {
        return set.size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return set.size();
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        if (!set.contains(p)) set.add(p);
    }

    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return set.contains(p);
    }

    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(0.5, 0.5, 0.5);
        for (Point2D p : set) {
            StdDraw.point(p.x(), p.y());
        }

    }

    public Iterable<Point2D> range(RectHV rect)    // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();
        Stack<Point2D> insideRect = new Stack<>();
        for (Point2D setPoint : set) {
            if (rect.contains(setPoint)) insideRect.push(setPoint);
        }
        return insideRect;
    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        if (set.isEmpty()) return null;
        champion = set.max();
        for (Point2D setPoint : set) {
            if (p.distanceSquaredTo(setPoint) < p.distanceSquaredTo(champion)) champion = setPoint;
        }
        return champion;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        /*
        KdTree kd = new KdTree();
        Point2D p1 = new Point2D(0.875, 0.0);
        Point2D p2 = new Point2D(1.0, 0.125);
        Point2D p3 = new Point2D(0.125, 0.5);
        Point2D p4 = new Point2D(0.5, 1.0);
        Point2D p5 = new Point2D(0.25, 0.875);


        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);

        System.out.println(kd.nearest(new Point2D(0.625, 0.75)));

         */
    }
}
