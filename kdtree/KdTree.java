import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private final Queue<Node> forDraw;
    private final Queue<Point2D> insideRect = new Queue<>();
    private Point2D champion;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private int size;
        private int depth;

        public Node(Point2D key, int size, int depth, RectHV rect) {
            this.p = key;
            this.size = size;
            this.depth = depth;
            this.rect = rect;
        }
    }

    public KdTree()                               // construct an empty set of points
    {
        forDraw = new Queue<>();

    }

    public boolean isEmpty()                      // is the set empty?
    {
        return size() == 0;
    }

    public int size()                         // number of points in the set
    {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        root = insert(root, p);


    }

    private Node insert(Node x, Point2D key) {
        // x = previous
        if (x == null) {
            Node temp = new Node(key, 1, 0, new RectHV(0, 0, 10, 10));
            forDraw.enqueue(temp);
            return temp;
        }

        double xMin;
        double yMin;
        double xMax;
        double yMax;

        int cmp;
        // Odd
        if (x.depth % 2 != 0) {
            cmp = Double.compare(key.y(), x.p.y());
            // Down
            if (cmp < 0) {
                xMin = x.rect.xmin();
                yMin = x.rect.ymin();
                xMax = x.rect.xmax();
                yMax = x.p.y();
                x.lb = insert(x.lb, key);
                x.lb.depth = x.depth + 1;
                x.lb.rect = new RectHV(xMin, yMin, xMax, yMax);
                forDraw.enqueue(x.lb);
            }
            // UP
            else if (cmp > 0) {
                xMin = x.rect.xmin();
                yMin = x.p.y();
                xMax = x.rect.xmax();
                yMax = x.rect.ymax();
                x.rt = insert(x.rt, key);
                x.rt.depth = x.depth + 1;
                x.rt.rect = new RectHV(xMin, yMin, xMax, yMax);
                forDraw.enqueue(x.rt);
            } else {
                if (Double.compare(key.x(), x.p.x()) != 0) {
                    xMin = x.rect.xmin();
                    yMin = x.p.y();
                    xMax = x.rect.xmax();
                    yMax = x.rect.ymax();
                    x.rt = insert(x.rt, key);
                    x.rt.depth = x.depth + 1;
                    x.rt.rect = new RectHV(xMin, yMin, xMax, yMax);
                    forDraw.enqueue(x.rt);

                }
            }
            // Even
        } else {
            cmp = Double.compare(key.x(), x.p.x());
            // Left
            if (cmp < 0) {
                xMin = x.rect.xmin();
                yMin = x.rect.ymin();
                xMax = x.p.x();
                yMax = x.rect.ymax();
                x.lb = insert(x.lb, key);
                x.lb.depth = x.depth + 1;
                x.lb.rect = new RectHV(xMin, yMin, xMax, yMax);
                forDraw.enqueue(x.lb);
            }
            // Right
            else if (cmp > 0) {
                xMin = x.p.x();
                yMin = x.rect.ymin();
                xMax = x.rect.xmax();
                yMax = x.rect.ymax();
                x.rt = insert(x.rt, key);
                x.rt.depth = x.depth + 1;
                x.rt.rect = new RectHV(xMin, yMin, xMax, yMax);
                forDraw.enqueue(x.rt);
            } else {
                if (Double.compare(key.y(), x.p.y()) != 0) {
                    xMin = x.p.x();
                    yMin = x.rect.ymin();
                    xMax = x.rect.xmax();
                    yMax = x.rect.ymax();
                    x.rt = insert(x.rt, key);
                    x.rt.depth = x.depth + 1;
                    x.rt.rect = new RectHV(xMin, yMin, xMax, yMax);
                    forDraw.enqueue(x.rt);
                }
            }
        }
        x.size = 1 + size(x.lb) + size(x.rt);
        return x;
    }


    public boolean contains(Point2D p)            // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return get(root, p) != null;
    }


    private Point2D get(Node x, Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (x == null) return null;
        int cmp;
        // ODD
        if (x.depth % 2 != 0) {
            cmp = Double.compare(p.y(), x.p.y());
            if (cmp < 0) return get(x.lb, p);
            else if (cmp > 0) return get(x.rt, p);
            else {
                if (Double.compare(p.x(), x.p.x()) == 0) {

                    return x.p;
                } else return get(x.rt, p);


            }
            // EVEN
        } else {
            cmp = Double.compare(p.x(), x.p.x());
            if (cmp < 0) return get(x.lb, p);
            else if (cmp > 0) return get(x.rt, p);
            else {
                if (Double.compare(p.y(), x.p.y()) == 0) {

                    return x.p;

                } else return get(x.rt, p);


            }
        }


    }


    public void draw()                         // draw all points to standard draw
    {
        StdDraw.setPenRadius(0.01);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.square(0.5, 0.5, 0.5);
        for (Node n : forDraw) {
            n.p.draw();
            n.rect.draw();

        }
    }

    public Iterable<Point2D> range(RectHV rect)    // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();

        range(root, rect);
        return insideRect;
    }

    private void range(Node x, RectHV rect) {
        if (x != null) {
            if (rect.contains(x.p)) insideRect.enqueue(x.p);
            if (x.lb != null) {
                if (rect.intersects(x.lb.rect)) range(x.lb, rect);
            }
            if (x.rt != null) {
                if (rect.intersects(x.rt.rect)) range(x.rt, rect);
            }
        }


    }

    public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        nearest(root, p);
        return champion;
    }

    private void nearest(Node x, Point2D p) {
        if (x != null) {
            if (x.depth == 0) {
                champion = root.p;
            }

            if (p.distanceSquaredTo(x.p) < p.distanceSquaredTo(champion)) {
                champion = x.p;
            }
            // ODD
            if (x.depth % 2 != 0) {


                if (p.y() >= x.p.y()) {
                    nearest(x.rt, p);
                    if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < p.distanceSquaredTo(champion)) {
                        nearest(x.lb, p);
                    }
                } else {
                    nearest(x.lb, p);
                    if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < p.distanceSquaredTo(champion)) {
                        nearest(x.rt, p);
                    }
                }

                // EVEN
            } else {

                if (p.x() >= x.p.x()) {
                    nearest(x.rt, p);
                    if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < p.distanceSquaredTo(champion)) {
                        nearest(x.lb, p);
                    }
                } else {
                    nearest(x.lb, p);
                    if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < p.distanceSquaredTo(champion)) {
                        nearest(x.rt, p);
                    }
                }

            }


        }


    }

    public static void main(String[] args)                  // unit testing of the methods (optional)
    {
/*
        KdTree kd = new KdTree();
        Point2D p1 = new Point2D(0.0, 0.625);
        Point2D p2 = new Point2D(0.5, 0.0);
        Point2D p3 = new Point2D(0.125, 0.25);
        Point2D p4 = new Point2D(0.375, 0.375);
        Point2D p5 = new Point2D(0.25, 0.75);
        //Point2D p6 = new Point2D(0.7, 0.7);

        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);
        // kd.insert(p6);
        // System.out.println(kd.root.rt.rt.rt.lb.p);
        // System.out.println(kd.size());
        // System.out.println(kd.size());
        // System.out.println(kd.contains(new Point2D(0.2, 0.9)));
        // kd.draw();
        // RectHV queryRec = new RectHV(0.3, 0.3, 0.7, 0.7);
        // queryRec.draw();
        // Iterable<Point2D> iter = kd.range(queryRec);
        // System.out.println(iter);
        // System.out.println(kd.nearest(new Point2D(0.269, 0.222)));
        // System.out.println(kd.nearest(new Point2D(0.796, 0.571)));
        // System.out.println(kd.nearest(new Point2D(0.75, 0.5)));
        // System.out.println(kd.root.lb.rt.p);

*/
/*
        KdTree kd = new KdTree();
        Point2D p1 = new Point2D(0.7, 0.2);
        Point2D p2 = new Point2D(0.5, 0.4);
        Point2D p3 = new Point2D(0.2, 0.3);
        Point2D p4 = new Point2D(0.4, 0.7);
        Point2D p5 = new Point2D(0.9, 0.6);
        kd.insert(p1);
        kd.insert(p2);
        kd.insert(p3);
        kd.insert(p4);
        kd.insert(p5);
        System.out.println(kd.nearest(new Point2D(0.67, 0.18)));



 */
    }
}
