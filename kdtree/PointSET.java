import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Stack;

public class PointSET {
    private final SET<Point2D> set;

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
        Point2D nearest = set.max();
        double smallestDis = p.distanceSquaredTo(set.max());
        for (Point2D setPoint : set) {
            if (p.distanceSquaredTo(setPoint) < smallestDis) nearest = setPoint;
            smallestDis = p.distanceSquaredTo(setPoint);
        }
        return nearest;
    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
        /*
        Point2D p = new Point2D(0.5, 0.5);
        Point2D p1 = new Point2D(0.1, 0.5);
        Point2D p2 = new Point2D(0.3, 0.5);
        PointSET pS = new PointSET();
        pS.insert(p);
        pS.insert(p1);
        pS.insert(p2);

        for (Point2D po : pS.set) {
            System.out.println(po);
        }


        pS.draw();
        RectHV rec = new RectHV(0.2, 0.3, 0.7, 0.7);
        rec.draw();

        Iterable<Point2D> temp = pS.range(rec);
        System.out.println(temp);

        System.out.println(pS.nearest(new Point2D(0, 0)));


         */
    }
}
